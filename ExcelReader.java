import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.text.DecimalFormat;


public class ExcelReader {
    String AbosulteFile;
    public ExcelReader(String file) throws IOException { //
        AbosulteFile = file;
    }
    public void readExcel() {
        FileInputStream fis; //文件读入流
        String filepath = AbosulteFile; //绝对路径
        String tableName = "stu"; //数据表名称
        Connection ct; //连接，建立程序与数据库之间的连接
        Workbook workbook; //由Apache POI库提供的操作Excel文件的组件 在此处提供读取Excel文件的内容功能
        Sheet sheet; //存储读取工作表的内容
        try {
            fis = new FileInputStream(filepath); // 指定地址
            workbook = new XSSFWorkbook(fis); //XSSF为Xlsx文件
            workbook.setForceFormulaRecalculation(true);
            sheet = workbook.getSheetAt(0); //初始化从0行开始读
            ct = createDatabaseConnection(); //connection

            for(Row row : sheet) { //分别读取ID Cscore JavaScore
                Cell cell1 = row.getCell(0);
                Cell cell2 = row.getCell(1);
                Cell cell3 = row.getCell(2);

                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();  //类型转换器

                CellValue cellValue = evaluator.evaluate(cell1); //单元格值读取器 能不能给我一个完整的数字？
                System.out.print(cellValue);
                Double Doublevalue0 = cellValue.getNumberValue();
                DecimalFormat decimalFormat = new DecimalFormat("#.#########"); //科学计数法转换为正常数字的转换器
                System.out.print(" " + Doublevalue0);
                String valueID = decimalFormat.format(Doublevalue0);
                System.out.println(" " + valueID);

                cellValue = evaluator.evaluate(cell2); //
                double DoubleValue1 = cellValue.getNumberValue();
                int valueScore1 = (int) DoubleValue1;

                cellValue = evaluator.evaluate(cell3);
                double DoubleValue2 = cellValue.getNumberValue();
                int valueScore2 = (int) DoubleValue2;
                insertDataIntoDatabase(ct,tableName,valueID,valueScore1,valueScore2); //调用写入方法
            }
            ct.close();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException { //设置一个能抛出连接数据库，和数据库内异常的方法
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/stu?useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String key = "xxxxx";
        return DriverManager.getConnection(url,user,key);
    }
    public static void insertDataIntoDatabase(Connection connection,String tableName,String ID,int value1,int value2) throws SQLException {
        try {
            String sql = "UPDATE " + tableName + " SET Cscore = ?, JavaScore = ? WHERE ID = ?";
            PreparedStatement state = connection.prepareStatement(sql); //预编译指令 传入语句
            System.out.println(ID + " " + value1 + " " + value2);
            state.setDouble(1, value1); //设置值
            state.setDouble(2, value2);
            state.setString(3, ID);

            int RowAffected = state.executeUpdate(); //查询影响行数 判断是否执行成功
            System.out.println("更新成功，并且受影响的行数为： " + RowAffected);
        } catch (SQLException e) {
            throw new RuntimeException(e); //IDE推荐的 我不懂
        }
    }
}
