import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;

public class Login extends JFrame implements ActionListener{

    JLabel Username, Password,Headline;
    JButton jb1,jb2,jb3;
    JTextField jtf;
    JPasswordField pwf;
    PictureView headimage;
    Connection ct;
    PreparedStatement psSearch;
    ResultSet rs;
    Font Fonthead = new Font("微软雅黑",Font.BOLD,50);  //设置字体
    Font Fontline = new Font("宋体",Font.BOLD,25);
    Font Fontbutton = new Font("宋体",Font.BOLD,20);
    String accountin;
    String keyin;

    int exitcode = 0;
    public Login() {
        this.setTitle("用户登陆界面");
        this.setSize(1200,900);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);

        Headline = new JLabel("学生管理系统");
        Username = new JLabel("用户名");
        Password = new JLabel("密码");
        jb1 = new JButton("登录");
        jb2 = new JButton("取消");
        jb3 = new JButton("注册");

        UIManager.put("OptionPane.messageFont",new Font("宋体",Font.BOLD,20));
        Headline.setFont(Fonthead);
        Username.setFont(Fontline);
        Password.setFont(Fontline);
        jb1.setFont(Fontbutton);
        jb2.setFont(Fontbutton);
        jb3.setFont(Fontbutton);
        Headline.setForeground(Color.RED);

        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        jtf = new JTextField(30);
        pwf = new JPasswordField(30);
        headimage = new PictureView(); //通过PictureView对象中的方法添加图片
        headimage.setImage("/res/loginview.png");

        Headline.setBounds(450,50,800,100);
        Username.setBounds(400,408,100,50);
        Password.setBounds(400,458,100,50);
        jtf.setBounds(500,420,200,30);
        pwf.setBounds(500,470,200,30);
        jb1.setBounds(430,520,80,30);
        jb2.setBounds(530,520,80,30);
        jb3.setBounds(630,520,80,30);
        headimage.setBounds(0,0,1200,300);

        add(Headline);
        add(Username);
        add(jtf);
        add(Password);
        add(pwf);
        add(jb1);
        add(jb2);
        add(jb3);
        add(headimage);

    }
    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException { //设置一个能抛出连接数据库，和数据库内异常的方法
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/account?useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String key = "xxxxx";
        return DriverManager.getConnection(url,user,key);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jb1) {

            try {
                ct = createDatabaseConnection();
                accountin = jtf.getText().trim();
                keyin = pwf.getText().trim();
                if (accountin == null) {
                    JOptionPane.showMessageDialog(null, "用户名为空！请输入账户和密码！");
                    return; //防止触发下面的密码不正确提示
                }

                String query = "SELECT 1 FROM accountkey WHERE account = ? AND `key` = ?"; // 存在返回1 不存在返回空 为if(rs.next())做准备
                psSearch = ct.prepareStatement(query);
                psSearch.setString(1,accountin);
                psSearch.setString(2,keyin);
                rs = psSearch.executeQuery();

                if(rs.next()) {
                        exitcode = 1;
                        JOptionPane.showMessageDialog(null, "登陆成功！");
                        dispose();
                        Main main = new Main("学生信息管理系统");
                        main.setVisible(true);
                        //SwingUtilities.invokeLater(() -> new Main("学生信息管理系统")); //Lambda表达式写法
                    } else {
                        JOptionPane.showMessageDialog(null, "用户不存在或密码错误！");
                    }
            } catch (SQLException | ClassNotFoundException ex) {  //返回数据库异常 和 加载类时缺失组件的异常
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource()==jb2) { //取消就退出系统
            System.exit(0);
        }
        if(e.getSource()==jb3) {
            //注册信息
            Register register = new Register(this);
        }
    }
}
