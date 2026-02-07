
package javaapplication5;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PharmacyDashboard extends JFrame {

    private final User user;
    private final ClinicData data = ClinicData.getInstance();

    // ===== THEME =====
    private static final Color BLACK = new Color(18,18,18);
    private static final Color DARK  = new Color(25,25,25);
    private static final Color GOLD  = new Color(212,175,55);
    private static final Color WHITE = new Color(235,235,235);
    private static final DateTimeFormatter DT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public PharmacyDashboard(User user) {
        this.user = user;
        setTitle("Pharmacy Dashboard - " + user.getFullName());
        setSize(800,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        add(mainPanel());
        setVisible(true);
    }

    // ================= MAIN =================
    private JPanel mainPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BLACK);
        p.add(header(), BorderLayout.NORTH);
        p.add(menu(), BorderLayout.WEST);
        p.add(statusBar(), BorderLayout.SOUTH);
        p.add(center(), BorderLayout.CENTER);
        return p;
    }

    // ================= HEADER =================
    private JPanel header() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(DARK);
        h.setBorder(new EmptyBorder(10,20,10,20));

        h.add(lbl("💊 PHARMACY DASHBOARD", 20, GOLD), BorderLayout.WEST);
        h.add(lbl("Welcome, " + user.getFullName(), 14, WHITE), BorderLayout.EAST);
        return h;
    }

    // ================= MENU =================
    private JPanel menu() {
        JPanel m = new JPanel(new GridLayout(7,1,10,10));
        m.setBackground(DARK);
        m.setBorder(new EmptyBorder(20,20,20,20));
        m.setPreferredSize(new Dimension(210,0));

        m.add(menuBtn("? View Stock", this::viewStock));
        m.add(menuBtn(" Add Medicine", this::addMedicine));
        m.add(menuBtn("? Sell Medicine", this::sellMedicine));
        m.add(menuBtn("? Search Medicine", this::searchMedicine));
        m.add(menuBtn("️ Low Stock", this::lowStock));
        m.add(menuBtn("? Update Stock", this::updateStock));
        m.add(menuBtn("?Logout", this::logout));
        return m;
    }

    // ================= CENTER =================
    private JPanel center() {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(BLACK);
        c.setBorder(new EmptyBorder(20,20,20,20));
        c.add(lbl("<html><center><h2>Pharmacy Management</h2>"
                + "<p>Manage stock & medicine sales easily</p></center></html>",
                14, WHITE), BorderLayout.CENTER);
        return c;
    }

    // ================= STATUS =================
    private JPanel statusBar() {
        long low = data.getMedicines().stream().filter(m -> m.quantity < 20).count();
        return bar("Total Medicines: " + data.getMedicines().size()
                + " | Low Stock: " + low);
    }

    // ================= ACTIONS =================
    private void viewStock() {
        List<Medicine> meds = data.getMedicines();
        if (meds.isEmpty()) { msg("No medicines found"); return; }

        String[] c = {"Name","Qty","Price","Expiry"};
        Object[][] r = new Object[meds.size()][4];

        for(int i=0;i<meds.size();i++){
            Medicine m = meds.get(i);
            r[i]=new Object[]{m.name,m.quantity,"₹"+m.price,m.expiryDate};
        }
        JOptionPane.showMessageDialog(this,new JScrollPane(new JTable(r,c)),
                "Medicine Stock",JOptionPane.PLAIN_MESSAGE);
    }

    private void addMedicine() {
        JTextField n=new JTextField(), p=new JTextField(), e=new JTextField();
        JSpinner q=new JSpinner(new SpinnerNumberModel(1,1,1000,1));
        e.setText(LocalDateTime.now().plusMonths(6)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if(dialog(new Object[]{"Name",n,"Qty",q,"Price",p,"Expiry",e},"Add Medicine")){
            try{
                data.addMedicine(new Medicine(n.getText(),
                        (int)q.getValue(),Double.parseDouble(p.getText()),e.getText()));
                data.saveAllData();
                msg("Medicine Added Successfully");
            }catch(Exception ex){ msg("Invalid Data"); }
        }
    }

    private void sellMedicine() {
        Medicine m = pickMedicine();
        if(m==null) return;

        String s = JOptionPane.showInputDialog(this,"Quantity to sell:");
        if(s==null) return;

        int q=Integer.parseInt(s);
        if(q<=0||q>m.quantity){ msg("Invalid Quantity"); return; }

        m.quantity-=q;
        data.saveAllData();

        double total=q*m.price, gst=total*0.18;
        msg("<html><b>Bill</b><br>Medicine: "+m.name+
                "<br>Qty: "+q+
                "<br>Total: ₹"+(total+gst)+
                "<br>Date: "+DT.format(LocalDateTime.now())+"</html>");
    }

    private void searchMedicine() {
        String t=JOptionPane.showInputDialog(this,"Medicine name:");
        if(t==null) return;

        StringBuilder sb=new StringBuilder("<html>");
        data.getMedicines().stream()
                .filter(m->m.name.toLowerCase().contains(t.toLowerCase()))
                .forEach(m->sb.append(m.name)
                        .append(" | Qty: ").append(m.quantity)
                        .append(" | ₹").append(m.price).append("<br>"));
        msg(sb.length()>6?sb+"</html>":"Not Found");
    }

    private void lowStock() {
        StringBuilder sb=new StringBuilder("<html>");
        data.getMedicines().stream()
                .filter(m->m.quantity<20)
                .forEach(m->sb.append(m.name)
                        .append(" → ").append(m.quantity).append("<br>"));
        msg(sb.length()>6?sb+"</html>":"All stock OK");
    }

    private void updateStock() {
        Medicine m = pickMedicine();
        if(m==null) return;

        String s=JOptionPane.showInputDialog(this,"New Quantity:");
        if(s==null) return;

        m.quantity=Integer.parseInt(s);
        data.saveAllData();
        msg("Stock Updated");
    }

    private void logout() {
        if(JOptionPane.showConfirmDialog(this,"Logout?","Confirm",
                JOptionPane.YES_NO_OPTION)==0){
            dispose();
            new WelcomeScreen();
        }
    }

    // ================= HELPERS =================
    private JButton menuBtn(String t,Runnable r){
        JButton b=new JButton(t);
        b.setFont(new Font("Segoe UI",Font.BOLD,14));
        b.setForeground(GOLD);
        b.setBackground(DARK);
        b.setBorder(BorderFactory.createLineBorder(GOLD));
        b.setFocusPainted(false);
        b.addActionListener(e->r.run());
        return b;
    }

    private JLabel lbl(String t,int s,Color c){
        JLabel l=new JLabel(t);
        l.setFont(new Font("Segoe UI",Font.BOLD,s));
        l.setForeground(c);
        return l;
    }

    private JPanel bar(String t){
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(DARK);
        p.add(lbl(t,12,WHITE));
        return p;
    }

    private boolean dialog(Object[] o,String t){
        return JOptionPane.showConfirmDialog(this,o,t,
                JOptionPane.OK_CANCEL_OPTION)==0;
    }

    private void msg(String m){
        JOptionPane.showMessageDialog(this,m);
    }

    private Medicine pickMedicine(){
        List<Medicine> m=data.getMedicines();
        if(m.isEmpty()){ msg("No medicines"); return null; }
        String[] o=m.stream().map(x->x.name).toArray(String[]::new);
        String s=(String)JOptionPane.showInputDialog(this,
                "Select Medicine","Medicine",
                JOptionPane.PLAIN_MESSAGE,null,o,o[0]);
        return m.stream().filter(x->x.name.equals(s)).findFirst().orElse(null);
    }
}

