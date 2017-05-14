 package com.brc.frame;
 
 import com.brc.db.c3p0.C3P0DBConnectionManager;
 import com.brc.model.DBModel;
 import com.brc.tool.PropertyUtil;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import javax.swing.BorderFactory;
 import javax.swing.JButton;
 import javax.swing.JComboBox;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JOptionPane;
 import javax.swing.JPanel;
 import javax.swing.JTextField;
 import javax.swing.UIManager;
 
 public class MainFrame extends JFrame
   implements ActionListener
 {
   private static final long serialVersionUID = -2818046268082166135L;
   JPanel panel = new JPanel();
 
   JLabel dbType_lab = new JLabel("数据库类型："); JLabel dbUrl_lab = new JLabel(
     "数据库连接URL：");
   JLabel userName_lab = new JLabel("用户名：");
   JLabel password_lab = new JLabel("密码：");
   JTextField dbUrl_txt = new JTextField();
   JTextField userName_txt = new JTextField();
   JTextField password_txt = new JTextField();
 
   JComboBox dbType = new JComboBox();
 
   JButton button1 = new JButton("确 定"); JButton button2 = new JButton("退出");
 
   public MainFrame()
   {
     try
     {
       UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
     } catch (Exception e) {
       e.printStackTrace();
     }
 
     this.panel.setLayout(null);
     this.panel.setBorder(BorderFactory.createTitledBorder("请输入下面相关信息："));
     this.dbType_lab.setBounds(20, 40, 120, 30);
     this.dbUrl_lab.setBounds(20, 90, 120, 30);
     this.userName_lab.setBounds(20, 140, 120, 30);
     this.password_lab.setBounds(20, 190, 120, 30);
     this.dbType.setBounds(115, 40, 200, 30);
     this.dbType.addItem("oracle");
 
     this.dbUrl_txt.setBounds(115, 90, 200, 30);
     this.dbUrl_txt.setText(PropertyUtil.getProperty("url", 
       "jdbc:oracle:thin:@localhost:1521:xe"));
     this.userName_txt.setBounds(115, 140, 200, 30);
     this.userName_txt.setText(PropertyUtil.getProperty("username", "myres"));
     this.password_txt.setBounds(115, 190, 200, 30);
     this.password_txt.setText(PropertyUtil.getProperty("password", "myres"));
     this.button1.setBounds(30, 240, 100, 30);
     this.button2.setBounds(230, 240, 100, 30);
 
     this.panel.add(this.dbUrl_lab);
     this.panel.add(this.userName_lab);
     this.panel.add(this.password_lab);
     this.panel.add(this.dbUrl_txt);
     this.panel.add(this.userName_txt);
     this.panel.add(this.password_txt);
     this.panel.add(this.dbType_lab);
     this.panel.add(this.dbType);
 
     this.panel.add(this.button1);
     this.panel.add(this.button2);
 
     this.button1.addActionListener(this);
     this.button2.addActionListener(this);
 
     add(this.panel, "Center");
   }
 
   public void actionPerformed(ActionEvent e) {
     if (e.getSource() == this.button1) {
       String db_url = this.dbUrl_txt.getText().trim();
       String db_Type = this.dbType.getSelectedItem().toString();
       String userName = this.userName_txt.getText().trim();
       String password = this.password_txt.getText().trim();
       if ((!"".equals(userName)) && (!"".equals(password))) {
         DBModel model = new DBModel();
         model.setDBurl(db_url);
         model.setDBtype(db_Type);
         model.setDBuser(userName);
         model.setDBpwd(password);
         try {
           showTableFrame(model);
         } catch (Exception e1) {
           e1.printStackTrace();
           JOptionPane.showMessageDialog(null, "系统出现异常..." + e1, 
             "MR.BoomBa提醒您", 0);
         }
       } else if ("".equals(userName)) {
         JOptionPane.showMessageDialog(null, "请输入用户名", "STAR提醒您", 
           0);
       } else {
         JOptionPane.showMessageDialog(null, "用户名密码不允许为空！！", "STAR提醒您", 
           0);
       }
     }
     if (e.getSource() == this.button2) {
       C3P0DBConnectionManager.close();
       System.exit(0);
     }
   }
 
   public void showTableFrame(DBModel model)
     throws Exception
   {
     try
     {
       C3P0DBConnectionManager.setDbModel(model);
     } catch (Exception e) {
       throw e;
     }
     TableFrame tableF = new TableFrame(model);
     tableF.setResizable(false);
     tableF.setDefaultCloseOperation(3);
     tableF.setTitle("数据库表配置");
     tableF.setVisible(true);
     setVisible(false);
     try
     {
       PropertyUtil.setProperty("url", model.getDBurl());
       PropertyUtil.setProperty("username", model.getDBuser());
       PropertyUtil.setProperty("password", model.getDBpwd());
       PropertyUtil.writeData();
     } catch (Exception e1) {
       e1.printStackTrace();
     }
   }
 }

