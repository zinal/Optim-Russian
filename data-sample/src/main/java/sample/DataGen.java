package sample;

/**
 *
 * @author zinal
 */
public class DataGen {

    public static void main(String[] args) {
        try {
            new DataGen() . run();
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(1);
        }
    }

    private void run() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
