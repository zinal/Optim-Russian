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
import com.ibm.optim.ru.gen.*;
import com.ibm.optim.ru.supp.DbUtils;

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

    private final Properties props;

    private Connection con;

    private final Random coin = new Random();
    private final RandomStringGenerator boober
           = new RandomStringGenerator.Builder()
                   .withinRange(new char[]{' ', ' '}, new char[]{'а', 'я'}, new char[]{'А', 'Я'})
            .build();
    
    private NamesSource names = null;
    private final ValueGenerator innPhy = new InnGen(true, InnGen.Type.Physical);
    private final ValueGenerator innLeg = new InnGen(true, InnGen.Type.Legal);
    private final ValueGenerator ogrnPhy = new OgrnGen(true, OgrnGen.Type.Physical);
    private final ValueGenerator ogrnCom = new OgrnGen(true, OgrnGen.Type.Commercial);
    private final ValueGenerator ogrnGov = new OgrnGen(true, OgrnGen.Type.Government);
    private final ValueGenerator passDom = new PassportGen(true, PassportGen.Type.Domestic);
    private final ValueGenerator passFor = new PassportGen(true, PassportGen.Type.Foreign);
    private final ValueGenerator snils = new SnilsGen(true);

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
       
       for (int i=0; i<100; i++) {
           int id = makeLegal();
           addPhones(id);
           addEmails(id);
       }

       for (int i=0; i<200; i++) {
           int id = makePhysical();
           addPhones(id);
           addEmails(id);
       }

       con.commit();
    }

    private NamesSource getNames() throws Exception {
        if (names==null) {
            names = new NamesSourceBuilder().loadNames(props);
            names.setAntiDupProtection(true);
            names.setReorder(true);
        }
        return names;
    }

    private int makeCustomer(boolean physical) throws Exception {
        if (psCustomer==null) {
            psCustomer = con.prepareStatement("INSERT INTO optim1.customer(custid, custmode)"
                    + " VALUES(nextval('optim1.customer_seq'),?) RETURNING custid");
        }
        psCustomer.setString(1, physical ? "P" : "L");
        psCustomer.executeUpdate();
        try (ResultSet rs = psCustomer.getResultSet()) {
            return rs.getInt(1);
        }
    }

    private int makeLegal() throws Exception {
        int id = makeCustomer(false);
        
        String name = "-";
        String regno = "-";
        String payno = "-";

        if ( coin.nextBoolean() ) {
            // ИП
            if (coin.nextBoolean()) {
                name = names.nextFemale().full;
            } else {
                name = names.nextMale().full;
            }
            regno = ogrnPhy.nextValue();
            payno = innPhy.nextValue();
        } else if ( coin.nextBoolean() && coin.nextBoolean() ) {
            // Гос. организация
            name = "Министерство " + boober.generate(5, 40);
            regno = ogrnGov.nextValue();
            payno = innLeg.nextValue();
        } else {
            // Коммерческая структура
            name = "ООО " + boober.generate(5, 40);
            regno = ogrnCom.nextValue();
            payno = innLeg.nextValue();
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

    private int makePhysical() throws Exception {
        int id = makeCustomer(true);
        
        boolean sex = coin.nextBoolean();
        NameValues name = sex ? names.nextFemale() : names.nextMale();
        String payno = innPhy.nextValue();
        String socno = snils.nextValue();
        LocalDate ld = LocalDate.now();
        ld.minusYears(20);
        ld.minusDays(coin.nextInt(365*50));

        if (psPhysical==null) {
            psPhysical = con.prepareStatement("INSERT INTO optim1.physical_entity "
                    + "(custid, pe_name_full, pe_name_first, pe_name_middle, pe_name_last, "
                    + "pe_sex, pe_num_pay, pe_num_soc, pe_birthday) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)");
        }
        psPhysical.setInt(1, id);
        psPhysical.setString(2, name.full);
        psPhysical.setString(3, name.first);
        psPhysical.setString(4, name.middle);
        psPhysical.setString(5, name.last);
        psPhysical.setString(6, sex ? "F" : "M");
        psPhysical.setString(7, payno);
        psPhysical.setString(8, socno);
        psPhysical.setDate(9, new java.sql.Date(ld.toEpochDay()));
        psPhysical.executeUpdate();

        return id;
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
        psContact.executeUpdate();
        try (ResultSet rs = psContact.getResultSet()) {
            return rs.getInt(1);
        }
    }
    
    private int addPhone(int customer) throws Exception {
        if ( psPhone == null ) {
            psPhone = con.prepareStatement("INSERT INTO optim1.contact_phone "
                    + "(contid, phoneval) VALUES (?, ?)");
        }
        int id = addContact(customer, ContactType.Phone);
        psPhone.setInt(1, id);
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
        int id = addContact(customer, ContactType.Phone);
        psEmail.setInt(1, id);
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
                    getNames().addKnown(rs.getString(1));
                    innPhy.addExisting(rs.getString(2));
                    snils.addExisting(rs.getString(3));
                }
            }
        }
    }

    private void loadLegal() throws Exception {
        try (PreparedStatement ps = con.prepareStatement("SELECT le_num_reg, "
                + "le_num_pay, le_name FROM optim1.legal_entity")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String reg = rs.getString(1);
                    if (reg!=null && reg.length()>0) {
                        if (reg.charAt(0)=='1') {
                            ogrnCom.addExisting(reg);
                        } else if (reg.charAt(0)=='1') {
                            ogrnGov.addExisting(reg);
                        } else {
                            ogrnPhy.addExisting(reg);
                            getNames().addKnown(rs.getString(3));
                        }
                    }
                    innLeg.addExisting(rs.getString(2));
                }
            }
        }
    }

}
