/*
 * dsmask-jconf - configuration program for advanced masking DataStage operator
 * Maksim Zinal <mzinal@ru.ibm.com>
 */
package com.ibm.optim.ru.dict;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Dictionary builder entry point.
 * @author zinal
 */
public class DictFio extends PropNames {

    private static final org.slf4j.Logger LOG =
            org.slf4j.LoggerFactory.getLogger(DictFio.class);

    private final Properties props;

    public DictFio(Properties props) {
        this.props = props;
    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("USAGE: " + DictFio.class.getName()
                        + " jobfile.xml");
                System.exit(1);
            }
            final Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(args[0])) {
                props.loadFromXML(fis);
            }

            LOG.info("Starting FIO dictionary generator...");
            new DictFio(props).run();
            LOG.info("FIO dictionary generator completed!");

        } catch(Exception ex) {
            LOG.error("FATAL: operation failed", ex);
            System.exit(1);
        }
    }

    private void run() throws Exception {
        // Create or truncate tables?
        final TableAction action = TableAction.valueOf(
                props.getProperty(PROP_TABACT, TableAction.CREATE.name()));
        try (FioDbWriter fgen = new FioDbWriter(props)) {
            final String dbName = props.getProperty(PROP_DBNAME);
            if (dbName!=null) {
                fgen.create(dbName, action);
            } else {
                final String dbUrl = props.getProperty(PROP_DBURL);
                final String dbUser = props.getProperty(PROP_DBUSER);
                final String dbPass = props.getProperty(PROP_DBPASS);
                if (dbUrl==null || dbUser==null || dbPass==null) {
                    throw new IllegalArgumentException("Incorrect configuration:"
                            + " either DbName or DbUrl must be specified");
                }
                fgen.openUrl(dbUrl, dbUser, dbPass, action);
            }
            LOG.info("Target database open.");
            fgen.generate(Integer.valueOf(props.getProperty(PROP_TOTNAMES)));
        }
    }

}
