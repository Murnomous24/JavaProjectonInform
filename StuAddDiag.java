import javax.swing.JDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
public class StuAddDiag extends JDialog implements ActionListener, KeyListener {
    JLabel jl1, jl2, jl3, jl4, jl5, jl6;
    JTextField jf1, jf2, jf3, jf4, jf5, jf6;
    JPanel jp1, jp2, jp3;
    JButton jb1, jb2;
    Font headline = new Font("微软雅黑",Font.PLAIN,25);
    Font textline = new Font("微软雅黑",Font.PLAIN,15);
    Font button = new Font("微软雅黑",Font.PLAIN,15);
    public JTextField[] textFields;
    Connection ct = null;
    PreparedStatement psSearch = null;
    ResultSet rs = null;

    //owner代笔父窗口,title是窗口的名字,modal指定是模式窗口()或者非模式窗口
    public StuAddDiag(Frame owner, String title, boolean modal) {
        super(owner, title, modal);

        jl1 = new JLabel("学号");  //设置对应文本框名
        jl2 = new JLabel("名字");
        jl3 = new JLabel("性别");
        jl4 = new JLabel("年龄");
        jl5 = new JLabel("专业");
        jl6 = new JLabel("院系");
        jl1.setFont(headline);
        jl2.setFont(headline);
        jl3.setFont(headline);
        jl4.setFont(headline);
        jl5.setFont(headline);
        jl6.setFont(headline);


        textFields = new JTextField[6]; //设置文本框
        for(int i=0; i<textFields.length; i++) {
            textFields[i] = new JTextField(15);
            textFields[i].setFont(textline);
            textFields[i].addKeyListener(this);
        }

        jb1 = new JButton("确认添加");
        jb1.addActionListener(this);
        jb2 = new JButton("取消");
        jb1.setFont(button);
        jb2.setFont(button);

        jp1 = new JPanel(); //分左中下三个面板
        jp2 = new JPanel();
        jp3 = new JPanel();

        //设置布局
        jp1.setLayout(new GridLayout(6, 1));
        jp2.setLayout(new GridLayout(6, 1));
        jp1.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        jp2.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        jp3.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        jp3.add(jb1);
        jp3.add(jb2);
        jp3.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        jp1.add(jl1);
        jp1.add(jl2);
        jp1.add(jl3);
        jp1.add(jl4);
        jp1.add(jl5);
        jp1.add(jl6);

        jp2.add(textFields[0]);
        jp2.add(textFields[1]);
        jp2.add(textFields[2]);
        jp2.add(textFields[3]);
        jp2.add(textFields[4]);
        jp2.add(textFields[5]);

        this.add(jp1, BorderLayout.WEST);
        this.add(jp2, BorderLayout.CENTER);
        this.add(jp3, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
        this.setSize(800,600);
        this.setVisible(true);
    }

    //@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb1) {

            try {
                ct = createDatabaseConnection();
                String strsql = "insert into stu(ID,`Name`,Sex,Age,JG,Dept) values(?,?,?,?,?,?)";  //添加新值
                psSearch = ct.prepareStatement(strsql);

                psSearch.setString(1, textFields[0].getText());
                psSearch.setString(2, textFields[1].getText());
                psSearch.setString(3, textFields[2].getText());
                psSearch.setString(4, textFields[3].getText());
                psSearch.setString(5, textFields[4].getText());
                psSearch.setString(6, textFields[5].getText());
                psSearch.executeUpdate();

                this.dispose();
            } catch (Exception arg1) {
                arg1.printStackTrace();
            } finally {
                FinallyAction();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
            //没用
    }

    @Override
    public void keyPressed(KeyEvent e) {  //键盘上下键操作 方便切换输入行
            if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                System.out.println("用户按下了方向键下键");
                Component currentComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                if(currentComponent instanceof JTextField) {
                    JTextField currentTextField = (JTextField) currentComponent;
                    int currentIndex = getCurrentIndex(currentTextField);

                    if (currentIndex < textFields.length - 1) {
                        JTextField nextTextField = textFields[currentIndex + 1];
                        nextTextField.requestFocus();
                    }
                }
            }
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("用户按下了方向键上键");
            Component currentComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if(currentComponent instanceof JTextField) {
                JTextField currentTextField = (JTextField) currentComponent;
                int currentIndex = getCurrentIndex(currentTextField);

                if (currentIndex > 0) {
                    JTextField nextTextField = textFields[currentIndex - 1];
                    nextTextField.requestFocus();
                }
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
            //没用
    }
    public int getCurrentIndex(JTextField textField) {  //输出现在所聚焦的文本框的序号
        for(int i=0; i<textFields.length; i++) {
            if(textFields[i]==textField) {
                return i;
            }
        }
        System.out.println("找不到对应文本框");
        return -1;
    }
    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException { //设置一个能抛出连接数据库，和数据库内异常的方法
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/stu?useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String key = "xxxxx";
        return DriverManager.getConnection(url,user,key);
    }
    public void FinallyAction() {  //关闭连接
        try{
            //为空的时候
            if(psSearch != null) {
                psSearch.close();
                psSearch = null;
            }
            if(rs != null) {
                rs.close();
                rs = null;
            }
            if(ct != null) {
                ct.close();
                ct = null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
