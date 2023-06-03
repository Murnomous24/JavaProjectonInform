
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends JFrame implements ActionListener{
    JPanel TestPanel,DataPanel,ButtonPanel, InformPanel,cardPanel, GradesPanel,MainPanel;
    JTree menuTree;
    JLabel jl1;
    JButton jb1,jb2,jb3,jb4;
    JTable jt;
    JScrollPane jsp,jspmenu;
    JTextField jtf;
    StuModel sm;
    Statement statu = null;
    PreparedStatement ps;
    Connection ct = null;
    ResultSet rs = null;
    CardLayout cardLayout;

    public static void main(String[] args) {
        new Login();
    }

    public Main(String title) {

        super(title);
        cardLayout = new CardLayout();
        //this.setLayout(new BorderLayout());
        MainPanel = new JPanel();
        DataPanel = new JPanel();
        ButtonPanel = new JPanel();
        InformPanel = new JPanel();
        TestPanel = new JPanel();
        GradesPanel = new ScoreAdd();
        GradesPanel.setLayout(new BorderLayout());
        InformPanel.setLayout(new BorderLayout());
        MainPanel.setLayout(new BorderLayout());
        cardPanel = new JPanel(cardLayout);

        DefaultMutableTreeNode FatherMenu = new DefaultMutableTreeNode("Menu");
        DefaultMutableTreeNode Inform = new DefaultMutableTreeNode("Inform");
        DefaultMutableTreeNode Grades = new DefaultMutableTreeNode("Grades");
        FatherMenu.add(Inform);
        FatherMenu.add(Grades);

        TreeCellRender treeCellRender = new TreeCellRender();
        menuTree = new JTree(FatherMenu);
        menuTree.setCellRenderer(treeCellRender);
        menuTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode =(DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();
            if(selectedNode != null && selectedNode.isLeaf()) {
                String selectedMenu = selectedNode.getUserObject().toString();
                System.out.println("已经切换了另一界面" + selectedMenu.toString());
                cardLayout.show(cardPanel,selectedMenu);
            }
        });

        jspmenu = new JScrollPane(menuTree);
        jspmenu.setPreferredSize(new Dimension(200,getHeight()));

        jtf = new JTextField(20);
        jtf.setSize(20,30);
        jl1 = new JLabel("请输入姓名：");
        jl1.setFont(new Font("微软雅黑",Font.PLAIN,25));
        DataPanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        ButtonPanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        jb1 = new JButton("查询");
        jb2 = new JButton("新增");
        jb3 = new JButton("修改");
        jb4 = new JButton("删除");
        jb1.setFont(new Font("微软雅黑",Font.PLAIN,25));
        jb2.setFont(new Font("微软雅黑",Font.PLAIN,25));
        jb3.setFont(new Font("微软雅黑",Font.PLAIN,25));
        jb4.setFont(new Font("微软雅黑",Font.PLAIN,25));
        UIManager.put("OptionPane.messageFont",new Font("宋体",Font.BOLD,30));
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        jb4.addActionListener(this);

        DataPanel.add(jl1);
        DataPanel.add(jtf);
        DataPanel.add(jb1);
        ButtonPanel.add(jb2);
        ButtonPanel.add(jb3);
        ButtonPanel.add(jb4);

        sm = new StuModel();
        jt = new JTable(sm);
        TableCellRender tableCellRender = new TableCellRender();
        jt.setRowHeight(30);
        JTableHeader tableHeader = jt.getTableHeader();
        tableHeader.setFont(new Font("微软雅黑",Font.PLAIN,20));
        //jt.getColumnModel().getColumn(jt.getColumnCount()).setCellRenderer(tableCellRender);
        jt.setFont(new Font("微软雅黑",Font.PLAIN,15));
        jt.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        jsp = new JScrollPane(jt);
        jsp.setFont(new Font("微软雅黑",Font.PLAIN,20));


        InformPanel.add(DataPanel,"North");
        InformPanel.add(ButtonPanel,"South");
        InformPanel.add(jsp);
        //SecondPanel.add(scoreAddPanel,"Center");
        cardPanel.add(InformPanel,"Inform");
        cardPanel.add(GradesPanel,"Grades");
        MainPanel.add(jspmenu,BorderLayout.WEST);
        MainPanel.add(cardPanel,BorderLayout.CENTER);
        this.add(MainPanel);
        this.pack();
        this.setSize(1600,1200);
        this.setLocation(600,600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent arg1) {
        //判断所按下按钮
        //如果按钮为查询
        if(arg1.getSource() == jb1) {
            System.out.println("用户希望查询数据");
            String name = this.jtf.getText().trim();
            String sql = "select * from stu where Name = '" + name + "' ";
            sm = new StuModel(sql);
            jt.setModel(sm);
        }

        //如果按钮是新增
        else if(arg1.getSource() == jb2) {
            System.out.println("用户希望添加数据");
            StuAddDiag sa = new StuAddDiag(this,"添加学生",true);

            sm = new StuModel();
            jt.setModel(sm);
        }
        //如果按钮是删除
        else if(arg1.getSource() == jb4) {
            int rowNum = this.jt.getSelectedRow();

            if(rowNum==-1) {
                JOptionPane.showMessageDialog(this,"您当前还未选中。请选中一行");
                return;
            }

            String stuID = (String) sm.getValueAt(rowNum,0);
            System.out.println("ID： " + stuID);

            //使用try-catch
            if(confirm()) {
                StuDelte stuDelte = new StuDelte(stuID);
                stuDelte.delteinform();
                sm = new StuModel();
                jt.setModel(sm);
            }
        }
        else if(arg1.getSource() == jb3) {
            System.out.println("用户想要修改数据");
            int rowNum = this.jt.getSelectedRow();
            if(rowNum==-1) {
                JOptionPane.showMessageDialog(this,"您当前还未选中。请选中一行");
                return;
            }
            System.out.println("修改中");
            StuUpDiag su = new StuUpDiag(this,"修改学号",true,sm,rowNum);
            sm = new StuModel();
            jt.setModel(sm);
        }
    }
    public Boolean confirm() {
        int select = JOptionPane.showConfirmDialog(this,
                "是否确认删除？",
                        "确认",
                JOptionPane.YES_NO_OPTION);
        if(select == 0) {
            System.out.println("正在执行删除操作");
            return true;
        }
        return false;
    }
    public class TableCellRender extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Font font = new Font("微软雅黑", Font.PLAIN, 15); // 设置字体和大小
            cellComponent.setFont(font); // 设置单元格字体
            return cellComponent;
        }
    }
    public class TreeCellRender extends DefaultTreeCellRenderer {
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setFont(new Font("微软雅黑", Font.PLAIN, 25));
            }
            return c;
        }
    };
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