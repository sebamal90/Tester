/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.BTDeviceResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.MessageResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.MessageConsole;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.Message;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

/**
 *
 * @author SebaTab
 */
public class Main {

    private static JFrame frame;
    private MessageResource messageResource = new MessageResource(this);
    private BTDeviceResource btdeviceResource = new BTDeviceResource(this);
    private TestResource testResource = new TestResource(this);
    private JTextPane logger = new javax.swing.JTextPane();


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new Main();
    }
    private JLabel heartRate;
    private Image heart1;
    private Image heart2;
    private JLabel heart;
    private HeartBeat hb;
    private JPanel monitor;
    private JPanel panel;
    private JPanel worker;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private JSeparator menuSeparator;
    private JPanel menu;
    private JPanel menu2;
    private JLabel clockLabel;
    private long time;
    private JLabel powerLabel;
    private JLabel timeLabel;
    private boolean connectionEstabilished;
    private JButton startStopButton;
    private JButton pauseResumeButton;
    
    public Main() throws IOException {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //frame.setMinimumSize(new Dimension(800, 600));

        frame.setSize(dim);
        //setFullscreen();
        addMenuBar();
        addMenu();
        addLogger();
        addWorker();
       
        
        frame.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){
 
            @Override
            public void ancestorMoved(HierarchyEvent e) {           
            }
            @Override
            public void ancestorResized(HierarchyEvent e) {
                
                menu2.setPreferredSize(new Dimension(100, menu.getHeight()));
                menuSeparator.setPreferredSize(new Dimension(2, menu.getHeight()));
                menu2.setSize(new Dimension(100, menu.getHeight()));
                menuSeparator.setPreferredSize(new Dimension(2, menu.getHeight()));
                
                monitor.setPreferredSize(new Dimension(frame.getWidth()-115, 100));
                panel.setPreferredSize(new Dimension(frame.getWidth()-115, 100));
            }          
        });
        
        frame.setVisible(true);
        
        MonitorUpdater mU = new MonitorUpdater();
        mU.start();
    }
    
    public static void setFullScreen(JFrame frame, boolean fullScreen) {
        frame.dispose();
        frame.setResizable(!fullScreen);
        frame.setUndecorated(fullScreen);
        if (fullScreen) {
          frame.setLocation(0, 0);
          frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
        }
        frame.setVisible(true);
        frame.repaint();
    }
    
    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        ActionListener menuBarListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if (action.equals("Exit")) {
                    System.exit(0);
                }
            }
        };
        
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
                
        JMenuItem exitItem = fileMenu.add("Exit");       
        exitItem.addActionListener(menuBarListener);  
    }
    
    private void addMenu() {
        int menuSize = 100;
        menu = new JPanel(); 
        menu.setPreferredSize(new Dimension(menuSize+15, menu.getHeight()));
        
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if (action.equals("Start")) {
                    if (connectionEstabilished) {
                        testResource.startTest();
                        startStopButton.setText("End");
                        pauseResumeButton.setEnabled(true);
                    }
                    else {
                        System.out.println("Don't connected with hr Listener");
                    }
                } else if (action.equals("End")) {
                    System.exit(0);
                } else if (action.equals("Pause")) {
                    testResource.pauseTest();
                    pauseResumeButton.setText("Resume");
                } else if (action.equals("Resume")) {
                    testResource.resumeTest();
                    pauseResumeButton.setText("Pause");
                }
                else if (action.equals("Stats")) {
                    btdeviceResource.getDevices();
                } else if (action.equals("Connect")) {
                    btdeviceResource.connect(Config.deviceAddress, Config.deviceType);
                } 
            }
        };
        
        menu2 = new JPanel();
