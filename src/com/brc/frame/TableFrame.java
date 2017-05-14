 package com.brc.frame;
 
 import com.brc.action.FileGenerateAction;
 import com.brc.action.FlowFileGenerateAction;
 import com.brc.model.DBModel;
 import com.brc.model.TableModel;
 import com.brc.tool.FrameItemTool;
 import com.brc.tool.PropertyUtil;
 import java.awt.Color;
 import java.awt.Container;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.ItemEvent;
 import java.awt.event.ItemListener;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.io.File;
 import java.util.ArrayList;
 import java.util.List;
 import javax.swing.DefaultListModel;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JFileChooser;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JList;
 import javax.swing.JOptionPane;
 import javax.swing.JScrollPane;
 import javax.swing.JTextField;
 import javax.swing.UIManager;
 
 public class TableFrame extends JFrame
   implements ActionListener
 {
   private static final long serialVersionUID = -2818046268082166135L;
   DBModel model = null;
   JLabel tablesLab = new JLabel("所有表"); JLabel tablesLabChoose = new JLabel("所选表");
   JLabel storePathLab = new JLabel("存储路径：");
   JLabel rootPathLab = new JLabel("包路径：");
   JLabel classNameLab = new JLabel("领域名称："); JLabel queryLab = new JLabel("查询表：");
   JLabel createBy = new JLabel("创建者：");
   JLabel jspPathLab = new JLabel("JSP路径：");
   JList jlistLeft;
   JList jlistRight;
   DefaultListModel jlistModelLeft;
   DefaultListModel jlistModelRight;
   JButton left = new JButton("<");
   JButton right = new JButton(">");
   JTextField createByTxt = new JTextField();
   JTextField classNameTxt = new JTextField();
   JTextField queryTxt = new JTextField();
   JTextField basePathTxt = new JTextField();
   JTextField storePathTxt = new JTextField();
   JTextField jspPathTxt = new JTextField();
   JCheckBox isBean = new JCheckBox("生成Bean类");
   JCheckBox isService = new JCheckBox("生成Service类及其子类");
   JCheckBox isAction = new JCheckBox("生成Action文件");
   JCheckBox isJsp = new JCheckBox("生成jsp文件");
   JCheckBox isXML = new JCheckBox("生成xml文件");
   JCheckBox isOneDomain = new JCheckBox("生成为一个模型文件");
   JButton query = new JButton("查询");
   JButton commit = new JButton("确 定");
   JButton back = new JButton("返回");
   JButton chooserButton = new JButton("选择路径");
   Color not_editable_color = new Color(235, 235, 235);
 
   public TableFrame(DBModel dmodel) throws Exception {
     try {
       UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
     } catch (Exception e) {
       e.printStackTrace();
     }
 
     initGUI(dmodel);
   }
 
   private void initGUI(DBModel dmodel) throws Exception {
     setDefaultCloseOperation(2);
     getContentPane().setLayout(null);
 
     this.model = dmodel;
 
     this.queryLab.setBounds(20, 10, 60, 25);
     getContentPane().add(this.queryLab);
 
     this.queryTxt.setBounds(80, 10, 160, 25);
     getContentPane().add(this.queryTxt);
 
     this.query.setBounds(260, 8, 70, 30);
     getContentPane().add(this.query);
     this.query.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent actionEvent) {
         FrameItemTool.tablesList(TableFrame.this.jlistModelLeft, TableFrame.this.model, TableFrame.this.queryTxt
           .getText().trim());
       }
     });
     this.createBy.setBounds(340, 10, 60, 25);
     getContentPane().add(this.createBy);
 
     this.createByTxt.setBounds(390, 10, 70, 25);
     getContentPane().add(this.createByTxt);
     this.createByTxt.setText(PropertyUtil.getProperty("author", ""));
 
     this.tablesLab.setBounds(20, 40, 70, 30);
     getContentPane().add(this.tablesLab);
 
     this.tablesLabChoose.setBounds(280, 40, 70, 30);
     getContentPane().add(this.tablesLabChoose);
 
     this.jlistModelLeft = new DefaultListModel();
     FrameItemTool.tablesList(this.jlistModelLeft, this.model);
     this.jlistLeft = new JList(this.jlistModelLeft);
     this.jlistLeft.setVisibleRowCount(8);
     JScrollPane tables = new JScrollPane(this.jlistLeft);
     tables.setBounds(20, 70, 180, 180);
     getContentPane().add(tables);
     this.jlistLeft.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
         if (e.getClickCount() == 2)
           TableFrame.this.rightActionPerformed();
       }
     });
     this.jlistModelRight = new DefaultListModel();
     this.jlistRight = new JList(this.jlistModelRight);
     this.jlistRight.setVisibleRowCount(8);
     tables = new JScrollPane(this.jlistRight);
     tables.setBounds(280, 70, 180, 180);
     getContentPane().add(tables);
     this.jlistRight.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
         if (e.getClickCount() == 2)
           TableFrame.this.leftActionPerformed();
       }
     });
     this.left.setBounds(210, 100, 60, 30);
     getContentPane().add(this.left);
     this.left.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent actionEvent) {
         TableFrame.this.leftActionPerformed();
       }
     });
     this.right.setBounds(210, 190, 60, 30);
     getContentPane().add(this.right);
     this.right.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent actionEvent) {
         TableFrame.this.rightActionPerformed();
       }
     });
     this.rootPathLab.setBounds(20, 265, 70, 25);
     getContentPane().add(this.rootPathLab);
 
     this.basePathTxt.setBounds(80, 265, 280, 25);
     getContentPane().add(this.basePathTxt);
     this.basePathTxt.setText(PropertyUtil.getProperty("basePath", ""));
 
     this.storePathLab.setBounds(20, 300, 70, 25);
     getContentPane().add(this.storePathLab);
 
     this.storePathTxt.setBounds(80, 300, 280, 25);
     getContentPane().add(this.storePathTxt);
     this.storePathTxt.setText(PropertyUtil.getProperty("storePath", ""));
     this.storePathTxt.setEditable(false);
     this.storePathTxt.setBackground(this.not_editable_color);
 
     this.chooserButton.setBounds(370, 300, 90, 25);
     getContentPane().add(this.chooserButton);
 
     this.chooserButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent actionEvent) {
         JFileChooser chooser = new JFileChooser("./");
         chooser.setFileSelectionMode(1);
         int returnVal = chooser.showOpenDialog(TableFrame.this);
         if (returnVal == 0)
           TableFrame.this.storePathTxt.setText(chooser.getSelectedFile()
             .getAbsolutePath());
       }
     });
     this.isBean.setBounds(30, 330, 90, 20);
     getContentPane().add(this.isBean);
 
     this.isService.setBounds(125, 330, 110, 20);
     getContentPane().add(this.isService);
 
     this.isAction.setBounds(235, 330, 110, 20);
     getContentPane().add(this.isAction);
 
     this.isXML.setBounds(345, 330, 110, 20);
     getContentPane().add(this.isXML);
 
     this.isJsp.setBounds(30, 363, 110, 20);
     getContentPane().add(this.isJsp);
     this.isJsp.addItemListener(new ItemListener() {
       public void itemStateChanged(ItemEvent e) {
         JCheckBox jCheckBox = (JCheckBox)e.getSource();
         if (jCheckBox.isSelected()) {
           TableFrame.this.jspPathTxt.setEditable(true);
           TableFrame.this.jspPathTxt.setBackground(Color.white);
         } else {
           TableFrame.this.jspPathTxt.setEditable(false);
           TableFrame.this.jspPathTxt.setBackground(TableFrame.this.not_editable_color);
         }
       }
     });
     this.jspPathLab.setBounds(150, 360, 70, 25);
     getContentPane().add(this.jspPathLab);
 
     this.jspPathTxt.setBounds(200, 360, 170, 25);
     getContentPane().add(this.jspPathTxt);
     this.jspPathTxt.setText(PropertyUtil.getProperty("jspPath", ""));
     this.jspPathTxt.setEditable(false);
     this.jspPathTxt.setBackground(this.not_editable_color);
 
     this.isOneDomain.setBounds(30, 387, 140, 30);
     getContentPane().add(this.isOneDomain);
     this.isOneDomain.addItemListener(new ItemListener() {
       public void itemStateChanged(ItemEvent e) {
         JCheckBox jCheckBox = (JCheckBox)e.getSource();
         if (jCheckBox.isSelected()) {
           TableFrame.this.classNameTxt.setEditable(true);
           TableFrame.this.classNameTxt.setBackground(Color.white);
         } else {
           TableFrame.this.classNameTxt.setEditable(false);
           TableFrame.this.classNameTxt.setBackground(TableFrame.this.not_editable_color);
         }
       }
     });
     this.classNameLab.setBounds(180, 390, 70, 25);
     getContentPane().add(this.classNameLab);
 
     this.classNameTxt.setBounds(240, 390, 130, 25);
     getContentPane().add(this.classNameTxt);
     this.classNameTxt.setEditable(false);
     this.classNameTxt.setBackground(this.not_editable_color);
 
     this.commit.setBounds(80, 420, 100, 30);
     getContentPane().add(this.commit);
     this.commit.addActionListener(this);
 
     this.back.setBounds(280, 420, 100, 30);
     getContentPane().add(this.back);
     this.back.addActionListener(this);
 
     pack();
     setSize(480, 485);
     setLocationRelativeTo(null);
   }
 
   public void actionPerformed(ActionEvent e) {
     if (e.getSource() == this.commit) {
       int rightSize = this.jlistRight.getMaxSelectionIndex();
       DefaultListModel dListModel = (DefaultListModel)this.jlistRight
         .getModel();
       if (rightSize < 0) {
         JOptionPane.showMessageDialog(null, "请选择表！", "Mr.BoomBa提醒您", 
           0);
         return;
       }
       List selecedTables = new ArrayList(rightSize + 1);
       for (int i = 0; i <= rightSize; i++) {
         selecedTables.add(dListModel.get(i));
       }
       String className = this.classNameTxt.getText().trim();
       String basePath = this.basePathTxt.getText().trim();
       String storePath = this.storePathTxt.getText().trim();
       String jspPath = this.jspPathTxt.getText().trim();
       String author = this.createByTxt.getText().trim();
       boolean isBeanBol = this.isBean.isSelected();
       boolean isServiceBol = this.isService.isSelected();
       boolean isActionBol = this.isAction.isSelected();
       boolean isXMLBol = this.isXML.isSelected();
       boolean isJSPBol = this.isJsp.isSelected();
       boolean isOneDomainBol = this.isOneDomain.isSelected();
       if ((isOneDomainBol) && ("".equals(className))) {
         JOptionPane.showMessageDialog(null, "领域模型名不能为空");
         return;
       }
       if ((isJSPBol) && ("".equals(jspPath))) {
         JOptionPane.showMessageDialog(null, "JSP路径不能为空");
         return;
       }
       if ("".equals(basePath)) {
         JOptionPane.showMessageDialog(null, "包路径不能为空");
         return;
       }
       if ("".equals(storePath)) {
         JOptionPane.showMessageDialog(null, "存储路径不能为空");
         return;
       }
       if ((!isJSPBol) && (!isActionBol) && (!isServiceBol) && (!isBeanBol) && 
         (!isXMLBol)) {
         JOptionPane.showMessageDialog(null, "只是要选择一个生成文件类别");
         return;
       }
 
       try
       {
         PropertyUtil.setProperty("basePath", basePath);
         PropertyUtil.setProperty("storePath", storePath);
         PropertyUtil.setProperty("jspPath", jspPath);
         PropertyUtil.setProperty("author", author);
         PropertyUtil.writeData();
       } catch (Exception e1) {
         e1.printStackTrace();
       }
       try
       {
         TableModel tableModel = new TableModel();
         tableModel.setModel(this.model);
         tableModel.setBasePath(basePath);
         tableModel.setStorePath(storePath);
         tableModel.setSelecedTables(selecedTables);
         tableModel.setObjectName(className);
         tableModel.setBeanBol(isBeanBol);
         tableModel.setServiceBol(isServiceBol);
         tableModel.setActionBol(isActionBol);
         tableModel.setXMLBol(isXMLBol);
         tableModel.setJspBol(isJSPBol);
         tableModel.setJspPath(jspPath);
         tableModel.setOneDomainBol(isOneDomainBol);
         tableModel.setAuthor(author);
//         FileGenerateAction.engineEntry(tableModel);
         FlowFileGenerateAction.engineEntry(tableModel);
         JOptionPane.showMessageDialog(null, "已经成功生成");
       } catch (Exception e1) {
         e1.printStackTrace();
         JOptionPane.showMessageDialog(null, "系统出现异常..." + e1, 
           "Mr.BoomBa提醒您", 0);
       }
     }
 
     if (e.getSource() == this.back) {
       setVisible(false);
       MainFrame login = new MainFrame();
       login.setResizable(false);
       login.setLocation(500, 260);
       login.setDefaultCloseOperation(3);
       login.setTitle("数据库表配置");
       login.setSize(350, 320);
       login.setVisible(true);
     }
   }
 
   private void rightActionPerformed()
   {
     if (this.jlistLeft.getSelectedIndex() != -1) {
       this.jlistModelRight.addElement(this.jlistLeft.getSelectedValue());
       int i = this.jlistLeft.getSelectedIndex();
       this.jlistModelLeft.remove(i);
       this.jlistLeft.setSelectedIndex(i > 0 ? i - 1 : 0);
       this.jlistRight.setSelectedIndex(this.jlistModelRight.size() - 1);
     }
   }
 
   private void leftActionPerformed()
   {
     if (this.jlistRight.getSelectedIndex() != -1) {
       this.jlistModelLeft.addElement(this.jlistRight.getSelectedValue());
       int i = this.jlistRight.getSelectedIndex();
       this.jlistModelRight.remove(i);
       this.jlistRight.setSelectedIndex(i > 0 ? i - 1 : 0);
       this.jlistLeft.setSelectedIndex(this.jlistModelRight.size() - 1);
     }
   }
 }

