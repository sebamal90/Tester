/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.dialogs.Properties;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.BTDeviceResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.MessageResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.MessageConsole;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.Message;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.dialogs.NewTest;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

/**
 *
 * @author SebaTab
 */
public class Main {
    private static Main main;
    private JFrame frame;
    private MessageResource messageResource = new MessageResource();
    private BTDeviceResource btdeviceResource = new BTDeviceResource(this);
    private TestResource testResource = new TestResource(this);
    private JTextPane logger = new javax.swing.JTextPane();
    private Monitor monitor;
    private JPanel panel;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private Menu menu;
    private JScrollPane scrollPane;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Config.saveConfig();
        Config.loadConfig();
        main = new Main();
    }

    public Main() throws IOException {
        frame = new JFrame(Config.labels.getString("Main.title"));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
            public void ancestorMoved(HierarchyEvent event) {
                //not used
            }

            @Override
            public void ancestorResized(HierarchyEvent evnt) {
                menu.updateUI();
                monitor.setPreferredSize(
                        new Dimension(frame.getWidth() - 115, 100));
                scrollPane.setPreferredSize(
                        new Dimension(frame.getWidth() - 130,
                                      frame.getHeight() - 300));
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
              closeApp();
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
            public void actionPerformed(ActionEvent event) {
                String action = event.getActionCommand();
                if (action.equals(Config.labels.getString("Main.exit"))) {
                    closeApp();
                } else if (action.equals(Config.labels.getString("Main.properties"))) {
                    new Properties(main, true);
                } else if (action.equals(Config.labels.getString("Main.new"))) {
                    new NewTest(main, true);
                }
            }
        };

        JMenu fileMenu = new JMenu(Config.labels.getString("Main.file"));
        menuBar.add(fileMenu);
        JMenuItem newItem = fileMenu.add(Config.labels.getString("Main.new"));
        newItem.addActionListener(menuBarListener);
        JMenuItem propertiesItem = fileMenu.add(Config.labels.getString("Main.properties"));
        propertiesItem.addActionListener(menuBarListener);
        fileMenu.addSeparator();
        JMenuItem exitItem = fileMenu.add(Config.labels.getString("Main.exit"));
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

        MessageConsole messageConsol = new MessageConsole(logger);
        messageConsol.redirectOut();
        messageConsol.redirectErr(Color.RED, null);
    }

    private void addWorker() throws IOException {
        JPanel worker = new JPanel();
        frame.add(worker, BorderLayout.CENTER);
//        worker.setBackground(Color.GRAY);
        monitor = new Monitor(testResource, messageResource);
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

    public JFrame getFrame() {
        return frame;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void closeApp() {
        int confirm = JOptionPane.showOptionDialog(null,
                Config.labels.getString("Main.exitConfirm"), null,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                Config.labels.getString("Main.exitConfirmOpt").split(","), null);
        if (confirm == 0) {
            monitor.monitorUpdaterStop();
            if (menu.getDataTable() != null) {
                menu.getDataTable().stop();
            }
            if (menu.getGraph()  != null) {
                menu.getGraph().stop();
            }
            testResource.endTest();
            messageResource.stopRead();
            monitor.monitorUpdaterStop();
            frame.dispose();
            //System.exit(0);
        }
    }

    public void connectionEstabilished() {
        System.out.println("Connection estabilished");
        menu.connectionEstabilished();
        monitor.startBeat();
    }

    public void connectionFailure() {
        menu.connectionFailure();
    }
}
