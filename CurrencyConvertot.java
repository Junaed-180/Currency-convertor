import javax.swing.*;
import java.awt.event.*;

public class CurrencyConvertot {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BDT to USD Converter");
        frame.setSize(350, 200);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Enter BDT:");
        label.setBounds(30, 20, 100, 25);
        frame.add(label);

        JTextField input = new JTextField();
        input.setBounds(130, 20, 160, 25);
        frame.add(input);

        JButton button = new JButton("Convert");
        button.setBounds(130, 60, 100, 30);
        frame.add(button);

        JLabel usdLabel = new JLabel("USD: ");
        usdLabel.setBounds(30, 110, 300, 25);
        frame.add(usdLabel);

        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(30, 140, 300, 25);
        frame.add(errorLabel);

        final double usdRate = 0.0082;

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = input.getText();
                if (text.matches("[0-9.]+")) {
                    double bdt = Double.parseDouble(text);
                    double usd = bdt * usdRate;
                    usdLabel.setText("USD: " + String.format("%.2f", usd));
                    errorLabel.setText("");
                } else {
                    errorLabel.setText("Invalid BDT amount!");
                    usdLabel.setText("USD: ");
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

