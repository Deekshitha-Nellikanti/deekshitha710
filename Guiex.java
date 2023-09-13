import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.swing.BorderFactory;
import java.swing.border.Border;

public class Mainpage1 extends JFrame implements ActionListner {
    JButton button = new JButton("ADMIN");
    JLabel lable= new JLabel();

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==button){
            AdminPage admin =  new AdminPage ();



public class Mainpage {
    public static void main(String args[]) {
        JFrame jf = new JFrame();
        jf.setVisible(true);
        // jf.setSize(420, 420);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // JButton jb = new JButton("click here");
        // jf.add(jb);
        // Border border=BorderFactory.createLineBorder(Color.white);
        JLabel jl = new JLabel("ONLINE SURVEY SYSTEM");
        jl.setForeground(Color.black);
        jl.setBackground(Color.orange);
        jl.setOpaque(true);
        ImageIcon img = new ImageIcon("sur.jpg");
        jf.add(jl);
        jl.setIcon(img);
        // jl.setBorder(border);
        jl.setHorizontalTextPosition(JLabel.CENTER);
        jl.setVerticalTextPosition(JLabel.TOP);
         jl.setVerticalAlignment(JLabel.CENTER);
        jl.setHorizontalAlignment(JLabel.CENTER);
       // jl.setLayout(null);
        jl.setFont(new Font("MV Boli", Font.BOLD, 50));
        // jl.setBounds(100, 100, 10, 10);
        jf.pack();

    }
}
