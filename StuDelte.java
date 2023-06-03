import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class StuDelte extends AbstractTableModel {
        Statement statu = null;
        Connection ct = null;
        PreparedStatement psSearch =null;
        String stuID;
    public StuDelte(String ID) {
            System.out.println("ID： " + ID);
            this.stuID=ID;
        }
    public void delteinform () {
            try {

                ct = createDatabaseConnection();
                System.out.println("或许连接上Mysql数据库了...");
                psSearch = ct.prepareStatement("DELETE FROM stu WHERE ID = ?"); //根据ID删除信息
                psSearch.setString(1,stuID);
                psSearch.executeUpdate();

                int rowsAffected = psSearch.executeUpdate(); //返回影响的行数
                System.out.println("更新成功，并且受影响的行数为： " + rowsAffected);

            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                FinallyAction();
            }
        }
        public void FinallyAction() {
            try{
                //为空的时候
                if(psSearch != null) {
                    psSearch.close();
                    psSearch = null;
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

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException { //设置一个能抛出连接数据库，和数据库内异常的方法
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/stu?useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String key = "xxxxx";
        return DriverManager.getConnection(url,user,key);
    }
}
