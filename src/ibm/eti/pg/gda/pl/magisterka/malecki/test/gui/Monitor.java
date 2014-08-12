/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 *
 * @author SebaTab
 */
public class Monitor extends JPanel {
    private final JLabel heartRate;
    private final Image heart1;
    private final Image heart2;
    private final JLabel heart;
    private final JLabel powerLabel;
    private final JLabel timeLabel;
    private final JLabel clockLabel;
    private final TestResource testResource;
    private boolean work;

    public Monitor(TestResource aTestResource) throws IOException {
        super();
        this.testResource = aTestResource;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createEmptyBorder(-5, 5, 0, 0));

        heartRate = new JLabel("0");
        heartRate.setHorizontalAlignment(SwingConstants.RIGHT);
        heartRate.setPreferredSize(new Dimension(170, 100));
        heartRate.setFont(heartRate.getFont().deriveFont(100.0f));
        add(heartRate);
        Image img;
        img = ImageIO.read(getClass().getResource("resources/heart1.png"));
        heart1 = img.getScaledInstance(80, 80, 0);
        img = ImageIO.read(getClass().getResource("resources/heart2.png"));
        heart2 = img.getScaledInstance(80, 80, 0);
        heart = new JLabel(new ImageIcon(heart1));
        heart.setHorizontalAlignment(SwingConstants.CENTER);
        heart.setPreferredSize(new Dimension(100, 100));
        add(heart);

        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        add(separator);

        powerLabel = new JLabel("0W");
        powerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        powerLabel.setPreferredSize(new Dimension(270, 100));
        powerLabel.setFont(powerLabel.getFont().deriveFont(100.0f));
        add(powerLabel);

        separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        add(separator);

        timeLabel = new JLabel("0:00");
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setPreferredSize(new Dimension(285, 100));
        timeLabel.setFont(timeLabel.getFont().deriveFont(100.0f));
        add(timeLabel);

        separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        add(separator);

        SimpleDateFormat ft = new SimpleDateFormat("HH:mm", Config.LOCALE);
        clockLabel = new JLabel(ft.format(new Date()));
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clockLabel.setPreferredSize(new Dimension(285, 100));
        clockLabel.setFont(clockLabel.getFont().deriveFont(100.0f));
        add(clockLabel);

        separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        add(separator);
    }

    public void monitorUpdaterStart() {
        MonitorUpdater mU = new MonitorUpdater(testResource);
        mU.setName("Monitor Updater Thread");
        work = true;
        mU.start();
    }

    public void monitorUpdaterStop() {
        work = false;
    }

    public void beat(int i) {
        if (i == 0) {
            heart.setIcon(new ImageIcon(heart1));
        } else {
            heart.setIcon(new ImageIcon(heart2));

        }
    }

    public void setHeartRate(String hr) {
        heartRate.setText(hr);
    }

    private class MonitorUpdater extends Thread {
        private SimpleDateFormat ft =
                new SimpleDateFormat("HH:mm", Config.LOCALE);
        private TestResource testResource;

        public MonitorUpdater(TestResource aTestResource) {
            super();
            this.testResource = aTestResource;
        }

        @Override
        public void run() {
            int i = 0;
            while (work) {
                clockLabel.setText(ft.format(new Date()));
                if (testResource.isTestStatus()) {
                    timeLabel.setText(testResource.getTimer());
                    powerLabel.setText(i + "W");
                }

                i++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
