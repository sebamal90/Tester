import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressBar {

  public static void main(String args[]) {
    JFrame frame = new JFrame("Stepping Progress");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final JProgressBar aJProgressBar = new JProgressBar();
    aJProgressBar.setIndeterminate(true);

    frame.add(aJProgressBar, BorderLayout.NORTH);
    frame.setSize(300, 200);
    frame.setVisible(true);
  }
}