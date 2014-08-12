package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.BTDeviceResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.MessageResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 *
 * @author SebaTab
 */
public class Menu extends JPanel {
    private JButton startStopButton;
    private JButton pauseResumeButton;
    private JButton connectButton;
    private JButton deviceButton;
    private JSeparator menuSeparator;
    private JPanel menuPanel;
    private final Main main;
    private DataTable dT;
    private Graph graph;
    private final JButton statsGraphButton;

    public Menu(Main aMain, final TestResource testResource,
                final BTDeviceResource bTDeviceResource,
                final MessageResource messageResource) {
        this.main = aMain;
        int menuSize = 100;

        setPreferredSize(new Dimension(menuSize + 15, this.getHeight()));

        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if (action.equals("Start")) {
                    if (main.getHeartBeat() == null) {
                        System.out.println("Don't connected with hr Listener");
                    } else {
                        testResource.startTest();
                        startStopButton.setText("End");
                        pauseResumeButton.setEnabled(true);
                    }
                } else if (action.equals("End")) {
                    main.closeWindow();
                } else if (action.equals("Pause")) {
                    testResource.pauseTest();
                    pauseResumeButton.setText("Resume");
                } else if (action.equals("Resume")) {
                    testResource.resumeTest();
                    pauseResumeButton.setText("Pause");
                } else if (action.equals("Stats")) {
                    main.getPanel().removeAll();
                    if (dT == null) {
                        dT = new DataTable(testResource);
                        Thread t = new Thread((Runnable) dT);
                        t.setName("DataTable Thread");
                        t.start();
                    }
                    statsGraphButton.setText("Graph");
                    main.getPanel().add(dT);
                    main.getPanel().updateUI();
                } else if (action.equals("Graph")) {
                    main.getPanel().removeAll();
                    if (graph == null) {
                        graph = new Graph();
                        Thread t = new Thread((Runnable) graph);
                        t.setName("Graph Thread");
                        t.start();
                    }
                    statsGraphButton.setText("Stats");
                    main.getPanel().add(graph);
                    main.getPanel().updateUI();
                } else if (action.equals("Connect")) {
                    bTDeviceResource.connect(Config.DEVICE_ADDRESS,
                                             Config.DEVICE_TYPE);
                    connectButton.setText("Reconnect");
                    deviceButton.setText("Disconnect");
                } else if (action.equals("Disconnect")) {
                    messageResource.stopRead();
                    connectButton.setText("Connect");
                    main.getHeartBeat().stopRead();
                } else if (action.equals("Reconnect")) {
                    messageResource.stopRead();
                    bTDeviceResource.connect(Config.DEVICE_ADDRESS,
                                             Config.DEVICE_TYPE);
                } else if (action.equals("Devices")) {
                    bTDeviceResource.getDevices();
                }
            }
        };

        menuPanel = new JPanel();
//        menu2.setBackground(Color.red);
        menuPanel.setPreferredSize(new Dimension(menuSize, 450));

        startStopButton = new JButton("Start");
        startStopButton.setPreferredSize(new Dimension(menuSize, menuSize));
        startStopButton.addActionListener(menuListener);
        menuPanel.add(startStopButton);

        pauseResumeButton = new JButton("Pause");
        pauseResumeButton.setPreferredSize(
                new Dimension(menuSize, menuSize / 2));
        pauseResumeButton.addActionListener(menuListener);
        pauseResumeButton.setEnabled(false);
        menuPanel.add(pauseResumeButton);

        statsGraphButton = new JButton("Stats");
        statsGraphButton.setPreferredSize(new Dimension(menuSize, menuSize));
        //statsButton.setEnabled(false);
        statsGraphButton.addActionListener(menuListener);
        menuPanel.add(statsGraphButton);

        connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(menuSize, menuSize));
        connectButton.addActionListener(menuListener);
        menuPanel.add(connectButton);

        deviceButton = new JButton("Devices");
        deviceButton.setPreferredSize(new Dimension(menuSize, menuSize / 2));
        deviceButton.addActionListener(menuListener);
        menuPanel.add(deviceButton);

        add(menuPanel);

        menuSeparator = new JSeparator(SwingConstants.VERTICAL);
        menuSeparator.setPreferredSize(new Dimension(2, 450));
        //separator.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        add(menuSeparator);
    }

    @Override
    public void updateUI() {
        setUI((BasicPanelUI) UIManager.getUI(this));
        if (menuPanel != null) {
            menuPanel.setPreferredSize(new Dimension(100, this.getHeight()));
            menuSeparator.setPreferredSize(
                    new Dimension(2, this.getHeight()));
            menuPanel.setSize(new Dimension(100, this.getHeight()));
            menuSeparator.setPreferredSize(
                    new Dimension(2, this.getHeight()));
        }
   }

    public DataTable getDataTable() {
        return dT;
    }
}

