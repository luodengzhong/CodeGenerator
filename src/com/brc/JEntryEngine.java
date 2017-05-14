 package com.brc;
 
 import com.brc.frame.MainFrame;
 
 public class JEntryEngine
 {
   public static void main(String[] args)
   {
     MainFrame login = new MainFrame();
     login.setResizable(false);
     login.setLocation(500, 260);
     login.setDefaultCloseOperation(3);
     login.setTitle("数据库表配置");
     login.setSize(350, 320);
     login.setVisible(true);
   }
 }

