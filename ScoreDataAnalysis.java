import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.DataUtils;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.*;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ScoreDataAnalysis extends JDialog implements ActionListener {

    PreparedStatement state = null;
    Connection ct = null;
    ResultSet rs = null;
    DefaultPieDataset<String> datapieset;
    int[] num,xvalue,yvalue;
    double[] scores;
    int index;
    JFreeChart piechart,curvechart;
    JPanel MessagePanel;
    JLabel Messgae1,Message2,Message3;
    PiePlot plotsetting;
    Font messageFont = new Font("微软雅黑", Font.PLAIN, 35);
    WeightedObservedPoints obs;
    public ScoreDataAnalysis(ScoreAdd owner, String title , boolean modal) {

        this.setTitle(title);
        this.setLayout(new GridLayout(1,2));
        num = new int[3];
        index = 0;
        obs = new WeightedObservedPoints();
        try{
            ct = createDatabaseConnection();

            String columnname = this.getTitle();
            int[] lowerBound = new int[3];
            int[] UpperBound = new int[3];
            lowerBound[0] = 0;  //制作三个成绩分区
            UpperBound[0] = 60;
            lowerBound[1] = 60;
            UpperBound[1] = 80;
            lowerBound[2] = 80;
            UpperBound[2] = 100;
            String query1 = "SELECT COUNT(" + columnname + ") FROM stu WHERE " + columnname + " >= ? AND " + columnname + " <= ?"; //遍历查询
            for(int i=0; i<3; i++) {
                state = ct.prepareStatement(query1);
                state.setInt(1,lowerBound[i]);
                state.setInt(2,UpperBound[i]);
                rs = state.executeQuery();
                if(rs.next()) {
                    num[i] = rs.getInt(1);
                    System.out.println("数据个数：" + num[i]); //num数组存储不同分段的人数
                }
                else {
                    System.out.println("返回数据为空！");
                }

            String query2 = "SELECT " + this.getTitle() +  " FROM stu";
            rs = state.executeQuery(query2);
            int rowCount = 0;
            if(rs.last()) {
                rowCount = rs.getRow();
                rs.beforeFirst();
            }
            scores = new double[100];
                while(rs.next()) {
                int score = rs.getInt(this.getTitle());
                scores[score]++;
                index++;
            }
            }
            rs.close();
            state.close();
            ct.close();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        datapieset = new DefaultPieDataset<>(); //作为类型
        datapieset.setValue("60-",num[0]);
        datapieset.setValue("60-80",num[1]);
        datapieset.setValue("80-100",num[2]);

        piechart = ChartFactory.createPieChart(
                 this.getTitle()+"PieChart",
                datapieset,
                true, //是否显示图例
                true,  //是否生成工具提示
                false // 是否生成URL链接
        );
        plotsetting = (PiePlot) piechart.getPlot();
        plotsetting.setSectionPaint("60-",Color.RED);
        plotsetting.setSectionPaint("60-80",Color.GREEN);
        plotsetting.setSectionPaint("80-100",Color.YELLOW);
        ChartPanel chartPanel1 = new ChartPanel(piechart);


        XYSeriesCollection datacurve = new XYSeriesCollection();
        XYSeries series = new XYSeries("Scores");
        XYSeries curveseries = new XYSeries("Score2");
        //datacurve.addSeries("Scores",scores,scores.length);
        for(int i=0; i<scores.length; i++) {
            series.add(i,scores[i]);
            obs.add(i,scores[i]);  //分别为折线和曲线做数据对
        }

        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
        double[] coefficients = fitter.fit(obs.toList()); //转化成List将数据
        double stepSize = 1;  //由于数据为整数 取间隔为1
        for (double x = 0; x <= 100; x += stepSize) {
            double y = coefficients[0] + coefficients[1] * x + coefficients[2] * x * x;  //做二项式曲线拟合 ？？研究一下
            curveseries.add(x, y);
        }

        datacurve.addSeries(curveseries); //可以选择series和curveseries两种


        curvechart = ChartFactory.createXYLineChart(
                this.getTitle()+"CurveChart",
                "Score",
                "Count",
                datacurve,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        XYPlot plot = (XYPlot) curvechart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        ChartPanel chartPanel2 = new ChartPanel(curvechart);

        MessagePanel = new JPanel(new GridLayout(3,1));
        Messgae1 = new JLabel("不及格人数： " + num[0]);
        Message2 = new JLabel("60-80(良)： "+ num[1]);
        Message3 = new JLabel("80-100(优)： " + num[2]);
        Messgae1.setFont(messageFont);
        Message2.setFont(messageFont);
        Message3.setFont(messageFont);
        MessagePanel.add(Messgae1);
        MessagePanel.add(Message2);
        MessagePanel.add(Message3);

        this.add(chartPanel1); //饼图
        this.add(chartPanel2); //折线图
        this.add(MessagePanel); //文字显示信息
        this.setLocationRelativeTo(null);
        this.setSize(1600, 1200);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException { //设置一个能抛出连接数据库，和数据库内异常的方法
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/stu?useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String key = "xxxxx";
        return DriverManager.getConnection(url,user,key);
    }
}

