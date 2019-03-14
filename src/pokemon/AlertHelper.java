package pokemon;

import javax.swing.*;

public class AlertHelper {

  public static void alert(String message) {
    JOptionPane.showMessageDialog(null, message);
  }

  public static void fatal(String message) {
    System.out.println(message);
    System.exit(0);
  }

  public static void debug(String message) {
    System.out.println(message);
  }
}
