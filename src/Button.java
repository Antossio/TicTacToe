import java.awt.Color;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Button extends JFrame {
    public int index; //индекс в массиве клеток
    public boolean open;  //открыта ли €чейка
    public JButton but = new JButton();
    public JLabel lab = new JLabel ();

    public Button(int a, int b, int c, int d) {
	but.setBounds(a, b, c, d);
	lab.setBounds(a, b, c, d);
	lab.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
	lab.setHorizontalAlignment(SwingConstants.CENTER);
	lab.setVerticalAlignment(SwingConstants.CENTER);
    }
}