//        menu2.setBackground(Color.red);
        menu2.setPreferredSize(new Dimension(menuSize, 450));
        
        startStopButton = new JButton("Start");
        startStopButton.setPreferredSize(new Dimension(menuSize, menuSize));
        startStopButton.addActionListener(menuListener); 
        menu2.add(startStopButton);
        
        pauseResumeButton = new JButton("Pause");
        pauseResumeButton.setPreferredSize(new Dimension(menuSize, menuSize/2));
        pauseResumeButton.addActionListener(menuListener); 
        pauseResumeButton.setEnabled(false);
        menu2.add(pauseResumeButton);
        
        JButton statsButton = new JButton("Stats");
        statsButton.setPreferredSize(new Dimension(menuSize, menuSize));
        //statsButton.setEnabled(false);
        statsButton.addActionListener(menuListener);
        menu2.add(statsButton);
        
        JButton connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(menuSize, menuSize));
        connectButton.addActionListener(menuListener);
        menu2.add(connectButton);
       
        menu.add(menu2);

        menuSeparator = new JSeparator(SwingConstants.VERTICAL);
        menuSeparator.setPreferredSize(new Dimension(2, 450));
        //separator.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        menu.add(menuSeparator);
        
        frame.add(menu, BorderLayout.WEST);
    }
    
    private void addLogger() {
        JScrollPane scrollPane = new javax.swing.JScrollPane();
        frame.add(scrollPane, BorderLayout.SOUTH);
        scrollPane.setPreferredSize(new Dimension((int)dim.getWidth(), 100));
        //logger.setRows(5);
        logger.setEditable(false);
        scrollPane.setViewportView(logger);
        
        MessageConsole mc = new MessageConsole(logger);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(100);
    
    }
    
    private void addWorker() throws IOException {
        worker = new JPanel();
        frame.add(worker, BorderLayout.CENTER);
//        worker.setBackground(Color.GRAY);
        monitor = new JPanel();
        monitor.setLayout(new FlowLayout(FlowLayout.LEFT));
        //monitor.setLayout(new GridLayout(1, 2));
 //       monitor.setBackground(Color.WHITE);
        //monitor.setPreferredSize(new Dimension(500, 100));
        monitor.setBorder(BorderFactory.createEmptyBorder(-5,10,0,0));
        monitor.setPreferredSize(new Dimension((int)dim.getWidth()-115, 100));
        worker.add(monitor, BorderLayout.NORTH);
        
        heartRate = new JLabel("0");
        heartRate.setHorizontalAlignment(SwingConstants.RIGHT);
        heartRate.setPreferredSize(new Dimension(150, 100));
        heartRate.setFont(heartRate.getFont().deriveFont(100.0f));
        monitor.add(heartRate);
                
        Image img = ImageIO.read(getClass().getResource("resources/heart1.png"));
        heart1 = img.getScaledInstance(80,80, 0);
        img = ImageIO.read(getClass().getResource("resources/heart2.png"));
        heart2 = img.getScaledInstance(80,80, 0);
        heart = new JLabel(new ImageIcon(heart1));
        heart.setHorizontalAlignment(SwingConstants.CENTER);
        heart.setPreferredSize(new Dimension(100, 100));
        monitor.add(heart);
        
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        monitor.add(separator);
  
        powerLabel = new JLabel("0W");
        powerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        powerLabel.setPreferredSize(new Dimension(270, 100));
        powerLabel.setFont(powerLabel.getFont().deriveFont(100.0f));
        monitor.add(powerLabel);
        
        separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        monitor.add(separator);
        
        timeLabel = new JLabel("0:00");
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setPreferredSize(new Dimension(290, 100));
        timeLabel.setFont(timeLabel.getFont().deriveFont(100.0f));
        monitor.add(timeLabel);
        
        separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        monitor.add(separator);
        
        SimpleDateFormat ft = new SimpleDateFormat ("HH:mm");
        clockLabel = new JLabel(ft.format(new Date()));
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clockLabel.setPreferredSize(new Dimension(290, 100));
        clockLabel.setFont(clockLabel.getFont().deriveFont(100.0f));
        monitor.add(clockLabel);
        
        separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        monitor.add(separator);
        
        separator = new JSeparator();
        separator.setPreferredSize(new Dimension((int)dim.getWidth()-115, 10));
        worker.add(separator, BorderLayout.NORTH);
        
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
//        panel.setBackground(Color.BLUE);
        JLabel lab = new JLabel("0");
        panel.add(lab);
        panel.setPreferredSize(new Dimension((int)dim.getWidth()-115, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        worker.add(panel, BorderLayout.CENTER);
    }

    public MessageResource getMessageResource() {
        return messageResource;
    }

    public BTDeviceResource getBtdeviceResource() {
        return btdeviceResource;
    }

    public void connectionEstabilished() {
        System.out.println("Connection estabilished");
        hb = new HeartBeat();
        connectionEstabilished = true;
        Thread t = new Thread((Runnable)hb);
        t.start();
    }    
    
    public HeartBeat getHeartBeat() {
        return hb;
    }
    
    public class HeartBeat implements Runnable{
        private int HR;
        private Message lastMessage;
        
        public HeartBeat() {
            HR = 0;
            lastMessage = new Message();
            lastMessage.setTime(System.currentTimeMillis());
            lastMessage.setHr(0);
        }
        @Override
        public void run() {
            while(true) {
                setHR();
                if (HR!=0) {
                    int sleep = 1000/HR;

                    try {
                        Thread.sleep(40*sleep);
                    } catch (InterruptedException ex) {}

                    heart.setIcon(new ImageIcon(heart2));

                    try {
                        Thread.sleep(20*sleep);
                    } catch (InterruptedException ex) {}
                    
                    heart.setIcon(new ImageIcon(heart1));
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {}
                }
            }
        }
        
        public void setHR() {
            Message message = messageResource.getLastMessage();

            if (message !=null) {
                if (message.getTime() == lastMessage.getTime() 
                    && System.currentTimeMillis()-lastMessage.getTime() > 3000) {
                    HR = (int)(HR *0.8) ;
                    heartRate.setText(HR+"!");
                } else {
                    lastMessage = message;
                    HR = lastMessage.getHr();
                    heartRate.setText(HR+"");
                }
            }
        }
        
        public int getHR() {
            return HR;
        }
    }
    
    private class MonitorUpdater extends Thread {
        private SimpleDateFormat ft = new SimpleDateFormat ("HH:mm");
        
        @Override
        public void run() {
            int i=0;
            while(true) {                   
                clockLabel.setText(ft.format(new Date()));
                if (testResource.getTestStatus()) {
                    timeLabel.setText(testResource.getTimer());
                }
                powerLabel.setText(i+"W");
                i++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
}
