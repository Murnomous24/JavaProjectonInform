import java.awt.*;
import java.io.Serializable;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.*;
public class StuModel extends AbstractTableModel{
    Vector<Vector<java.io.Serializable>> rowData;
    Vector <String> columnNames = new Vector<>();
    Statement statu = null;
    Connection ct = null;
    ResultSet rs = null;
    Font textfont = new Font("微软雅黑",Font.BOLD,20);

    public void init(String sql) {
        if (sql.equals("")) {
            sql = "select * from stu";
        }

        columnNames = new Vector<>();
        columnNames.add("学号");
        columnNames.add("名字");
        columnNames.add("性别");
        columnNames.add("年龄");
        columnNames.add("专业");
        columnNames.add("院系");
        rowData = new Vector<>();


        try {
            ct = createDatabaseConnection();
            statu = ct.createStatement();
            rs = statu.executeQuery(sql);

            while(rs.next()) {
                Vector hang = new Vector<>();
                hang.add(rs.getString(1));
                hang.add(rs.getString(2));
                hang.add(rs.getString(3));
                hang.add(rs.getInt(4));
                hang.add(rs.getString(5));
                hang.add(rs.getString(6));

                rowData.add(hang); //建一个与数据表同类型的一行 并加入修改后的数据
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            FinallyAction();
        }
    }
    public void addStu(String sql) {

    }
    public StuModel(String sql) {
        this.init(sql);
    }
    public StuModel() {
        this.init("");
    }
    public int getRowCount() {
        return this.rowData.size();
    }
    public int getColumnCount() {
        return this.columnNames.size();
    }
    public Object getValueAt(int row,int column) {
        return ((this.rowData.get(row))).get(column);
    }
    public String getColumnName(int column) {
        return this.columnNames.get(column);
    }
    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException { //设置一个能抛出连接数据库，和数据库内异常的方法
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/stu?useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String key = "xxxxx";
        return DriverManager.getConnection(url,user,key);
    }
    public void FinallyAction() {
        try{
            //为空的时候
            if(rs != null) {
                rs.close();
                rs = null;
            }
            if(ct != null) {
                ct.close();
                ct = null;
            }
            if(statu != null) {
                statu.close();
                statu = null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

