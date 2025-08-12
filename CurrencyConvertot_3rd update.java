import javax.swing.*;
import java.awt.event.*;

public class CurrencyConvertot {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BDT Currency Converter");
        frame.setSize(400, 350);
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

        JLabel sarLabel = new JLabel("Saudi Riyal (SAR): ");
        sarLabel.setBounds(30, 140, 300, 25);
        frame.add(sarLabel);

        JLabel pkrLabel = new JLabel("Pakistani Rupee (PKR): ");
        pkrLabel.setBounds(30, 170, 300, 25);
        frame.add(pkrLabel);

        JLabel inrLabel = new JLabel("Indian Rupee (INR): ");
        inrLabel.setBounds(30, 200, 300, 25);
        frame.add(inrLabel);

        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(30, 240, 300, 25);
        frame.add(errorLabel);

        final double usdRate = 0.0082;
        final double saudiRate = 0.031;
        final double pakistanRate = 2.33;
        final double indiaRate = 0.72;

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = input.getText();
                if (text.matches("[0-9.]+")) {
                    double bdt = Double.parseDouble(text);
                    double usd = bdt * usdRate;
                    double sar = bdt * saudiRate;
                    double pkr = bdt * pakistanRate;
                    double inr = bdt * indiaRate;

                    usdLabel.setText("USD: " + String.format("%.2f", usd));
                    sarLabel.setText("Saudi Riyal (SAR): " + String.format("%.2f", sar));
                    pkrLabel.setText("Pakistani Rupee (PKR): " + String.format("%.2f", pkr));
                    inrLabel.setText("Indian Rupee (INR): " + String.format("%.2f", inr));
                    errorLabel.setText("");
                } else {
                    errorLabel.setText("Invalid BDT amount!");
                    usdLabel.setText("USD: ");
                    sarLabel.setText("Saudi Riyal (SAR): ");
                    pkrLabel.setText("Pakistani Rupee (PKR): ");
                    inrLabel.setText("Indian Rupee (INR): ");
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

