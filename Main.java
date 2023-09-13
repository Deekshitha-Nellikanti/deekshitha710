import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
// import java.swing.*;

// import com.mysql.cj.xdevapi.Statement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.mysql.cj.xdevapi.JsonArray;

public class Main {

    public static final String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/Survey1"; // Corrected URL format
    public static final String dbUser = "root";
    public static final String dbPassword = "Deeks@123";
    public static Connection connection;
    public static int i = 1;
    public static int j = 0;
    public static int uid;
    public static HashMap<Integer, ArrayList<String>> radioMapper = new HashMap<>();
    public static HashMap<Integer, String> questionMapper = new HashMap<>();
    public static HashMap<Integer, JComboBox> comboBoxMapper = new HashMap<>();
    public static int questionCount;

    public static JSONArray jsonarray = new JSONArray();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
        } catch (Exception e) {

        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Survey System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("SURVEY SYSTEM");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2)); // Use GridLayout
        JButton adminButton = new JButton("Admin");
        JButton userButton = new JButton("User");
        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(panel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                createAdminFrame();
            }
        });
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
                createUserFrame();
            }
        });
    }

    static JFrame fr;

    public static void createAdminFrame() {

        fr = new JFrame("SIGN IN/LOG IN");
        fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fr.setSize(400, 300);
        fr.setLayout(new GridLayout(7, 1));

        JLabel label1 = new JLabel("Username");
        JLabel label2 = new JLabel("Password");

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JCheckBox checkBox1 = new JCheckBox("new");

        JButton button3 = new JButton("Submit");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int flag1 = 0;
                if (checkBox1.isSelected()) {
                    flag1 = 1;
                }

                String username = userField.getText();
                char[] passwordChars = passField.getPassword();
                String password = new String(passwordChars);

                // Store username and password in the database
                if (username.equals("") || password.equals("")) {
                    JOptionPane.showMessageDialog(fr, "Please enter both the credentials",
                            "WARNING!!", JOptionPane.WARNING_MESSAGE);
                } else {
                    storeCredentials(username, password, flag1);
                }
            }
        });

        fr.add(label1);
        fr.add(userField);
        fr.add(label2);
        fr.add(passField);
        fr.add(new JLabel());
        fr.add(button3);
        fr.add(checkBox1);

        fr.setVisible(true);
        fr.setLocationRelativeTo(null);
    }

    public static void createUserFrame() {
        JFrame Userframe = new JFrame("User Page");
        // Userframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Userframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Userframe.setSize(400, 300);
        JPanel panel = new JPanel();
        JLabel efid = new JLabel("Enter form id");
        JTextField tffid = new JTextField("");
        JButton subfid = new JButton("Submit");
        panel.add(efid);
        panel.add(tffid);
        panel.add(subfid);
        panel.setLayout(new GridLayout(3, 1));
        Userframe.add(panel);
        Userframe.setVisible(true);
        Userframe.setLocationRelativeTo(null);

        subfid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String fid = tffid.getText();
                if (fid != "") {
                    int ofid = Integer.parseInt(fid);
                    String str = "SELECT * FROM survey_data WHERE fid = " + ofid;
                    try {
                        Statement stm = connection.createStatement();
                        ResultSet rst = ((java.sql.Statement) stm).executeQuery(str);
                        if (!rst.next()) {
                            JOptionPane.showMessageDialog(Userframe,
                                    "No data found for this Form ID,Please enter valid Form Id", "ERROR!",
                                    JOptionPane.ERROR_MESSAGE);

                        } else {
                            JFrame surveyFormFrame = new JFrame("User Page");
                            surveyFormFrame.setSize(400, 300);
                            JPanel p1 = new JPanel();
                            JButton submit = new JButton("Submit");

                            questionCount = 1;
                            do {
                                p1.setLayout(new GridLayout(questionCount * 11 + 1, 1));
                                String question = rst.getString("question");
                                String option1 = rst.getString("option1");
                                String option2 = rst.getString("option2");
                                String option3 = rst.getString("option3");
                                String option4 = rst.getString("option4");
                                System.out.println(
                                        " " + question + "1." + option1 + "2." + option2 + "3." + option3 + "4."
                                                + option4);

                                JLabel l1 = new JLabel("Question " + questionCount);
                                JLabel l2 = new JLabel(question);
                                p1.add(l1);
                                p1.add(l2);

                                JTextArea textArea1 = new JTextArea();
                                textArea1.setText(option1);
                                textArea1.setEditable(false);

                                JTextArea textArea2 = new JTextArea();
                                textArea2.setText(option2);
                                textArea2.setEditable(false);

                                JTextArea textArea3 = new JTextArea();
                                textArea3.setText(option3);
                                textArea3.setEditable(false);

                                JTextArea textArea4 = new JTextArea();
                                textArea4.setText(option4);
                                textArea4.setEditable(false);

                                p1.add(new JLabel("A"));
                                p1.add(textArea1);

                                p1.add(new JLabel("B"));
                                p1.add(textArea2);

                                p1.add(new JLabel("C"));
                                p1.add(textArea3);

                                p1.add(new JLabel("D"));
                                p1.add(textArea4);

                                String[] gg = { "A", "B", "C", "D" };
                                JComboBox comboBox = new JComboBox<>(gg);
                                p1.add(comboBox);

                                JScrollPane sp = new JScrollPane(p1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                surveyFormFrame.add(sp);
                                ArrayList<String> jrb = new ArrayList<>();
                                jrb.add(textArea1.getText());
                                jrb.add(textArea2.getText());
                                jrb.add(textArea3.getText());
                                jrb.add(textArea4.getText());

                                radioMapper.put(questionCount, jrb);
                                questionMapper.put(questionCount, question);
                                comboBoxMapper.put(questionCount, comboBox);

                                questionCount++;

                            } while (rst.next());

                            JScrollPane sp = new JScrollPane(p1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                            surveyFormFrame.add(sp);

                            surveyFormFrame.setVisible(true);
                            surveyFormFrame.setLocationRelativeTo(null);
                            // System.out.println(radioMapper);
                            // System.out.println(questionMapper);
                            System.out.println(comboBoxMapper);

                            submit.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    for (int i = 1; i < questionCount; i++) {
                                        JComboBox opres = comboBoxMapper.get(i);

                                        String res = (String) opres.getSelectedItem();
                                        ArrayList<String> opt = new ArrayList<>();
                                        opt = radioMapper.get(i);
                                        String que = questionMapper.get(i);
                                        System.out.println(que);
                                        System.out.println(opt);
                                        String str1 = "INSERT INTO user_res (fid,qid,question,option1,option2,option3,option4,result) VALUES(?,?,?,?,?,?,?,?)";
                                        try (PreparedStatement preparedStatement = connection.prepareStatement(str1)) {
                                            preparedStatement.setInt(1, Integer.parseInt(fid));
                                            preparedStatement.setInt(2, i);
                                            preparedStatement.setString(3, que);
                                            int j = 4;
                                            for (String st : opt) {

                                                preparedStatement.setString(j, st);
                                                System.out.println(st);
                                                j++;
                                            }
                                            System.out.println(res);
                                            preparedStatement.setString(j, res);
                                            System.out.println(res);
                                            preparedStatement.executeUpdate();

                                        } catch (Exception ee) {
                                            System.out.println(ee);

                                        }
                                        JOptionPane.showMessageDialog(Userframe,
                                                "You have Successfully Submitted The Form!", "Submitted!",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        surveyFormFrame.dispose();
                                        Userframe.dispose();

                                    }
                                }

                            });
                            p1.add(submit);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public static void storeCredentials(String username, String password, int flag1) {
        if (flag1 == 1) {
            String str = "SELECT * FROM admin WHERE username = '" + username + "'";
            try {

                Statement stm = connection.createStatement();
                ResultSet rst = ((java.sql.Statement) stm).executeQuery(str);
                if (!rst.next()) {

                    String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);
                        preparedStatement.executeUpdate();
                        String idInput = JOptionPane.showInputDialog(null, "Enter a user ID:");
                        uid = Integer.parseInt(idInput);
                        String str1 = "SELECT * FROM admin WHERE uid = '" + uid + "'";
                        try {

                            Statement stm1 = connection.createStatement();
                            ResultSet rst1 = ((java.sql.Statement) stm1).executeQuery(str1);
                            if (!rst1.next()) {
                                JOptionPane.showMessageDialog(fr, "Welcome", "user added!",
                                        JOptionPane.INFORMATION_MESSAGE);
                                String sql1 = "UPDATE admin SET uid=" + uid + " WHERE username='" + username + "'";
                                try (PreparedStatement preparedStatement1 = connection.prepareStatement(sql1)) {
                                    preparedStatement1.executeUpdate();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("User credentials stored in the database.");

                            }

                            else {
                                JOptionPane.showMessageDialog(fr, " User id already exists. Please enter another one!",
                                        "WARNING!!", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username already exists");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            String str = "SELECT * FROM admin WHERE username = '" + username + "'";
            try {

                Statement stm = connection.createStatement();
                ResultSet rst = ((java.sql.Statement) stm).executeQuery(str);
                if (!rst.next()) {
                    JOptionPane.showMessageDialog(fr, "No User Found", "WARNING!!", JOptionPane.WARNING_MESSAGE);
                }

                else {
                    if (rst.getString("password").equals(password)) {
                        JOptionPane.showMessageDialog(fr, "Login Successful!", "SUCCESSFUL!",
                                JOptionPane.INFORMATION_MESSAGE);

                        fr.dispose();

                        JFrame Mainadmin = new JFrame("Admin - MainPage ");
                        // Mainadmin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        // Mainadmin.setSize(400, 300);
                        // close this window after clicking on login button
                        Mainadmin.setLayout(new GridLayout(4, 1));
                        JButton creation = new JButton("Create  Form");
                        JButton viewform = new JButton("View  Form");
                        JButton vresult = new JButton("View Result");
                        JButton exit = new JButton("Exit");
                        Mainadmin.add(creation);
                        Mainadmin.add(viewform);
                        Mainadmin.add(vresult);
                        Mainadmin.add(exit);
                        Mainadmin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        Mainadmin.setVisible(true);// main page of admin
                        Mainadmin.setSize(400, 300);// size of mainpage
                        Mainadmin.setLocationRelativeTo(null);

                        creation.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame formpage = new JFrame("Frame Creation");
                                formpage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                formpage.setSize(400, 300);
                                // formpage.setVisible(true);
                                JLabel quest = new JLabel("Question ");
                                JTextField questf = new JTextField();
                                JLabel opt1 = new JLabel(" Option 1 ");
                                JTextField opt1f = new JTextField();
                                JLabel opt2 = new JLabel("Option  2 ");
                                JTextField opt2f = new JTextField();
                                JLabel opt3 = new JLabel("Option   3");
                                JTextField opt3f = new JTextField();
                                JLabel opt4 = new JLabel("Option   4");
                                JTextField opt4f = new JTextField();
                                JButton add = new JButton(" Add Question ");
                                JButton createform = new JButton(" Create form ");
                                formpage.add(quest);
                                formpage.add(questf);
                                formpage.add(opt1);
                                formpage.add(opt1f);
                                formpage.add(opt2);
                                formpage.add(opt2f);
                                formpage.add(opt3);
                                formpage.add(opt3f);
                                formpage.add(opt4);
                                formpage.add(opt4f);
                                formpage.add(createform);
                                formpage.add(add);
                                formpage.setLocationRelativeTo(null);
                                formpage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                formpage.setVisible(true);
                                formpage.setLayout(new GridLayout(12, 1));
                                add.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent ae) {
                                        String q1 = questf.getText();
                                        String op1 = opt1f.getText();
                                        String op2 = opt2f.getText();
                                        String op3 = opt3f.getText();
                                        String op4 = opt4f.getText();
                                        String st = "Question " + i + ".";
                                        if (q1 == null || op1 == null || op2 == null || op3 == null || op4 == null) {
                                            JOptionPane.showMessageDialog(formpage, "Please fill all the fields",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            JSONObject jsonobj = new JSONObject();
                                            jsonobj.put("question", q1);
                                            jsonobj.put("option-a", op1);
                                            jsonobj.put("option-b", op2);
                                            jsonobj.put("option-c", op3);
                                            jsonobj.put("option-d", op4);
                                            jsonarray.put(jsonobj);
                                            questf.setText("");
                                            opt1f.setText("");
                                            opt2f.setText("");
                                            opt3f.setText("");
                                            opt4f.setText("");

                                            // System.out.println(jsonobj);

                                        }
                                    }
                                });
                                createform.addActionListener(new ActionListener() {
                                    // @Override
                                    public void actionPerformed(ActionEvent ae) {
                                        j += 1;
                                        String idInput2 = JOptionPane.showInputDialog(null, "Enter a Form ID:");
                                        int fid = Integer.parseInt(idInput2);
                                        String str2 = "SELECT * FROM survey_data  WHERE fid = '" + fid + "'";
                                        try {
                                            Statement stm4 = connection.createStatement();
                                            ResultSet rst4 = ((java.sql.Statement) stm4).executeQuery(str2);
                                            if (!rst4.next()) {

                                                for (int i = 0; i < jsonarray.length(); i++) {
                                                    JSONObject jo = jsonarray.getJSONObject(i);
                                                    String question = jo.getString("question");
                                                    String option1 = jo.getString("option-a");
                                                    String option2 = jo.getString("option-b");
                                                    String option3 = jo.getString("option-c");
                                                    String option4 = jo.getString("option-d");

                                                    String sql4 = "INSERT INTO survey_data (username,fid,question,option1,option2,option3,option4) VALUES (?,?,?,?,?,?,?)";
                                                    try (PreparedStatement preparedStatement = connection
                                                            .prepareStatement(sql4)) {
                                                        preparedStatement.setString(1, username);
                                                        preparedStatement.setInt(2, fid);
                                                        preparedStatement.setString(3, question);
                                                        preparedStatement.setString(4, option1);
                                                        preparedStatement.setString(5, option2);
                                                        preparedStatement.setString(6, option3);
                                                        preparedStatement.setString(7, option4);
                                                        preparedStatement.executeUpdate();
                                                    }

                                                    catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                System.out.println("Data inserted into the database.");

                                            } else {
                                                JOptionPane.showMessageDialog(null, "Already Exists", "Error",
                                                        JOptionPane.ERROR_MESSAGE);
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        formpage.dispose();
                                    }

                                });
                            }
                        });
                        viewform.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                formview(username);

                            }
                        });
                        vresult.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                resultview(username);

                            }
                        });

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static void formview(String username) {
        JFrame v1 = new JFrame("form");
        JLabel k = new JLabel("Enter Form id");
        JTextField tffid = new JTextField();
        v1.add(k);
        v1.add(tffid);
        String fid = tffid.getText();
        v1.setVisible(true);
        v1.setSize(400, 300);
        v1.setLayout(new GridLayout(3, 1));
        v1.setLocationRelativeTo(null);
        JButton s = new JButton("submit");
        v1.add(s);
        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fid = tffid.getText();
                    if (fid != "") {
                        int ofid = Integer.parseInt(fid);
                        String str = "SELECT * FROM survey_data WHERE fid = " + ofid + " AND username='" + username
                                + "'";

                        Statement stm = connection.createStatement();
                        ResultSet rst = ((java.sql.Statement) stm).executeQuery(str);

                        if (!rst.next()) {
                            JOptionPane.showMessageDialog(v1,
                                    "No data found for this Form ID,Please enter valid Form Id", "ERROR!",
                                    JOptionPane.ERROR_MESSAGE);

                        } else {

                            JFrame view = new JFrame("Viewing Form");
                            // view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            view.setSize(400, 300);
                            JPanel p1 = new JPanel();

                            view.setLocationRelativeTo(null);

                            questionCount = 1;
                            do {
                                p1.setLayout(new GridLayout(questionCount * 11 + 1, 1));
                                String question = rst.getString("question");
                                String option1 = rst.getString("option1");
                                String option2 = rst.getString("option2");
                                String option3 = rst.getString("option3");
                                String option4 = rst.getString("option4");
                                System.out.println(
                                        " " + question + "1." + option1 + "2." + option2 + "3." + option3 + "4."
                                                + option4);

                                JLabel l1 = new JLabel("Question " + questionCount);
                                JLabel l2 = new JLabel(question);
                                p1.add(l1);
                                p1.add(l2);

                                JTextArea textArea1 = new JTextArea();
                                textArea1.setText(option1);
                                textArea1.setEditable(false);

                                JTextArea textArea2 = new JTextArea();
                                textArea2.setText(option2);
                                textArea2.setEditable(false);

                                JTextArea textArea3 = new JTextArea();
                                textArea3.setText(option3);
                                textArea3.setEditable(false);

                                JTextArea textArea4 = new JTextArea();
                                textArea4.setText(option4);
                                textArea4.setEditable(false);

                                p1.add(new JLabel("A"));
                                p1.add(textArea1);

                                p1.add(new JLabel("B"));
                                p1.add(textArea2);

                                p1.add(new JLabel("C"));
                                p1.add(textArea3);

                                p1.add(new JLabel("D"));
                                p1.add(textArea4);

                                String[] gg = { "A", "B", "C", "D" };
                                JComboBox comboBox = new JComboBox<>(gg);
                                p1.add(comboBox);

                                JScrollPane sp = new JScrollPane(p1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                view.add(sp);

                                questionCount++;

                            } while (rst.next());
                            view.setVisible(true);
                            // view.setLayout(new GridLayout(questionCount - 1, 1));

                        }
                    }

                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        });
    }

    public static void resultview(String username) {
        JFrame r1 = new JFrame("form");
        JLabel k = new JLabel("Enter Form id");
        JTextField tffid = new JTextField();
        r1.add(k);
        r1.add(tffid);
        String fid = tffid.getText();
        // int ofid = Integer.parseInt(fid);
        r1.setVisible(true);
        r1.setSize(400, 300);
        r1.setLayout(new GridLayout(3, 1));
        r1.setLocationRelativeTo(null);
        JButton s = new JButton("submit");
        r1.add(s);
        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fid = tffid.getText();
                    if (fid != "") {
                        int ofid = Integer.parseInt(fid);
                        String str = "SELECT * FROM survey_data WHERE fid = " + ofid + "AND username=" + username;

                        Statement stm = connection.createStatement();
                        ResultSet rst = ((java.sql.Statement) stm).executeQuery(str);

                        if (!rst.next()) {
                            JOptionPane.showMessageDialog(r1,
                                    "No data found for this Form ID,Please enter valid Form Id", "ERROR!",
                                    JOptionPane.ERROR_MESSAGE);

                        } else {
                            String str1 = "SELECT result FROM user_res WHERE fid = " + ofid;
                            Statement stm1 = connection.createStatement();
                            ResultSet rst1 = ((java.sql.Statement) stm1).executeQuery(str);

                            if (!rst1.next()) {
                                JOptionPane.showMessageDialog(r1,
                                        "No Results Yet", "ERROR!",
                                        JOptionPane.ERROR_MESSAGE);

                            } else {

                                JFrame res1 = new JFrame("Result");
                                // view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                res1.setSize(400, 300);
                                JPanel p1 = new JPanel();

                                questionCount = 1;
                                do {
                                    p1.setLayout(new GridLayout(questionCount * 11 + 1, 1));
                                    String question = rst.getString("question");
                                    String option1 = rst.getString("option1");
                                    String option2 = rst.getString("option2");
                                    String option3 = rst.getString("option3");
                                    String option4 = rst.getString("option4");
                                    System.out.println(
                                            " " + question + "1." + option1 + "2." + option2 + "3." + option3 + "4."
                                                    + option4);

                                    JLabel l1 = new JLabel("Question " + questionCount);
                                    JLabel l2 = new JLabel(question);
                                    p1.add(l1);
                                    p1.add(l2);

                                    JTextArea textArea1 = new JTextArea();
                                    textArea1.setText(option1);
                                    textArea1.setEditable(false);

                                    JTextArea textArea2 = new JTextArea();
                                    textArea2.setText(option2);
                                    textArea2.setEditable(false);

                                    JTextArea textArea3 = new JTextArea();
                                    textArea3.setText(option3);
                                    textArea3.setEditable(false);

                                    JTextArea textArea4 = new JTextArea();
                                    textArea4.setText(option4);
                                    textArea4.setEditable(false);

                                    p1.add(new JLabel("A"));
                                    p1.add(textArea1);

                                    p1.add(new JLabel("B"));
                                    p1.add(textArea2);

                                    p1.add(new JLabel("C"));
                                    p1.add(textArea3);

                                    p1.add(new JLabel("D"));
                                    p1.add(textArea4);

                                    String str2 = "SELECT MAX(qid) FROM user_res WHERE fid = " + ofid;
                                    Statement stm2 = connection.createStatement();
                                    ResultSet rst2 = ((java.sql.Statement) stm2).executeQuery(str2);
                                    rst2.next();
                                    String q = rst2.getString(1);
                                    int qu = Integer.parseInt(q);

                                    String str3 = "SELECT COUNT(result) FROM user_res WHERE fid = ? AND qid = ? AND result = ?";
                                    int co1 = 0;
                                    try (PreparedStatement preparedStatement1 = connection.prepareStatement(str3)) {
                                        preparedStatement1.setInt(1, ofid); // Set the first parameter (fid)
                                        preparedStatement1.setInt(2, questionCount); // Set the second parameter (qid)
                                        preparedStatement1.setString(3, "A"); // Set the third parameter (result

                                        ResultSet resultSet1 = preparedStatement1.executeQuery();
                                        if (resultSet1.next()) {
                                            co1 = resultSet1.getInt(1);
                                            // Process the resultCount as needed
                                        }
                                    } catch (SQLException ee) {

                                        ee.printStackTrace();
                                    }
                                    String str4 = "SELECT COUNT(result) FROM user_res WHERE fid = ? AND qid = ? AND result = ?";
                                    int co2 = 0;
                                    try (PreparedStatement preparedStatement2 = connection.prepareStatement(str4)) {
                                        preparedStatement2.setInt(1, ofid); // Set the first parameter (fid)
                                        preparedStatement2.setInt(2, questionCount); // Set the second parameter (qid)
                                        preparedStatement2.setString(3, "B"); // Set the third parameter (result)

                                        ResultSet resultSet2 = preparedStatement2.executeQuery();
                                        if (resultSet2.next()) {
                                            co2 = resultSet2.getInt(1);
                                            // Process the resultCount as needed
                                        }
                                    } catch (SQLException ee) {
                                        ee.printStackTrace();
                                    }
                                    String str5 = "SELECT COUNT(result) FROM user_res WHERE fid = ? AND qid = ? AND result = ?";
                                    int co3 = 0;
                                    try (PreparedStatement preparedStatement3 = connection.prepareStatement(str5)) {
                                        preparedStatement3.setInt(1, ofid); // Set the first parameter (fid)
                                        preparedStatement3.setInt(2, questionCount); // Set the second parameter (qid)
                                        preparedStatement3.setString(3, "C"); // Set the third parameter (result)

                                        ResultSet resultSet3 = preparedStatement3.executeQuery();
                                        if (resultSet3.next()) {
                                            co3 = resultSet3.getInt(1);
                                            // Process the resultCount as needed
                                        }
                                    } catch (SQLException ee) {
                                        ee.printStackTrace();
                                    }

                                    String str6 = "SELECT COUNT(result) FROM user_res WHERE fid = ? AND qid = ? AND result = ?";
                                    int co4 = 0;
                                    try (PreparedStatement preparedStatement4 = connection.prepareStatement(str6)) {
                                        preparedStatement4.setInt(1, ofid); // Set the first parameter (fid)
                                        preparedStatement4.setInt(2, questionCount); // Set the second parameter (qid)
                                        preparedStatement4.setString(3, "D"); // Set the third parameter (result)

                                        ResultSet resultSet4 = preparedStatement4.executeQuery();
                                        if (resultSet4.next()) {
                                            co4 = resultSet4.getInt(1);
                                            // Process the resultCount as needed
                                        }
                                    } catch (SQLException ee) {
                                        ee.printStackTrace();
                                    }

                                    String[] gg = { "A-" + co1, "B-" + co2, "C-" + co3, "D-" + co4 };
                                    JComboBox comboBox = new JComboBox<>(gg);
                                    p1.add(comboBox);

                                    JScrollPane sp = new JScrollPane(p1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                    res1.add(sp);

                                    questionCount++;

                                } while (rst.next());
                                res1.setVisible(true);
                                res1.setLayout(new GridLayout(3, 1));
                                res1.setLocationRelativeTo(null);
                            }
                        }

                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        });

    }

}