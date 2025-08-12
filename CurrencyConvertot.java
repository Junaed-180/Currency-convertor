import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class CurrencyConvertot {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Currency Converter");
        frame.setSize(600, 320);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImagePanel panel = new ImagePanel("D:\\sumophoto.jpg");
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Color labelColor = new Color(25, 50, 90);

        JLabel fromLabel = new JLabel("From Country:");
        fromLabel.setFont(new Font("Arial", Font.BOLD, 15));
        fromLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panel.add(fromLabel, gbc);

        String[] countries = {"Bangladesh (BDT)", "USA (USD)", "Pakistan (PKR)", "India (INR)", "Saudi Arabia (SAR)"};
        JComboBox<String> fromCombo = new JComboBox<>(countries);
        fromCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        fromCombo.setBackground(new Color(230, 240, 250));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        panel.add(fromCombo, gbc);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 15));
        amountLabel.setForeground(labelColor);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.2;
        panel.add(amountLabel, gbc);

        JTextField amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 13));
        amountField.setBackground(new Color(230, 240, 250));
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.5;
        panel.add(amountField, gbc);

        JLabel toLabel = new JLabel("To Country:");
        toLabel.setFont(new Font("Arial", Font.BOLD, 15));
        toLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panel.add(toLabel, gbc);

        JComboBox<String> toCombo = new JComboBox<>(countries);
        toCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        toCombo.setBackground(new Color(230, 240, 250));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        panel.add(toCombo, gbc);

        JButton convertBtn = new JButton("Convert");
        convertBtn.setFont(new Font("Arial", Font.BOLD, 16));
        convertBtn.setBackground(new Color(0, 123, 255));
        convertBtn.setForeground(Color.WHITE);
        convertBtn.setFocusPainted(false);
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        panel.add(convertBtn, gbc);

        JLabel resultLabel = new JLabel("Result:");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 15));
        resultLabel.setForeground(new Color(0, 100, 0));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        gbc.weightx = 1;
        panel.add(resultLabel, gbc);

        JLabel errorLabel = new JLabel();
        errorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panel.add(errorLabel, gbc);

        frame.setContentPane(panel);
        frame.setVisible(true);

        convertBtn.addActionListener(e -> {
            errorLabel.setText("");
            resultLabel.setText("Result:");

            String amountText = amountField.getText().trim();
            if (!amountText.matches("[0-9.]+")) {
                errorLabel.setText("Invalid amount! Only digits and decimal point allowed.");
                return;
            }

            double amount = Double.parseDouble(amountText);

            String fromSelection = (String) fromCombo.getSelectedItem();
            String toSelection = (String) toCombo.getSelectedItem();

            if (fromSelection.equals(toSelection)) {
                errorLabel.setText("From and To countries cannot be the same.");
                return;
            }

            String fromCode = fromSelection.substring(fromSelection.indexOf("(") + 1, fromSelection.indexOf(")"));
            String toCode = toSelection.substring(toSelection.indexOf("(") + 1, toSelection.indexOf(")"));

            try {
                String apiUrl = "https://api.exchangerate-api.com/v4/latest/USD";
                String json = readUrl(apiUrl);

                int ratesIndex = json.indexOf("\"rates\"");
                if (ratesIndex == -1) throw new Exception("Rates not found");

                double rateFrom = 0, rateTo = 0;
                String baseCurrency = "USD";

                if (fromCode.equals(baseCurrency)) {
                    rateFrom = 1.0;
                } else {
                    int fromIdx = json.indexOf("\"" + fromCode + "\":", ratesIndex);
                    if (fromIdx == -1) throw new Exception(fromCode + " rate not found");
                    fromIdx += fromCode.length() + 3;
                    int fromEnd = json.indexOf(",", fromIdx);
                    if (fromEnd == -1) fromEnd = json.indexOf("}", fromIdx);
                    rateFrom = Double.parseDouble(json.substring(fromIdx, fromEnd).trim());
                }

                if (toCode.equals(baseCurrency)) {
                    rateTo = 1.0;
                } else {
                    int toIdx = json.indexOf("\"" + toCode + "\":", ratesIndex);
                    if (toIdx == -1) throw new Exception(toCode + " rate not found");
                    toIdx += toCode.length() + 3;
                    int toEnd = json.indexOf(",", toIdx);
                    if (toEnd == -1) toEnd = json.indexOf("}", toIdx);
                    rateTo = Double.parseDouble(json.substring(toIdx, toEnd).trim());
                }

                double convertedAmount = amount / rateFrom * rateTo;

                resultLabel.setText(String.format("Result: %.2f %s", convertedAmount, toCode));
            } catch (Exception ex) {
                errorLabel.setText("Error fetching conversion rate. Check your internet connection.");
                ex.printStackTrace();
            }
        });
    }

    static class ImagePanel extends JPanel {
        private Image img;

        public ImagePanel(String imgPath) {
            try {
                img = Toolkit.getDefaultToolkit().getImage(imgPath);
                MediaTracker mt = new MediaTracker(this);
                mt.addImage(img, 0);
                mt.waitForAll();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Image not found: " + imgPath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
