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
    private JButton disconnectButton;
    private JSeparator menuSeparator;
    private JPanel menuPanel;
    private final Main main;
    private DataTable dataTable;
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
            public void actionPerformed(ActionEvent event) {
                String action = event.getActionCommand();
                if (action.equals(Config.labels.getString("Menu.start"))) {
                    if (!main.getMessageResource().isReading()) {
                        System.out.println("Don't connected with hr Listener");
                    } else {
                        testResource.startTest();
                        startStopButton.setText(Config.labels.getString("Menu.stop"));
                        pauseResumeButton.setEnabled(true);
                    }
                } else if (action.equals(Config.labels.getString("Menu.stop"))) {
                    testResource.stopTest();
                    startStopButton.setText(Config.labels.getString("Menu.end"));
                } else if (action.equals(Config.labels.getString("Menu.end"))) {
                    testResource.endTest();
                    startStopButton.setEnabled(false);
                } else if (action.equals(Config.labels.getString("Menu.pause"))) {
                    testResource.pauseTest();
                    pauseResumeButton.setText(Config.labels.getString("Menu.resume"));
                } else if (action.equals(Config.labels.getString("Menu.resume"))) {
                    testResource.resumeTest();
                    pauseResumeButton.setText(Config.labels.getString("Menu.pause"));
                } else if (action.equals(Config.labels.getString("Menu.stats"))) {
                    main.getPanel().removeAll();
                    if (dataTable == null) {
                        dataTable = new DataTable(testResource);
                        Thread thread = new Thread((Runnable) dataTable);
                        thread.setName("DataTable Thread");
                        thread.start();
                    }
                    statsGraphButton.setText(Config.labels.getString("Menu.graph"));
                    main.getPanel().add(dataTable);
                    main.getPanel().updateUI();
                } else if (action.equals(Config.labels.getString("Menu.graph"))) {
                    main.getPanel().removeAll();
                    if (graph == null) {
                        graph = new Graph(main);
                        Thread thread = new Thread((Runnable) graph);
                        thread.setName("Graph Thread");
                        thread.start();
                    }
                    statsGraphButton.setText(Config.labels.getString("Menu.stats"));
                    main.getPanel().add(graph);
                    main.getPanel().updateUI();
                } else if (action.equals(Config.labels.getString("Menu.connect"))) {
                    if (Config.DEVICE_ADDRESS == null) {
                        System.out.println("Select device");
                    } else {
                        bTDeviceResource.connect(Config.DEVICE_ADDRESS,
                                             Config.DEVICE_TYPE);
                        connectButton.setEnabled(false);
                    }
                } else if (action.equals(Config.labels.getString("Menu.disconnect"))) {
                    messageResource.stopRead();
                    connectButton.setText(Config.labels.getString("Menu.connect"));
                    disconnectButton.setEnabled(false);
                    main.getMonitor().stopBeat();

                } else if (action.equals(Config.labels.getString("Menu.reconnect"))) {
                    messageResource.stopRead();
                    bTDeviceResource.connect(Config.DEVICE_ADDRESS,
                                             Config.DEVICE_TYPE);
                }
            }
        };

        menuPanel = new JPanel();
//        menu2.setBackground(Color.red);
        menuPanel.setPreferredSize(new Dimension(menuSize, 450));

        startStopButton = new JButton(Config.labels.getString("Menu.start"));
        startStopButton.setPreferredSize(new Dimension(menuSize, menuSize));
        startStopButton.addActionListener(menuListener);
        menuPanel.add(startStopButton);

        pauseResumeButton = new JButton(Config.labels.getString("Menu.pause"));
        pauseResumeButton.setPreferredSize(
                new Dimension(menuSize, menuSize / 2));
        pauseResumeButton.addActionListener(menuListener);
        pauseResumeButton.setEnabled(false);
        menuPanel.add(pauseResumeButton);

        statsGraphButton = new JButton(Config.labels.getString("Menu.stats"));
        statsGraphButton.setPreferredSize(new Dimension(menuSize, menuSize));
        statsGraphButton.addActionListener(menuListener);
        menuPanel.add(statsGraphButton);

        connectButton = new JButton(Config.labels.getString("Menu.connect"));
        connectButton.setPreferredSize(new Dimension(menuSize, menuSize));
        connectButton.addActionListener(menuListener);
        menuPanel.add(connectButton);

        disconnectButton = new JButton(Config.labels.getString("Menu.disconnect"));
        disconnectButton.setPreferredSize(new Dimension(menuSize, menuSize / 2));
        disconnectButton.addActionListener(menuListener);
        disconnectButton.setEnabled(false);
        menuPanel.add(disconnectButton);

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
        return dataTable;
    }

    public Graph getGraph() {
        return graph;
    }

    public void connectionEstabilished() {
        connectButton.setText(Config.labels.getString("Menu.reconnect"));
        disconnectButton.setEnabled(true);
        connectButton.setEnabled(true);
    }

    public void connectionFailure() {
        connectButton.setEnabled(true);
    }
}
