package javaapplication5;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class SignupScreen extends JFrame {

    // ===== THEME =====
    private static final Color BG   = new Color(18,18,18);
    private static final Color DARK = new Color(25,25,25);
    private static final Color GOLD = new Color(212,175,55);
    private static final Color TXT  = new Color(235,235,235);

    private JTextField username,email,fullname,phone;
    private JPasswordField password;
    private JComboBox<String> roleBox;

    public SignupScreen() {
        setNimbus();
        setTitle("Create New Account");
        setSize(450,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        add(main());
        setVisible(true);
    }

    // ================= UI =================
    private JPanel main() {
        JPanel p=new JPanel(new GridBagLayout());
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(20,30,20,30));

        GridBagConstraints g=new GridBagConstraints();
        g.fill=GridBagConstraints.HORIZONTAL;
        g.insets=new Insets(10,10,10,10);

        g.gridx=0; g.gridy=0; g.gridwidth=2;
        p.add(title("CREATE NEW ACCOUNT"),g);

        g.gridwidth=1;
        p.add(lbl("Username"),pos(g,1,0)); p.add(username=field(),pos(g,1,1));
        p.add(lbl("Email"),pos(g,2,0));    p.add(email=field(),pos(g,2,1));
        p.add(lbl("Password"),pos(g,3,0)); p.add(password=pfield(),pos(g,3,1));
        p.add(lbl("Full Name"),pos(g,4,0));p.add(fullname=field(),pos(g,4,1));
        p.add(lbl("Phone"),pos(g,5,0));    p.add(phone=field(),pos(g,5,1));

        p.add(lbl("Role"),pos(g,6,0));
        roleBox=new JComboBox<>(new String[]{"DOCTOR","RECEPTION","PHARMACY"});
        style(roleBox); p.add(roleBox,pos(g,6,1));

        g.gridx=0; g.gridy=7; g.gridwidth=2; g.insets=new Insets(20,10,10,10);
        p.add(buttons(),g);

        return p;
    }

    private JPanel buttons() {
        JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
        p.setBackground(BG);

        JButton signup=btn("Sign Up",true);
        JButton back=btn("Back",false);

        signup.addActionListener(e->signup());
        back.addActionListener(e->{dispose(); new WelcomeScreen();});

        p.add(signup); p.add(back);
        return p;
    }

    // ================= LOGIC =================
    private void signup() {
        if(username.getText().isEmpty()||email.getText().isEmpty()
           ||password.getPassword().length==0||fullname.getText().isEmpty()){
            msg("Fill all required fields!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User u=new User(
                fullname.getText(),
                email.getText(),
                new String(password.getPassword()),
                phone.getText(),
                roleBox.getSelectedItem().toString()
        );

        if(ClinicData.getInstance().addUser(u)){
            msg("Account created successfully!", JOptionPane.INFORMATION_MESSAGE);
            dispose(); new LoginScreen();
        }else msg("Email already exists!", JOptionPane.ERROR_MESSAGE);
    }

    // ================= HELPERS =================
    private JLabel title(String t){
        JLabel l=new JLabel(t,JLabel.CENTER);
        l.setFont(new Font("Segoe UI",Font.BOLD,24));
        l.setForeground(GOLD); return l;
    }

    private JLabel lbl(String t){
        JLabel l=new JLabel(t);
        l.setForeground(TXT);
        l.setFont(new Font("Segoe UI",Font.PLAIN,14));
        return l;
    }

    private JTextField field(){ JTextField f=new JTextField(15); style(f); return f; }
    private JPasswordField pfield(){ JPasswordField f=new JPasswordField(15); style(f); return f; }

    private void style(JComponent c){
        c.setFont(new Font("Segoe UI",Font.PLAIN,14));
        c.setBackground(DARK);
        c.setForeground(TXT);
        c.setBorder(BorderFactory.createLineBorder(GOLD,1));
    }

    private JButton btn(String t,boolean filled){
        JButton b=new JButton(t);
        b.setFont(new Font("Segoe UI",Font.BOLD,14));
        b.setFocusPainted(false);
        b.setBackground(filled?GOLD:DARK);
        b.setForeground(filled?Color.BLACK:GOLD);
        b.setBorder(new EmptyBorder(10,25,10,25));
        return b;
    }

    private GridBagConstraints pos(GridBagConstraints g,int y,int x){
        GridBagConstraints c=(GridBagConstraints)g.clone();
        c.gridy=y; c.gridx=x; return c;
    }

    private void msg(String m,int t){
        JOptionPane.showMessageDialog(this,m,"Message",t);
    }

    private void setNimbus(){
        try{
            for(UIManager.LookAndFeelInfo i:UIManager.getInstalledLookAndFeels())
                if("Nimbus".equals(i.getName()))
                    UIManager.setLookAndFeel(i.getClassName());
        }catch(Exception ignored){}
    }
}

