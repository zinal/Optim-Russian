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
package com.ibm.optim.ru.gen;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;
import java.time.LocalDate;
import org.apache.commons.text.RandomStringGenerator;
import com.ibm.optim.ru.dict.*;
import com.ibm.optim.ru.supp.DbUtils;
import java.io.File;

/**
 *
 * @author zinal
 */
public class DataGen implements AutoCloseable {

    private static final org.slf4j.Logger LOG
           = org.slf4j.LoggerFactory.getLogger(DataGen.class);

    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            try (FileReader fr = new FileReader("data-sample.properties")) {
                props.load(fr);
            }
            new DataGen(props) . run();
        } catch(Exception ex) {
            LOG.error("FATAL", ex);
            System.exit(1);
        }
    }

    public static final String PROP_URL = "DbUrl";
    public static final String PROP_USER = "DbUser";
    public static final String PROP_PASS = "DbPassword";
    public static final String PROP_FLOWERS = "Flowers";

    private final Properties props;

    private Connection con;

    private final Random coin = new Random();
    private final RandomStringGenerator boober
           = new RandomStringGenerator.Builder()
                   .withinRange(new char[]{' ', ' '}, new char[]{'а', 'я'}, new char[]{'А', 'Я'})
            .build();

    private NamesSource genPhyNames = null;
    private OrgNameGen genComNames = null;
    private OrgNameGen genGovNames = null;
    private final ValueGenerator genInnPhy = new InnGen(true, InnGen.Type.Physical);
    private final ValueGenerator genInnLeg = new InnGen(true, InnGen.Type.Legal);
    private final ValueGenerator genOgrnPhy = new OgrnGen(true, OgrnGen.Type.Physical);
    private final ValueGenerator genOgrnCom = new OgrnGen(true, OgrnGen.Type.Commercial);
    private final ValueGenerator genOgrnGov = new OgrnGen(true, OgrnGen.Type.Government);
    private final ValueGenerator genPassDom = new PassportGen(true, PassportGen.Type.Domestic);
    private final ValueGenerator genPassFor = new PassportGen(true, PassportGen.Type.Foreign);
    private final ValueGenerator genSnils = new SnilsGen(true);
    private final ValueGenerator genPhone = new PhoneGen();
    private final ValueGenerator genEmail = new EmailGen();

    private PreparedStatement psCustomer = null;
    private PreparedStatement psLegal = null;
    private PreparedStatement psPhysical = null;
    private PreparedStatement psContact = null;
    private PreparedStatement psPhone = null;
    private PreparedStatement psEmail = null;

    private DataGen(Properties props) throws Exception {
        this.props = props;
        this.con = DriverManager.getConnection(
                props.getProperty(PROP_URL),
                props.getProperty(PROP_USER),
                props.getProperty(PROP_PASS));
    }

    private void run() throws Exception {
       LOG.info("Connected, initializing...");
       con.setAutoCommit(false);

       { // Значение соли делаем каждый раз новое
           String salt = props.getProperty(PropNames.PROP_SALT, "");
           if (salt==null || salt.length()==0)
               salt = "-";
           salt = salt + " " + Long.toHexString(System.currentTimeMillis());
           props.setProperty(PropNames.PROP_SALT, salt);
       }

       loadPhysical();
       loadLegal();

       LOG.info("Initialized, ready to generate data...");

       for (int i=0; i<20000; i++) {
           int id = addLegalEntity();
           addPhones(id);
           addEmails(id);
           logProgress(i, 20000, "legal entities");
       }

       LOG.info("Legal entitities ready!");

       for (int i=0; i<150000; i++) {
           int id = addPhysicalEntity();
           addPhones(id);
           addEmails(id);
           logProgress(i, 150000, "physical entities");
       }

       LOG.info("Physical entitities ready!");

       con.commit();

       LOG.info("Transaction committed!");
    }

    private NamesSource makePhyNames() throws Exception {
        if (genPhyNames==null) {
            LOG.info("Loading names dictionary...");
            genPhyNames = new NamesSourceBuilder().loadNames(props);
            genPhyNames.setAntiDupProtection(true);
            genPhyNames.setReorder(true);
            LOG.info("Names dictionary ready!");
        }
        return genPhyNames;
    }

    private OrgNameGen makeComNames() throws Exception {
        if (genComNames==null) {
            LOG.info("Loading COMMERCIAL dictionary...");
            File f = new File(props.getProperty(PROP_FLOWERS, "dict/flowers.txt"));
            genComNames = new OrgNameGen(f, true);
            LOG.info("COMMERCIAL dictionary ready!");
        }
        return genComNames;
    }

    private OrgNameGen makeGovNames() throws Exception {
        if (genGovNames==null) {
            LOG.info("Loading GOVERNMENTAL dictionary...");
            File f = new File(props.getProperty(PROP_FLOWERS, "dict/flowers.txt"));
            genGovNames = new OrgNameGen(f, false);
            LOG.info("GOVERNMENTAL dictionary ready!");
        }
        return genGovNames;
    }

    private int addCustomer(boolean physical) throws Exception {
        if (psCustomer==null) {
            psCustomer = con.prepareStatement("INSERT INTO optim1.customer(custid, custmode)"
                    + " VALUES(nextval('optim1.customer_seq'),?) RETURNING custid");
        }
        psCustomer.setString(1, physical ? "P" : "L");
        psCustomer.execute();
        try (ResultSet rs = psCustomer.getResultSet()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private int addLegalEntity() throws Exception {
        int id = addCustomer(false);

        String name;
        String regno;
        String payno;

        if ( coin.nextBoolean() ) {
            // ИП
            if (coin.nextBoolean()) {
                name = makePhyNames().nextFemale().full;
            } else {
                name = makePhyNames().nextMale().full;
            }
            regno = genOgrnPhy.nextValue();
            payno = genInnPhy.nextValue();
        } else if ( coin.nextBoolean() 
                && coin.nextBoolean() 
                && coin.nextBoolean()
                && coin.nextBoolean() ) {
            // Гос. организация
            name = makeGovNames().nextValue();
            regno = genOgrnGov.nextValue();
            payno = genInnLeg.nextValue();
        } else {
            // Коммерческая структура
            name = makeComNames().nextValue();
            regno = genOgrnCom.nextValue();
            payno = genInnLeg.nextValue();
        }

        if (psLegal==null) {
            psLegal = con.prepareStatement("INSERT INTO optim1.legal_entity "
                    + "(custid, le_name, le_num_reg, le_num_pay) "
                    + "VALUES (?,?,?,?)");
        }
        psLegal.setInt(1, id);
        psLegal.setString(2, name);
        psLegal.setString(3, regno);
        psLegal.setString(4, payno);
        psLegal.executeUpdate();

        return id;
    }

    private int addPhysicalEntity() throws Exception {
        int id = addCustomer(true);

        boolean sex = coin.nextBoolean();
        NameValues name = sex ? genPhyNames.nextFemale() : genPhyNames.nextMale();
        String payno = genInnPhy.nextValue();
        String socno = genSnils.nextValue();
        LocalDate dateBirth = LocalDate.now();
        dateBirth = dateBirth.minusYears(20);
        dateBirth = dateBirth.minusDays(coin.nextInt(365*50));
        LocalDate datePasspIss = dateBirth.plusYears(18);
        LocalDate datePasspFor = dateBirth.plusYears(20);
        LocalDate datePasspExp = datePasspFor.plusYears(10);
        String passpdom = genPassDom.nextValue();
        String passpfor = genPassFor.nextValue();

        if (psPhysical==null) {
            psPhysical = con.prepareStatement("INSERT INTO optim1.physical_entity "
                    + "(custid, pe_name_full, pe_name_first, pe_name_middle, pe_name_last, "
                    + "pe_sex, pe_num_pay, pe_num_soc, pe_birthday,"
                    + "pe_passp_num, pe_passp_iss_date, "
                    + "pe_forg_num, pe_forg_iss_date, pe_forg_exp_date) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        }
        psPhysical.setInt(1, id);
        psPhysical.setString(2, name.full);
        psPhysical.setString(3, name.first);
        psPhysical.setString(4, name.middle);
        psPhysical.setString(5, name.last);
        psPhysical.setString(6, sex ? "F" : "M");
        psPhysical.setString(7, payno);
        psPhysical.setString(8, socno);
        psPhysical.setDate(9, java.sql.Date.valueOf(dateBirth));
        psPhysical.setString(10, passpdom);
        psPhysical.setDate(11, java.sql.Date.valueOf(datePasspIss));
        psPhysical.setString(12, passpfor);
        psPhysical.setDate(13, java.sql.Date.valueOf(datePasspFor));
        psPhysical.setDate(14, java.sql.Date.valueOf(datePasspExp));
        psPhysical.executeUpdate();

        return id;
    }

    private long lastLogProgress = 0L;

    private void logProgress(int ncurrent, int ntotal, String xtype) {
        if (lastLogProgress==0L) {
            lastLogProgress = System.currentTimeMillis();
            return;
        }
        if (ncurrent % 10 != 0)
            return;
        long tv = System.currentTimeMillis();
        if (tv-lastLogProgress >= 5000L) {
            lastLogProgress = tv;
            LOG.info("** Progress: {} of {} at {}", ncurrent, ntotal, xtype);
        }
    }

    public static enum ContactType {
        Phone,
        Email
    };

    private int addContact(int customer, ContactType ct) throws Exception {
        if ( psContact == null ) {
            psContact = con.prepareStatement("INSERT INTO optim1.contact "
                    + "(contid, custid, contmode) "
                    + "VALUES (nextval('optim1.customer_seq'), ?, ?) "
                    + "RETURNING contid");
        }
        psContact.setInt(1, customer);
        switch (ct) {
            case Email:
                psContact.setString(2, "E");
                break;
            case Phone:
                psContact.setString(2, "P");
                break;
        }
        psContact.execute();
        try (ResultSet rs = psContact.getResultSet()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private int addPhone(int customer) throws Exception {
        if ( psPhone == null ) {
            psPhone = con.prepareStatement("INSERT INTO optim1.contact_phone "
                    + "(contid, phoneval) VALUES (?, ?)");
        }
        String phone = genPhone.nextValue();
        int id = addContact(customer, ContactType.Phone);
        psPhone.setInt(1, id);
        psPhone.setString(2, phone);
        psPhone.executeUpdate();
        return id;
    }

    private void addPhones(int customer) throws Exception {
        // 1-4 телефона
        addPhone(customer);
        if (coin.nextBoolean()) {
            addPhone(customer);
            if (coin.nextBoolean()) {
                addPhone(customer);
                if (coin.nextBoolean()) {
                    addPhone(customer);
                }
            }
        }
    }

    private int addEmail(int customer) throws Exception {
        if ( psEmail == null ) {
            psEmail = con.prepareStatement("INSERT INTO optim1.contact_email "
                    + "(contid, emailval) VALUES (?, ?)");
        }
        String email = genEmail.nextValue();
        int id = addContact(customer, ContactType.Phone);
        psEmail.setInt(1, id);
        psEmail.setString(2, email);
        psEmail.executeUpdate();
        return id;
    }

    private void addEmails(int customer) throws Exception {
        // 1-3 адреса email
        addEmail(customer);
        if (coin.nextBoolean()) {
            addEmail(customer);
            if (coin.nextBoolean()) {
                addEmail(customer);
            }
        }
    }

    @Override
    public void close() {
        DbUtils.close(psCustomer);  psCustomer = null;
        DbUtils.close(psLegal);     psLegal = null;
        DbUtils.close(psPhysical);  psPhysical = null;
        DbUtils.close(psContact);   psContact = null;
        DbUtils.close(psPhone);     psPhone = null;
        DbUtils.close(psEmail);     psEmail = null;
        if (con!=null) {
            try {
                if (!con.isReadOnly())
                    con.rollback();
            } catch(Exception ex) {}
            try {
                con.close();
            } catch(Exception ex) {}
            con = null;
        }
    }

    private void loadPhysical() throws Exception {
        try (PreparedStatement ps = con.prepareStatement("SELECT pe_name_full, "
                + "pe_num_pay, pe_num_soc FROM optim1.physical_entity")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    makePhyNames().addKnown(rs.getString(1));
                    genInnPhy.addExisting(rs.getString(2));
                    genSnils.addExisting(rs.getString(3));
                }
            }
        }
    }

    private void loadLegal() throws Exception {
        try (PreparedStatement ps = con.prepareStatement("SELECT le_num_pay, "
                + "le_num_reg, le_name FROM optim1.legal_entity")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    genInnLeg.addExisting(rs.getString(1));
                    String reg = rs.getString(2);
                    String name = rs.getString(3);
                    if (reg!=null && reg.length()>0) {
                        switch (reg.charAt(0)) {
                            case '1':
                                genOgrnCom.addExisting(reg);
                                makeComNames().addExisting(name);
                                break;
                            case '2':
                                genOgrnGov.addExisting(reg);
                                makeGovNames().addExisting(name);
                                break;
                            default:
                                genOgrnPhy.addExisting(reg);
                                makePhyNames().addKnown(name);
                                break;
                        }
                    }
                }
            }
        }
    }

}
