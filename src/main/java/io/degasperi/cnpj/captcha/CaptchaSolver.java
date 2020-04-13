package io.degasperi.cnpj.captcha;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CaptchaSolver {
	public static String solve(byte[] image) {
		final Icon icon = new ImageIcon(image);
		final JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		return (String) JOptionPane.showInputDialog(frame, "Digite os caracteres da imagem", "Captcha", 0, icon, null,
				null);
	}
}
