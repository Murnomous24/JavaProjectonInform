import javax.swing.JDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
public class StuUpDiag extends JDialog implements ActionListener{
    JLabel jl1, jl2, jl3, jl4, jl5, jl6;
    JTextField jf1, jf2, jf3, jf4, jf5, jf6;
    JPanel jp1, jp2, jp3;
    JButton jb1, jb2;
    Font headline = new Font("微软雅黑",Font.PLAIN,25);
    Font textline = new Font("微软雅黑",Font.PLAIN,15);
    Font button = new Font("微软雅黑",Font.PLAIN,15);
    Connection ct = null;
    PreparedStatement psSearch = null;
    public StuUpDiag(Frame owner,String title, boolean modal,StuModel sm,int rowNum) {
        super(owner,"修改学生信息",modal);

        jl1 = new JLabel("学号");
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

        jf1 = new JTextField(15); //初始化JTextfield并读取原有文本
        jf1.setText((sm.getValueAt(rowNum,0)).toString());
        jf2 = new JTextField(15);
        jf2.setText((sm.getValueAt(rowNum,1)).toString());
        jf3 = new JTextField(15);
        jf3.setText((sm.getValueAt(rowNum,2)).toString());
        jf4 = new JTextField(15);
        jf4.setText((sm.getValueAt(rowNum,3)).toString());
        jf5 = new JTextField(15);
        jf5.setText((sm.getValueAt(rowNum,4)).toString());
        jf6 = new JTextField(15);
        jf6.setText((sm.getValueAt(rowNum,5)).toString());
        jf1.setFont(textline);
        jf2.setFont(textline);
        jf3.setFont(textline);
        jf4.setFont(textline);
        jf5.setFont(textline);
        jf6.setFont(textline);



        jb1 = new JButton("确认修改");
        jb1.addActionListener(this);
        jb2 = new JButton("取消");
        jb1.setFont(button);
        jb2.setFont(button);

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        //设置布局
        jp1.setLayout(new GridLayout(6, 1));
        jp2.setLayout(new GridLayout(6, 1));
        jp1.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        jp2.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        jp3.add(jb1);
        jp3.add(jb2);

        jp1.add(jl1);
        jp1.add(jl2);
        jp1.add(jl3);
        jp1.add(jl4);
        jp1.add(jl5);
        jp1.add(jl6);

        jp2.add(jf1);
        jp2.add(jf2);
        jp2.add(jf3);
        jp2.add(jf4);
        jp2.add(jf5);
        jp2.add(jf6);

        this.add(jp1, BorderLayout.WEST);
        this.add(jp2, BorderLayout.CENTER);
        this.add(jp3, BorderLayout.SOUTH);

        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException { //设置一个能抛出连接数据库，和数据库内异常的方法
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/stu?useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String key = "xxxxx";
        return DriverManager.getConnection(url,user,key);
    }

    //@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb1) {

            try {

                ct = createDatabaseConnection();
                String query = "update stu set Name=?, Sex=?, Age=?, JG=?, Dept=? where ID=?";
                psSearch = ct.prepareStatement(query);

                psSearch.setString(1, jf2.getText()); // 名字
                psSearch.setString(2, jf3.getText()); // 性别
                psSearch.setString(3, jf4.getText()); // 年龄
                psSearch.setString(4, jf5.getText()); // 专业
                psSearch.setString(5, jf6.getText()); // 院系
                psSearch.setString(6, jf1.getText()); // 学号
                psSearch.executeUpdate();

                int RowAffected = psSearch.executeUpdate(); //查询影响行数 判断是否执行成功
                System.out.println("更新成功，并且受影响的行数为： " + RowAffected);

                psSearch.close();
                ct.close();
                this.dispose();
            } catch (Exception arg1) {
                arg1.printStackTrace();
            }
        }
    }
}

