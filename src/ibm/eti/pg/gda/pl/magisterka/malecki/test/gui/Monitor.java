/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.MessageResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.Message;
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
    private final MessageResource messageResource;
    private HeartBeat heartBeat;

    public Monitor(TestResource aTestResource,
            MessageResource aMessageResource) throws IOException {
        super();
        this.testResource = aTestResource;
        this.messageResource = aMessageResource;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createEmptyBorder(-5, 5, 0, 0));

        heartRate = new JLabel("0");
        heartRate.setHorizontalAlignment(SwingConstants.RIGHT);
        heartRate.setPreferredSize(new Dimension(200, 100));
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
        timeLabel.setPreferredSize(new Dimension(270, 100));
        timeLabel.setFont(timeLabel.getFont().deriveFont(100.0f));
        add(timeLabel);

        separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        add(separator);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Config.LOCALE);
        clockLabel = new JLabel(dateFormat.format(new Date()));
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clockLabel.setPreferredSize(new Dimension(270, 100));
        clockLabel.setFont(clockLabel.getFont().deriveFont(100.0f));
        add(clockLabel);

        separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        add(separator);
    }

    public void monitorUpdaterStart() {
        MonitorUpdater monitorUp = new MonitorUpdater(testResource);
        monitorUp.setName("Monitor Updater Thread");
        work = true;
        monitorUp.start();
    }

    public void monitorUpdaterStop() {
        work = false;
    }

    public void beat(int beat) {
        if (beat == 0) {
            heart.setIcon(new ImageIcon(heart1));
        } else {
            heart.setIcon(new ImageIcon(heart2));

        }
    }

    public void setHeartRate(String hrString) {
        heartRate.setText(hrString);
    }

    public void startBeat() {
        heartBeat = new HeartBeat();
        heartBeat.setName("Heart Beat Thread");
        heartBeat.start();
    }

    public void stopBeat() {
        heartBeat.stopBeat();
    }

    private class MonitorUpdater extends Thread {
        private SimpleDateFormat dateFormat =
                new SimpleDateFormat("HH:mm", Config.LOCALE);
        private TestResource testResource;

        public MonitorUpdater(TestResource aTestResource) {
            super();
            this.testResource = aTestResource;
        }

        @Override
        public void run() {
            while (work) {
                clockLabel.setText(dateFormat.format(new Date()));
                if (messageResource.isReading()) {
                    heartRate.setText(String.valueOf(messageResource.getHR()));
                    if (!messageResource.isHRTimeValid()) {
                        heartRate.setText(heartRate.getText() + "!");
                    }
                }
                if (testResource.isTestStatus()) {
                    timeLabel.setText(testResource.getTimer());
                    powerLabel.setText(testResource.getLoad() + "W");
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public class HeartBeat extends Thread {
        private int heartRate;
        private boolean beatWork = true;

        @Override
        public void run() {
            while (beatWork) {
                heartRate = messageResource.getHR();
                if (heartRate != 0) {
                    int sleep = 1000 / heartRate;

                    try {
                        Thread.sleep(40 * sleep);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }

                    beat(1);

                    try {
                        Thread.sleep(20 * sleep);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }

                    beat(0);
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

        public void stopBeat() {
            beatWork = false;
            heartRate = 0;
        }
    }
}
