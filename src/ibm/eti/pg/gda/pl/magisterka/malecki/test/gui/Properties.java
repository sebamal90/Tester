/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author SebaTab
 */
public class Properties extends JDialog {

    private JLabel labLanguage = setLabel("language");
    private JLabel labDeviceName = setLabel("deviceName");
    private JLabel labDeviceAddress = setLabel("deviceAddress");
    private JLabel labDeviceType = setLabel("deviceType");

    public Properties(java.awt.Frame parent, boolean modal){
        super(parent, modal);
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(400, 250));
        setResizable(false);

        add(labLanguage);

        JComboBox langList = new JComboBox(
                Config.labels.getString("Properties.languageList").split(","));
        langList.setPreferredSize(new Dimension(200, 30));
        langList.setSelectedIndex(setLanguage());
        add(langList);

        add(labDeviceType);
        JLabel devType = new JLabel(Config.DEVICE_TYPE);
        devType.setPreferredSize(new Dimension(200, 30));
        add(devType);

        add(labDeviceName);
        JLabel devName = new JLabel(Config.DEVICE_NAME);
        devName.setPreferredSize(new Dimension(200, 30));
        add(devName);

        add(labDeviceAddress);
        JLabel devAddr = new JLabel(Config.DEVICE_ADDRESS);
        devAddr.setPreferredSize(new Dimension(200, 30));
        add(devAddr);

        JLabel freeSpace = new JLabel("");
        freeSpace.setPreferredSize(new Dimension(200, 30));
        add(freeSpace);

        JButton setDev = new JButton(Config.labels.getString("Properties.deviceSelect"));
        setDev.setPreferredSize(new Dimension(150, 30));
        add(setDev);

        JButton cancel = new JButton(Config.labels.getString("Properties.cancel"));
        cancel.setPreferredSize(new Dimension(100, 30));
        add(cancel);

        JButton save = new JButton(Config.labels.getString("Properties.save"));
        save.setPreferredSize(new Dimension(100, 30));
        add(save);


        pack();
        setVisible(true);
    }

    private JLabel setLabel(String txt) {
        JLabel lab = new JLabel(Config.labels.getString("Properties." + txt));
        lab.setPreferredSize(new Dimension(150, 30));
        return lab;
    }

    private int setLanguage() {
        int result = 0;

        if (Config.LOCALE.getLanguage().equalsIgnoreCase("en")) {
            result = 0;
        } else if (Config.LOCALE.getLanguage().equalsIgnoreCase("pl")) {
            result = 1;
        }
        return result;
    }




}
