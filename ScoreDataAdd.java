import javax.swing.JDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
public class ScoreDataAdd extends JDialog implements ActionListener {
    JLabel jl1, jl2, jl3, jtips;
    JTextField IDTextField, NameTextField, ScoreTextField;
    JPanel jp1, jp2, jp3;
    JButton jb1, jb2, jb3;
    Font headline = new Font("微软雅黑", Font.PLAIN, 25);
    Font textline = new Font("微软雅黑", Font.PLAIN, 25);
    Font button = new Font("微软雅黑", Font.PLAIN, 20);
    String column;

    //owner代笔父窗口,title是窗口的名字,modal指定是模式窗口()或者非模式窗口
    public ScoreDataAdd(ScoreAdd owner, String column,String title ,boolean modal) {

        this.setTitle(title);
        this.column = column;
        this.setLayout(new BorderLayout());
        jl1 = new JLabel("学号");
        jl2 = new JLabel("姓名");
        jl3 = new JLabel(this.getTitle() + "最终成绩");
        jl1.setFont(headline);
        jl2.setFont(headline);
        jl3.setFont(headline);
        jtips = new JLabel("请先输入学号，并根据系统给出的姓名检查是否为所选的学生！");
        System.out.println(this.getTitle());
        System.out.println(this.column);

        IDTextField = new JTextField(15);
        NameTextField = new JTextField(15);
        ScoreTextField = new JTextField(15);
        IDTextField.setFont(textline);
        NameTextField.setFont(textline);
        ScoreTextField.setFont(textline);
        jtips.setFont(textline);

        jb1 = new JButton("确认添加");
        jb1.addActionListener(this);
        jb2 = new JButton("取消");
        jb3 = new JButton("测试");
        jb3.addActionListener(this);
        jb1.setFont(button);
        jb2.setFont(button);
        jb3.setFont(button);

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        //设置布局
        jp1.setLayout(new GridLayout(6, 1));
        jp2.setLayout(new GridLayout(6, 1));
        jp1.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        jp2.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        jp3.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        jp3.add(jtips);
        jp3.add(jb1);
        jp3.add(jb2);
        jp3.add(jb3);
        jp3.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        jp1.add(jl1);
        jp1.add(jl2);
        jp1.add(jl3);

        jp2.add(IDTextField);
        jp2.add(NameTextField);
        jp2.add(ScoreTextField);

        this.add(jp1, BorderLayout.WEST);
        this.add(jp2, BorderLayout.CENTER);
        this.add(jp3, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
        this.setSize(1200, 900);
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
        if (e.getSource() == jb1) { //通过ID更新成绩
            Connection ct = null;
            PreparedStatement psSearch = null;
            ResultSet rs = null;

            try {
                ct = createDatabaseConnection();
                String stuID = IDTextField.getText().trim();
                String query = "SELECT * FROM stu WHERE ID = ?";

                psSearch = ct.prepareStatement(query);
                psSearch.setString(1, stuID);
                rs = psSearch.executeQuery();
                String strsql = "update stu set "+ column +"=? where ID=?";
                psSearch = ct.prepareStatement(strsql);
                psSearch.setString(1, ScoreTextField.getText().trim());   // 设置更新值
                psSearch.setString(2, stuID);           // 设置需要对应的ID

                int rowsAffected = psSearch.executeUpdate(); //返回影响的行数
                if (rowsAffected > 0) {
                    System.out.println("数据更新成功！");
                } else {
                    System.out.println("未找到匹配的行，数据更新失败！");
                }
                this.dispose();
            } catch (Exception arg1) {
                arg1.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (psSearch != null) {
                        psSearch.close();
                    }
                    if (ct != null) {
                        ct.close();
                    }
                } catch (Exception arg2) {
                    arg2.printStackTrace();
                }
            }
        }

        if (e.getSource() == jb3) { //通过ID读取Name
            Connection ct;
            PreparedStatement psSearch;
            ResultSet rs;
            try {
                ct = createDatabaseConnection();
                String stuID = IDTextField.getText().trim();
                String query = "SELECT * FROM stu WHERE ID = ?";
                psSearch = ct.prepareStatement(query);
                psSearch.setString(1, stuID);
                rs = psSearch.executeQuery();
                System.out.println(rs);
                if (rs.next()) {
                    String name =  rs.getString("Name");
                    int oldsocre = rs.getInt(column);
                    System.out.println("Name" + name);
                    NameTextField.setText(name);
                    ScoreTextField.setText(String.valueOf(oldsocre));
                } else {
                    System.out.println("no find!");
                }

                rs.close();
                psSearch.close();
                ct.close();
            } catch (Exception arg1) {
                arg1.printStackTrace();
            }
        }
    }
}

