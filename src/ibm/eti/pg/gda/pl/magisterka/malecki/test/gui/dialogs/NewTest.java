package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.dialogs;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class NewTest extends JDialog {
    private final Main main;

    public NewTest(Main aMain, boolean modal) {
        super(aMain.getFrame(), modal);
        main = aMain;
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(500, 430));
        setSize(new Dimension(500, 430));
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - getWidth()) / 2;
        final int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
        setResizable(false);

        add(new PersonSettings());


        pack();
        setVisible(true);
    }

    private class PersonSettings extends JPanel {
        private JTextField labName = new JTextField();
        private JTextField labSurname = new JTextField();
        private JTextField labBirthDate = new JTextField();
        private JTextField labHeight = new JTextField();
        private JTextField labWeigh = new JTextField();
        private JTextField labFat = new JTextField();
        private JTextField labFatKg = new JTextField();
        private JTextField labFFM = new JTextField();
        private JTextField labFFMKg = new JTextField();
        private JTextField labMM = new JTextField();
        private JTextField labBMI = new JTextField();
        private JTextField labMore = new JTextField();

        public PersonSettings() {
            setPreferredSize(new Dimension(450, 400));
            setLayout(new FlowLayout(FlowLayout.LEFT));

            addLabeli18l("name", 100);
            addField(labName, true, 300);

            addLabeli18l("surname", 100);
            addField(labSurname, true, 300);

            addLabeli18l("birthDate", 100);
            addField(labBirthDate, true, 100);
            addLabel("(yyyy-mm-dd)", 200);

            addLabeli18l("height", 100);
            addField(labHeight, true, 100);
            addLabel("cm", 200);

            addLabeli18l("weight", 100);
            addField(labWeigh, true, 100);
            addLabel("kg", 200);

            addLabeli18l("fat", 100);
            addField(labFat, true, 100);
            addLabel("%", 50);

            JSeparator sep = new JSeparator();
            sep.setPreferredSize(new Dimension(400, 5));
            add(sep);

            addLabeli18l("fat", 100);
            addField(labFatKg, false, 100);
            addLabel("kg", 200);

            addLabeli18l("fatFreeMass", 100);
            addField(labFFM, false, 100);
            addLabel("%", 200);

            addLabeli18l("fatFreeMass", 100);
            addField(labFFMKg, false, 100);
            addLabel("kg", 200);

            JButton valid = new JButton("Validate");
            valid.setPreferredSize(new Dimension(150, 30));
            valid.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    validateData();
                }
            });
            add(valid);

            pack();
        }

        private void addLabeli18l(String txt, int width) {
            JLabel lab = new JLabel(Config.labels.getString("NewTest." + txt));
            lab.setPreferredSize(new Dimension(width, 30));
            add(lab);
        }

        private void addLabel(String txt, int width) {
            JLabel lab = new JLabel(txt);
            lab.setPreferredSize(new Dimension(width, 30));
            add(lab);
        }

        private void addField(JTextField field, boolean editable, int width) {
            field.setEditable(editable);
            field.setPreferredSize(new Dimension(width, 30));
            add(field);
        }

        private boolean validateData() {
            boolean isValid = true;
            labName.setBorder(new LineBorder(Color.GRAY));
            labSurname.setBorder(new LineBorder(Color.GRAY));
            labBirthDate.setBorder(new LineBorder(Color.GRAY));
            labHeight.setBorder(new LineBorder(Color.GRAY));
            labWeigh.setBorder(new LineBorder(Color.GRAY));

            if (labName.getText().equals("")) {
                isValid = false;
                labName.setBorder(new LineBorder(Color.RED));
            }

            if (labSurname.getText().equals("")) {
                isValid = false;
                labSurname.setBorder(new LineBorder(Color.RED));
            }
            if (labBirthDate.getText().equals("")) {
                isValid = false;
                labBirthDate.setBorder(new LineBorder(Color.RED));
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = sdf.parse(labBirthDate.getText());
                } catch (ParseException ex) {
                    isValid = false;
                    labBirthDate.setBorder(new LineBorder(Color.RED));
                }
            }

            if (labHeight.getText().equals("")) {
                isValid = false;
                labHeight.setBorder(new LineBorder(Color.RED));
            } else {
                try {
                    Integer.parseInt(labHeight.getText());
                } catch (NumberFormatException ex) {
                    isValid = false;
                    labHeight.setBorder(new LineBorder(Color.RED));
                }
            }

            if (labWeigh.getText().equals("")) {
                isValid = false;
                labWeigh.setBorder(new LineBorder(Color.RED));
            } else {
                try {
                    Integer.parseInt(labWeigh.getText());
                } catch (NumberFormatException ex) {
                    isValid = false;
                    labWeigh.setBorder(new LineBorder(Color.RED));
                }
            }

            if (!labFat.getText().equals("")) {
                try {
                    Float.parseFloat(labFat.getText());
                } catch (NumberFormatException ex) {
                    isValid = false;
                    labFat.setBorder(new LineBorder(Color.RED));
                }
            }

            return isValid;
        }
    }

    private class ProtocolSettings extends JPanel {

        public ProtocolSettings() {

        }
    }
}
