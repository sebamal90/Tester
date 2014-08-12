/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.BTDeviceResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.MessageResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
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
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;

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
    private Menu menu;
    private JScrollPane scrollPane;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new Main();
    }

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
        frame.getContentPane().
                addHierarchyBoundsListener(new HierarchyBoundsListener() {
            @Override
            public void ancestorMoved(HierarchyEvent e) {
                //not used
            }

            @Override
            public void ancestorResized(HierarchyEvent e) {
                menu.updateUI();
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
        menu = new Menu(this, testResource, btdeviceResource, messageResource);
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

    public TestResource getTestResource() {
        return testResource;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void close() {
        monitor.monitorUpdaterStop();
        if (menu.getDataTable() != null) {
            menu.getDataTable().stop();
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
