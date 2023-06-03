import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends JDialog implements ActionListener {
    public  JTextField usernameField;
    public JPasswordField passwordField;
    public JButton registerButton,cancelButton;
    public JLabel jl1,jl2;
    public JPanel jp1,jp2,jp3;
    Font Fonthead = new Font("微软雅黑",Font.BOLD,50);  //设置字体
    Font Fontline = new Font("宋体",Font.BOLD,25);
    Font Fontbutton = new Font("宋体",Font.BOLD,20);
    public Register(Frame owner) {
        super(owner, "注册", true);

        jp1 = new JPanel(new FlowLayout());
        jp2 = new JPanel(new FlowLayout());
        jp3 = new JPanel(new FlowLayout());
        // 初始化界面组件
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        usernameField.setFont(Fontline);
        passwordField.setFont(Fontline);

        registerButton = new JButton("注册");
        cancelButton = new JButton("取消");
        registerButton.setFont(Fontbutton);
        cancelButton.setFont(Fontbutton);

        jl1 = new JLabel("用户名");
        jl2= new JLabel("密码");
        jl1.setFont(Fontline);
        jl2.setFont(Fontline);

        JPanel panel = new JPanel(null);
        PictureView headimage = new PictureView();
        headimage.setImage("/res/regist.png");

        headimage.setBounds(0,0,800,180);
        jl1.setBounds(90,200,100,50);
        usernameField.setBounds(270,200,300,50);
        jl2.setBounds(90,300,100,50);
        passwordField.setBounds(270,300,300,50);
        registerButton.setBounds(250,400,80,30);
        cancelButton.setBounds(350,400,80,30);

        panel.add(headimage);
        panel.add(jl1);
        panel.add(jl2);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(cancelButton);

        this.add(panel);

        registerButton.addActionListener(this);
        cancelButton.addActionListener(this);
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
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
        if(e.getSource()==registerButton) {
            String accountin = usernameField.getText();
            String keyin = new String(passwordField.getPassword());

            try {
                // 连接数据库
                Connection connection = createDatabaseConnection();

                // 执行注册逻辑
                String sql = "INSERT INTO accountkey (account, `key`) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, accountin);
                statement.setString(2, keyin);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "注册成功");
                } else {
                    JOptionPane.showMessageDialog(this, "注册失败");
                }

                // 关闭
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "注册出错");
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource()==cancelButton) {
            System.exit(0);
        }
    }
}
