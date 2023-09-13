import javax.swing.JOptionPane;

public class Useless {

    public static void main(String args[]) {
        int i = 1;
        while (i != 0) {
            i = JOptionPane.showConfirmDialog(null, "Will you marry me??", "marry me", JOptionPane.YES_NO_OPTION);

        }
    }

}
