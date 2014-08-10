/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
    private JPanel menu;
    private final Main main;
    private DataTable dT;

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
                    if (main.getHeartBeat() != null) {
                        testResource.startTest();
                        startStopButton.setText("End");
                        pauseResumeButton.setEnabled(true);
                    } else {
                        System.out.println("Don't connected with hr Listener");
                    }
                } else if (action.equals("End")) {
                    main.close();
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
                    main.getPanel().add(dT);
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

        menu = new JPanel();
//        menu2.setBackground(Color.red);
        menu.setPreferredSize(new Dimension(menuSize, 450));

        startStopButton = new JButton("Start");
        startStopButton.setPreferredSize(new Dimension(menuSize, menuSize));
        startStopButton.addActionListener(menuListener);
        menu.add(startStopButton);

        pauseResumeButton = new JButton("Pause");
        pauseResumeButton.setPreferredSize(
                new Dimension(menuSize, menuSize / 2));
        pauseResumeButton.addActionListener(menuListener);
        pauseResumeButton.setEnabled(false);
        menu.add(pauseResumeButton);

        JButton statsButton = new JButton("Stats");
        statsButton.setPreferredSize(new Dimension(menuSize, menuSize));
        //statsButton.setEnabled(false);
        statsButton.addActionListener(menuListener);
        menu.add(statsButton);

        connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(menuSize, menuSize));
        connectButton.addActionListener(menuListener);
        menu.add(connectButton);

        deviceButton = new JButton("Devices");
        deviceButton.setPreferredSize(new Dimension(menuSize, menuSize / 2));
        deviceButton.addActionListener(menuListener);
        menu.add(deviceButton);

        add(menu);

        menuSeparator = new JSeparator(SwingConstants.VERTICAL);
        menuSeparator.setPreferredSize(new Dimension(2, 450));
        //separator.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        add(menuSeparator);
    }

    @Override
    public void updateUI() {
        setUI((BasicPanelUI)UIManager.getUI(this));
        if (menu != null) {
            menu.setPreferredSize(new Dimension(100, this.getHeight()));
            menuSeparator.setPreferredSize(
                    new Dimension(2, this.getHeight()));
            menu.setSize(new Dimension(100, this.getHeight()));
            menuSeparator.setPreferredSize(
                    new Dimension(2, this.getHeight()));
        }
   }

    public DataTable getDataTable() {
        return dT;
    }
}
