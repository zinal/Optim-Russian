/*
 *  (c) Copyright IBM Corp. 2021 All rights reserved.
 *
 *  The following sample of source code ("Sample") is owned by International
 *  Business Machines Corporation or one of its subsidiaries ("IBM") and is
 *  copyrighted and licensed, not sold. You may use, copy, modify, and
 *  distribute the Sample in any form without payment to IBM.
 *
 *  The Sample code is provided to you on an "AS IS" basis, without warranty of
 *  any kind.
 *  IBM HEREBY EXPRESSLY DISCLAIMS ALL WARRANTIES, EITHER EXPRESS OR
 *  IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. Some jurisdictions do
 *  not allow for the exclusion or limitation of implied warranties, so the above
 *  limitations or exclusions may not apply to you. IBM shall not be liable for
 *  any damages you suffer as a result of using, copying, modifying or
 *  distributing the Sample, even if IBM has been advised of the possibility of
 *  such damages.
 *
 *  Author:   Maksim Zinal <mzinal@ru.ibm.com>
 */
package com.ibm.optim.ru.dict;

import com.ibm.optim.ru.supp.DbUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author zinal
 */
public class FioDbWriter extends DbUtils implements AutoCloseable {

    private static final org.slf4j.Logger LOG =
            org.slf4j.LoggerFactory.getLogger(FioDbWriter.class);

    private static final String[] SQL_CREATE = {
        "CREATE TABLE dict_fio("
            + "id INTEGER NOT NULL PRIMARY KEY, "
            + "nfull VARCHAR(150) NOT NULL, "
            + "nfirst VARCHAR(40) NOT NULL, "
            + "nmiddle VARCHAR(40) NOT NULL, "
            + "nlast VARCHAR(40) NOT NULL,"
            + "sex CHAR(1) NOT NULL)",
        "CREATE TABLE dict_fio_male("
            + "id INTEGER NOT NULL PRIMARY KEY, "
            + "nfull VARCHAR(150) NOT NULL, "
            + "nfirst VARCHAR(40) NOT NULL, "
            + "nmiddle VARCHAR(40) NOT NULL, "
            + "nlast VARCHAR(40) NOT NULL)",
        "CREATE TABLE dict_fio_female("
            + "id INTEGER NOT NULL PRIMARY KEY, "
            + "nfull VARCHAR(150) NOT NULL, "
            + "nfirst VARCHAR(40) NOT NULL, "
            + "nmiddle VARCHAR(40) NOT NULL, "
            + "nlast VARCHAR(40) NOT NULL)",
        "CREATE TABLE dict_name_last("
            + "id INTEGER NOT NULL PRIMARY KEY, "
            + "val VARCHAR(40) NOT NULL, "
            + "sex CHAR(1) NOT NULL)",
        "CREATE TABLE dict_name_first("
            + "id INTEGER NOT NULL PRIMARY KEY, "
            + "val VARCHAR(40) NOT NULL, "
            + "sex CHAR(1) NOT NULL)",
        "CREATE INDEX dict_name_first_ix1 "
            + "ON dict_name_first(val)",
        "CREATE TABLE dict_name_middle("
            + "id INTEGER NOT NULL PRIMARY KEY, "
            + "val VARCHAR(40) NOT NULL, "
            + "sex CHAR(1) NOT NULL)"
    };

    private Connection connection;
    private PreparedStatement psAddMale;
    private PreparedStatement psAddFemale;
    private PreparedStatement psAddAny;

    private int countAll = 0;
    private int countMale = 0;
    private int countFemale = 0;

    private final Properties config;
    private NamesSource controller = null;
    private long lastUpdated = 0L;

    public FioDbWriter(Properties config) {
        this.config = config;
    }

    public NamesSource makeController() throws Exception {
        if (controller==null) {
            controller = new NamesSourceBuilder().loadNames(config);
            controller.start();
            LOG.info("Started controller {}", controller.toString());
        }
        return controller;
    }

    @Override
    public void close() {
        if (controller!=null) {
            controller.stop();
            controller = null;
        }
        DbUtils.close(psAddMale);  psAddMale = null;
        DbUtils.close(psAddFemale);  psAddFemale = null;
        DbUtils.close(psAddAny);  psAddAny = null;
        if (connection != null) {
            try {
                connection.rollback();
            } catch(Exception ex) {
            }
            try {
                connection.close();
            } catch(Exception ex) {}
            connection = null;
        }
    }

    /**
     * Create a new database, throwing the exception if one exists.
     * @param pathname Path to the database
     * @throws Exception In case of an error
     */
    public void create(String pathname) throws Exception {
        deleteFiles(pathname);
        final Connection con = DriverManager.getConnection
            (makeH2Url(pathname)
                    + ";COMPRESS=YES"
                    + ";LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0");
        try {
            con.setAutoCommit(false);
            createTables(con, SQL_CREATE);
            con.commit();
        } catch(Exception ex) {
            try { con.rollback(); } catch(Exception xx) {}
            try { con.close(); } catch(Exception xx) {}
            deleteFiles(pathname);
            throw new Exception("Table creation failed", ex);
        }
        this.connection = con;
    }

