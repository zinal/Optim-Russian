package sample;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 *
 * @author zinal
 */
public class DataGen implements AutoCloseable {

    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            try (FileReader fr = new FileReader("data-sample.properties")) {
                props.load(fr);
            }
            new DataGen(props) . run();
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(1);
        }
    }

    public static final String PROP_URL = "DbUrl";
    public static final String PROP_USER = "DbUser";
    public static final String PROP_PASS = "DbPassword";

    private Connection con;

    private PreparedStatement psCustomer = null;

    private DataGen(Properties props) throws Exception {
        this.con = DriverManager.getConnection(
                props.getProperty(PROP_URL),
                props.getProperty(PROP_USER),
                props.getProperty(PROP_PASS));
    }

    private void run() throws Exception {
       con.setAutoCommit(false);
       con.commit();
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
        return id;
    }

    private int makePhysical() throws Exception {
        int id = makeCustomer(true);
        return id;
    }

    @Override
    public void close() {
        if (psCustomer!=null) {
            try { psCustomer.close(); } catch(Exception ex) {}
            psCustomer = null;
        }
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

}
