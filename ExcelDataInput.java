import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ExcelDataInput extends JDialog implements ActionListener{

    public JLabel tipsLabel;
    public JTextField pathTextField;
    public JButton chooseButton,cancelButton,confirmButton;
    Font textline = new Font("微软雅黑",Font.PLAIN,30);
    Font textfieldline = new Font("微软雅黑",Font.PLAIN,15);
    Font button = new Font("微软雅黑",Font.PLAIN,30);
    public ExcelDataInput(ScoreAdd owner, String title , boolean modal) { //普通JDialog页面
        this.setTitle(title);

        this.setLayout(new FlowLayout()); //设置流式布局

        tipsLabel = new JLabel("目录");
        tipsLabel.setFont(textline);
        chooseButton = new JButton("打开目录");
        cancelButton = new JButton("取消");
        confirmButton = new JButton("确认");
        chooseButton.addActionListener(this);
        confirmButton.addActionListener(this);
        cancelButton.addActionListener(this);
        chooseButton.setFont(button);
        cancelButton.setFont(button);
        confirmButton.setFont(button);  //初始化组件 添加监听器 设置字体

        pathTextField = new JTextField(25);
        pathTextField.setFont(textfieldline);
        pathTextField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));  //设置边框与底边距离


        this.add(tipsLabel);
        this.add(pathTextField);
        this.add(chooseButton);
        this.add(confirmButton);
        this.add(cancelButton);

        this.setSize(600,400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            if(e.getSource()==chooseButton) {  //当用户点击选择文件按钮时
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setPreferredSize(new Dimension(1200,900)); //初始化FileChooser类
                int returnValue = fileChooser.showOpenDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {   //如果点击确定按钮 读取绝对路径
                    String filepath = fileChooser.getSelectedFile().getAbsolutePath();
                    pathTextField.setText(filepath);  //文本框填充绝对路径
                }
            }
            if(e.getSource()==confirmButton) { //如果点击确定按钮
                try {
                    ExcelReader excelReader; // new一个 excelReader对象 catch防止超时的异常
                    try {
                        excelReader = new ExcelReader(pathTextField.getText().trim()); //传入绝对路径并执行构造函数
                        excelReader.readExcel(); //读取excel的xlsx文件
                    } catch (IOException ex) {
                        //catch 读入文件异常
                        System.out.println("Xlsx文件或读入有问题");
                        throw new RuntimeException(ex);
                    }
                } catch (RuntimeException ex) {
                    System.out.println("IDE提示您超时了！");
                    throw new RuntimeException(ex);
                }
            }
    }
}