    public void openUrl(String url, String username, String password)
            throws Exception {
        final Connection con
                = DriverManager.getConnection(url, username, password);
        try {
            con.setAutoCommit(false);
            createTables(con, SQL_CREATE);
            con.commit();
        } catch(Exception ex) {
            try { con.rollback(); } catch(Exception xx) {}
            try { con.close(); } catch(Exception xx) {}
            throw new Exception("Table creation failed", ex);
        }
        this.connection = con;
    }

    public void generate(int count) throws Exception {
        saveNames();
        LOG.info("Name dictionaries saved to database.");
        lastUpdated = System.currentTimeMillis();
        int steps = 0;
        for (int i=0; i<count; ++i) {
            writeRow(true, makeController().nextMale());
            writeRow(false, makeController().nextFemale());
            if (checkCommit(++steps))
                steps = 0;
        } // for (...)
        if (steps>0) {// Have some rows still uncommitted
            checkCommit(-1);
        }
    }

    /**
     * Commit if number of inserted rows exceed threshold.
     * @param steps Number of inserted rows, or -1 on final call
     * @return true, if committed, false otherwise
     * @throws Exception
     */
    private boolean checkCommit(int steps)
            throws Exception {
        if (steps>=0 && steps<500)
            return false;
        psAddMale.executeBatch();
        psAddFemale.executeBatch();
        psAddAny.executeBatch();
        connection.commit();
        long tv = System.currentTimeMillis();
        if (steps<0 || tv - lastUpdated >= 5000L) {
            int dupCount = makeController().getDuplicateCount();
            if (dupCount > 0) {
                LOG.info("Generated {} names, {} duplicates",
                        countAll, dupCount);
            } else {
                LOG.info("Generated {} names", countAll);
            }
            lastUpdated = tv;
        }
        return true;
    }

    private void writeRow(boolean male, NameValues nv) throws Exception {
        if (psAddAny==null) {
            psAddAny = connection.prepareStatement("INSERT "
                        + "INTO dict_fio "
                        + "(id, nfull, nfirst, nmiddle, nlast, sex) "
                        + "VALUES (?, ?, ?, ?, ?, ?)");
        }
        psAddAny.setInt(1, countAll++);
        psAddAny.setString(2, nv.full);
        psAddAny.setString(3, nv.first);
        psAddAny.setString(4, nv.middle);
        psAddAny.setString(5, nv.last);
        psAddAny.setString(6, male ? "M" : "T");
        psAddAny.addBatch();
        final PreparedStatement ps;
        if (male) {
            if (psAddMale==null) {
                psAddMale = connection.prepareStatement("INSERT "
                        + "INTO dict_fio_male "
                        + "(id, nfull, nfirst, nmiddle, nlast) "
                        + "VALUES (?, ?, ?, ?, ?)");
            }
            ps = psAddMale;
        } else {
            if (psAddFemale==null) {
                psAddFemale = connection.prepareStatement("INSERT "
                        + "INTO dict_fio_female "
                        + "(id, nfull, nfirst, nmiddle, nlast) "
                        + "VALUES (?, ?, ?, ?, ?)");
            }
            ps = psAddFemale;
        }
        if (male) {
            ps.setInt(1, countMale++);
        } else {
            ps.setInt(1, countFemale++);
        }
        ps.setString(2, nv.full);
        ps.setString(3, nv.first);
        ps.setString(4, nv.middle);
        ps.setString(5, nv.last);
        ps.addBatch();
    }

    private void savePair(String tabName, List<NamesBean> male,
            List<NamesBean> female) throws Exception {
        PreparedStatement ps = null;
        int index;
        try {
            index = 0;
            ps = connection.prepareStatement("INSERT INTO " + tabName
                    + "(id,val,sex) VALUES(?,?,?)");
            for (NamesBean nb : male) {
                ps.setInt(1, index);
                ps.setString(2, nb.getName());
                ps.setString(3, "M");
                ps.execute();
                ++index;
            }
            for (NamesBean nb : female) {
                ps.setInt(1, index);
                ps.setString(2, nb.getName());
                ps.setString(3, "F");
                ps.execute();
                ++index;
            }
            ps.close();
        } finally {
            DbUtils.close(ps);
        }
    }

    private void saveNames() throws Exception {
        if (controller != null) {
            NamesData dataMale = makeController().getDataMale();
            NamesData dataFemale = makeController().getDataFemale();
            savePair("dict_name_first", dataMale.first, dataFemale.first);
            savePair("dict_name_last", dataMale.last, dataFemale.last);
            savePair("dict_name_middle", dataMale.middle, dataFemale.middle);
        }
    }

}
