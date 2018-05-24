package com.snack.cn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class SnackClient extends JFrame{
    private static SnackClient sc;  
    private SnackBody snackFood;  
    //当前游戏状态  
    private boolean gameState = true;  
    private MyComponent mc;  
    //游戏  
    private boolean pause = false;  
    private JMenu menu;  
    private JMenuBar menubar;  
    //低等级  
    private JMenuItem lowSpeedItem;  
    //中等级  
    private JMenuItem midSpeedItem;  
    //高等级  
    private JMenuItem heightSpeedItem;  
    //重新开始  
    private JMenuItem restartItem;  
    public int speed = 15;  
    static int score = 0;  
    private MaxScore ms;  
    //蛇身子  
    ArrayList<SnackBody> snackBodys = new ArrayList<SnackBody>();  
    //蛇头  
    static SnackBody snackHead;  
    private static final long serialVersionUID = 1L;  
  
    public SnackClient() {  
        //初始化菜单栏  
        initMenu();  
        mc = new MyComponent();  
        mc.setBackground(Color.BLACK);  
        add(mc);  
        setTitle("贪吃蛇");  
        setSize(800, 600);  
        setLocationRelativeTo(null);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setVisible(true);  
        setResizable(false);  
        snackHead = new SnackBody(100, 100, this);  
        for (int i = 0; i < 2; i++) {  
            snackBodys.add(new SnackBody(-100, -100, this));  
        }  
        ms = new MaxScore();  
        showSnackFood();  
        addKeyListener(new MyKeyListener());  
        new paintThread().start();  
    }  
  
    private void initMenu() {  
        // TODO Auto-generated method stub  
        menu = new JMenu("参数设置");  
        lowSpeedItem = new JMenuItem("低等级");  
        midSpeedItem = new JMenuItem("中等级");  
        heightSpeedItem = new JMenuItem("高等级");  
        restartItem=new JMenuItem("重新开始");  
        lowSpeedItem.addActionListener(new ActionListener() {  
  
            public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                speed = 15;  
            }  
        });  
        midSpeedItem.addActionListener(new ActionListener() {  
  
            public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                speed = 17;  
            }  
        });  
        heightSpeedItem.addActionListener(new ActionListener() {  
  
            public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                speed = 20;  
            }  
        });  
        restartItem.addActionListener(new ActionListener() {  
              
            public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                dispose();  
                sc=new SnackClient();  
            }  
        });  
        menu.add(lowSpeedItem);  
        menu.add(midSpeedItem);  
        menu.add(heightSpeedItem);  
        menu.add(restartItem);  
        menubar = new JMenuBar();  
        menubar.add(menu);  
        setJMenuBar(menubar);  
    }  
  
    public boolean isPause() {  
        return pause;  
    }  
  
    public void setPause(boolean pause) {  
        this.pause = pause;  
    }  
  
    public static void main(String[] args) {  
        // TODO Auto-generated method stub  
        sc = new SnackClient();  
    }  
  
    public class MyComponent extends JPanel {  
  
        /** 
         *  
         */  
        private static final long serialVersionUID = 1L;  
  
        protected void paintComponent(Graphics g) {  
            // TODO Auto-generated method stub  
            super.paintComponent(g);  
              
            if (!gameState) {  
                Font font = new Font("微软雅黑", Font.BOLD, 50);  
                g.setFont(font);  
                g.setColor(Color.RED);  
                g.drawString("GAME OVER!!", this.getWidth() / 2 - 150,  
                        this.getHeight() / 2);  
                return;  
            }  
  
            // 如果蛇撞到蛇身 或 撞墙 游戏结束  
            if (snackHead.hitWall() || snackHead.hitSnackBody(snackBodys)) {  
                gameState = false;  
            }  
            // 如果蛇头吃到食物  
            if (snackHead.hitSnackBody(snackFood)) {  
                snackBodys.add(snackFood);  
                score++;  
                // 刷新食物  
                showSnackFood();  
            }  
            // 画食物  
            snackFood.draw(g);  
            // 画蛇身  
            if (gameState) {  
                for (SnackBody sb : snackBodys) {  
                    sb.draw(g);  
                }  
            }  
            // 画蛇头  
            if (gameState) {  
                snackHead.draw(g);  
            }  
            try {  
                g.drawString(  
                        "当前分数：" + score * 100 + "      游戏最高分:"  
                                + ms.getMaxScore(), 0, 10);  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }  
    //随机出现食物  
    public void showSnackFood() {  
  
        Random r = new Random();  
        boolean exit = true;  
        while (exit) {  
            int x = r.nextInt(mc.getWidth() - snackHead.getWidth());  
            int y = r.nextInt(mc.getHeight() - snackHead.getHeight());  
            snackFood = new SnackBody(x, y, sc);  
            if (snackFood.hitSnackBody(snackHead)  
                    || snackFood.hitSnackBody(snackBodys)) {  
                continue;  
            }  
            exit = false;  
        }  
  
    }  
    //键盘事件  
    public class MyKeyListener extends KeyAdapter {  
        @Override  
        public void keyPressed(KeyEvent e) {  
            // TODO Auto-generated method stub  
            snackHead.keyPress(e);  
        }  
  
        @Override  
        public void keyReleased(KeyEvent e) {  
            // TODO Auto-generated method stub  
            snackHead.keyRelease(e);  
        }  
    }  
    //游戏刷新  
    public class paintThread extends Thread {  
        public void run() {  
            // TODO Auto-generated method stub  
            while (true) {  
                if (!pause) {  
                    repaint();  
                    try {  
                        Thread.sleep(100);  
                    } catch (InterruptedException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                }  
  
            }  
        }  
    }  
}
