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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

/**
 *
 * @author SebaTab
 */
public class Main {

    private JFrame frame;
    private MessageResource messageResource = new MessageResource(this);
    private BTDeviceResource btdeviceResource = new BTDeviceResource(this);
    private TestResource testResource = new TestResource(this);
    private JTextPane logger = new javax.swing.JTextPane();
    private HeartBeat hb;
    private Monitor monitor;
    private JPanel panel;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private JSeparator menuSeparator;
    private JPanel menu;
    private JPanel menu2;
    private boolean isConnectionEstabilished;
    private JButton startStopButton;
    private JButton pauseResumeButton;
    private JButton connectButton;
    private JButton deviceButton;
    private JScrollPane scrollPane;
    private DataTable dT;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new Main();
    }

    public Main() throws IOException {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //frame.setMinimumSize(new Dimension(800, 600));

        frame.setSize(dim);
        //setFullscreen();
        addMenuBar();
        addMenu();
        addLogger();
        addWorker();
        frame.getContentPane().
                addHierarchyBoundsListener(new HierarchyBoundsListener() {
            @Override
            public void ancestorMoved(HierarchyEvent e) {
                //not used
            }

            @Override
            public void ancestorResized(HierarchyEvent e) {
                menu2.setPreferredSize(new Dimension(100, menu.getHeight()));
                menuSeparator.setPreferredSize(
                        new Dimension(2, menu.getHeight()));
                menu2.setSize(new Dimension(100, menu.getHeight()));
                menuSeparator.setPreferredSize(
                        new Dimension(2, menu.getHeight()));
                monitor.setPreferredSize(
                        new Dimension(frame.getWidth() - 115, 100));
                scrollPane.setPreferredSize(
                        new Dimension(frame.getWidth() - 130,
                                      frame.getHeight() - 300));
            }
        });

        frame.setVisible(true);
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
                    close();
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
        menu.setPreferredSize(new Dimension(menuSize + 15, menu.getHeight()));

        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if (action.equals("Start")) {
                    if (isConnectionEstabilished) {
                        testResource.startTest();
                        startStopButton.setText("End");
                        pauseResumeButton.setEnabled(true);
                    } else {
                        System.out.println("Don't connected with hr Listener");
                    }
                } else if (action.equals("End")) {
                    close();
                } else if (action.equals("Pause")) {
                    testResource.pauseTest();
                    pauseResumeButton.setText("Resume");
                } else if (action.equals("Resume")) {
                    testResource.resumeTest();
                    pauseResumeButton.setText("Pause");
                } else if (action.equals("Stats")) {
                    panel.removeAll();
                    if (dT == null) {
                        dT = new DataTable(testResource);
                        Thread t = new Thread((Runnable) dT);
                        t.setName("DataTable Thread");
                        t.start();
                    }
                    panel.add(dT);
                } else if (action.equals("Connect")) {
                    btdeviceResource.connect(Config.DEVICE_ADDRESS,
                                             Config.DEVICE_TYPE);
                    connectButton.setText("Reconnect");
                    deviceButton.setText("Disconnect");
                } else if (action.equals("Disconnect")) {
                    messageResource.stopRead();
                    connectButton.setText("Connect");
                    hb.stopRead();
                } else if (action.equals("Reconnect")) {
                    messageResource.stopRead();
                    btdeviceResource.connect(Config.DEVICE_ADDRESS,
                                             Config.DEVICE_TYPE);
                } else if (action.equals("Devices")) {
                    btdeviceResource.getDevices();
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
        pauseResumeButton.setPreferredSize(
                new Dimension(menuSize, menuSize / 2));
        pauseResumeButton.addActionListener(menuListener);
        pauseResumeButton.setEnabled(false);
        menu2.add(pauseResumeButton);

        JButton statsButton = new JButton("Stats");
        statsButton.setPreferredSize(new Dimension(menuSize, menuSize));
        //statsButton.setEnabled(false);
        statsButton.addActionListener(menuListener);
        menu2.add(statsButton);

        connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(menuSize, menuSize));
        connectButton.addActionListener(menuListener);
        menu2.add(connectButton);

        deviceButton = new JButton("Devices");
        deviceButton.setPreferredSize(new Dimension(menuSize, menuSize / 2));
        deviceButton.addActionListener(menuListener);
        menu2.add(deviceButton);

        menu.add(menu2);

        menuSeparator = new JSeparator(SwingConstants.VERTICAL);
        menuSeparator.setPreferredSize(new Dimension(2, 450));
        //separator.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        menu.add(menuSeparator);

        frame.add(menu, BorderLayout.WEST);
    }

    private void addLogger() {
        JScrollPane loggerScrollPane = new javax.swing.JScrollPane();
        frame.add(loggerScrollPane, BorderLayout.SOUTH);
        loggerScrollPane.setPreferredSize(
                new Dimension((int) dim.getWidth(), 100));
        //logger.setRows(5);
        logger.setEditable(false);
        loggerScrollPane.setViewportView(logger);

        MessageConsole mc = new MessageConsole(logger);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        //mc.setMessageLines(50);

    }

    private void addWorker() throws IOException {
        JPanel worker = new JPanel();
        frame.add(worker, BorderLayout.CENTER);
//        worker.setBackground(Color.GRAY);
        monitor = new Monitor(testResource);
        monitor.setPreferredSize(
                new Dimension((int) dim.getWidth() - 115, 100));
        monitor.monitorUpdaterStart();
        worker.add(monitor, BorderLayout.NORTH);

        JSeparator separator = new JSeparator();
        separator.setPreferredSize(
                new Dimension((int) dim.getWidth() - 115, 10));
        worker.add(separator, BorderLayout.NORTH);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
//        panel.setBackground(Color.BLUE);
        //panel.setPreferredSize(new Dimension((int)dim.getWidth()-115, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
//        panel.setBackground(Color.red);
        scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        worker.add(scrollPane, BorderLayout.CENTER);
    }

    public MessageResource getMessageResource() {
        return messageResource;
    }

    public BTDeviceResource getBtdeviceResource() {
        return btdeviceResource;
    }

    private void close() {
        monitor.monitorUpdaterStop();
        if (dT != null) {
            dT.stop();
        }
        if (hb != null) {
            hb.stopRead();
        }
        testResource.stopTest();
        messageResource.stopRead();
        monitor.monitorUpdaterStop();
        frame.dispose();
    }

    public void connectionEstabilished() {
        System.out.println("Connection estabilished");
        hb = new HeartBeat();
        isConnectionEstabilished = true;
        Thread t = new Thread((Runnable) hb);
        t.setName("Heart Beat Thread");
        t.start();
    }

    public HeartBeat getHeartBeat() {
        return hb;
    }

    public class HeartBeat implements Runnable {
        private int hr;
        private Message lastMessage;
        private boolean read = true;

        public HeartBeat() {
            hr = 0;
            lastMessage = new Message();
            lastMessage.setTime(System.currentTimeMillis());
            lastMessage.setHr(0);
        }
        @Override
        public void run() {
            while (read) {
                setHR();
                if (hr != 0) {
                    int sleep = 1000 / hr;

                    try {
                        Thread.sleep(40 * sleep);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }

                    monitor.beat(1);

                    try {
                        Thread.sleep(20 * sleep);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }

                    monitor.beat(0);
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        public void setHR() {
            Message message = messageResource.getLastMessage();

            if (message != null) {
                if (message.getTime() == lastMessage.getTime()
                    && System.currentTimeMillis()
                        - lastMessage.getTime() > 3000) {
                    hr = (int) (hr * 0.8);
                    monitor.setHeartRate(hr + "!");
                    connectButton.setText("Reconnect");
                } else {
                    lastMessage = message;
                    hr = lastMessage.getHr();
                    monitor.setHeartRate(String.valueOf(hr));
                }
            }
        }

        public void stopRead() {
            read = false;
            hr = 0;

        }

        public int getHR() {
            return hr;
        }
    }
}
