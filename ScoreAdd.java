import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreAdd extends JPanel implements ActionListener {

    JLabel Cscore,JavaScore,PathInput;
    JDialog inform1;
    JPanel CsocrePanel, JavaScorePanel, ExcelFileInputPanel;
    JButton Add1,Add2,ScoreDetails1,ScoreDetails2,PathAdd;
    Font headline = new Font("微软雅黑",Font.PLAIN,40);
    Font button = new Font("微软雅黑",Font.PLAIN,30);

    public ScoreAdd() {

        this.setLayout(null);
        Cscore = new JLabel("C语言期末成绩");
        JavaScore = new JLabel("Java期末成绩");
        PathInput = new JLabel("Excel文件读入");
        Cscore.setFont(headline);
        JavaScore.setFont(headline);
        PathInput.setFont(headline);

        Add1 = new JButton("新增");
        ScoreDetails1 = new JButton("成绩详情");
        Add2 = new JButton("新增");
        ScoreDetails2 = new JButton("成绩详情");
        PathAdd = new JButton("上传");

        ScoreDetails2.setFont(button);
        ScoreDetails1.setFont(button);
        Add1.setFont(button);
        Add2.setFont(button);
        PathAdd.setFont(button);

        PathAdd.addActionListener(this);
        Add1.addActionListener(this);
        Add2.addActionListener(this);
        ScoreDetails1.addActionListener(this);
        ScoreDetails2.addActionListener(this);

        inform1 = new JDialog();

        CsocrePanel = new JPanel();
        JavaScorePanel = new JPanel();
        ExcelFileInputPanel = new JPanel();
        CsocrePanel.setLayout(new FlowLayout());
        JavaScorePanel.setLayout(new FlowLayout());
        ExcelFileInputPanel.setLayout(new FlowLayout());

        CsocrePanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6)); //设置与边框的边界
        JavaScorePanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        ExcelFileInputPanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        CsocrePanel.add(Cscore);
        CsocrePanel.add(Add1);
        CsocrePanel.add(ScoreDetails1);
        JavaScorePanel.add(JavaScore);
        JavaScorePanel.add(Add2);
        JavaScorePanel.add(ScoreDetails2);
        ExcelFileInputPanel.add(PathInput);
        ExcelFileInputPanel.add(PathAdd);

        this.setSize(1600,1200);
        CsocrePanel.setBounds(500,200,300,200);
        JavaScorePanel.setBounds(500,500,300,200);
        ExcelFileInputPanel.setBounds(500,800,300,200);

        CsocrePanel.setVisible(true);
        JavaScorePanel.setVisible(true);
        ExcelFileInputPanel.setVisible(true);
        this.add(CsocrePanel);
        this.add(JavaScorePanel);
        this.add(ExcelFileInputPanel);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==Add1) { //添加C语言成核
            System.out.println("用户希望添加学生的C语言成绩");
            ScoreDataAdd sda = new ScoreDataAdd(this,"Cscore","C语言成绩",true);
        }
        if(e.getSource()==Add2) { //添加Java语言成绩
            System.out.println("用户希望添加学生的Java语音成绩");
            ScoreDataAdd sda = new ScoreDataAdd(this,"JavaScore","Java语言成绩",true);
        }
        if(e.getSource()==ScoreDetails1) { //查看C语言成绩可视化数据
            System.out.println("用户希望查看C成绩可视化后的数据");
            ScoreDataAnalysis sdan = new ScoreDataAnalysis(this,"Cscore",true);
        }
        if(e.getSource()==ScoreDetails2) { //查看Java成绩可视化数据
            System.out.println("用户希望查看Java成绩可视化后的数据");
            ScoreDataAnalysis sdan = new ScoreDataAnalysis(this,"JavaScore",true);
        }
        if(e.getSource()==PathAdd) { //文件输入
            System.out.println("用户希望从Excel表格上传成绩数据");
            ExcelDataInput edi = new ExcelDataInput(this,"Excel数据上传",true);
        }
    }
}
