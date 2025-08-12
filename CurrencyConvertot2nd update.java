import javax.swing.*;
import java.net.*;
import java.io.*;

public class CurrencyConvertot {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Currency Converter (Radio Buttons)");
        frame.setSize(600, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(30, 20, 80, 25);
        frame.add(amountLabel);

        JTextField input = new JTextField();
        input.setBounds(120, 20, 160, 25);
        frame.add(input);

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setBounds(30, 60, 50, 25);
        frame.add(fromLabel);


        JRadioButton bdtFrom = new JRadioButton("BDT");
        bdtFrom.setBounds(80, 60, 60, 25);
        JRadioButton usdFrom = new JRadioButton("USD");
        usdFrom.setBounds(140, 60, 60, 25);
        JRadioButton pkrFrom = new JRadioButton("PKR");
        pkrFrom.setBounds(200, 60, 60, 25);
        JRadioButton inrFrom = new JRadioButton("INR");
        inrFrom.setBounds(260, 60, 60, 25);
        JRadioButton sarFrom = new JRadioButton("SAR");
        sarFrom.setBounds(320, 60, 60, 25);

        ButtonGroup fromGroup = new ButtonGroup();
        fromGroup.add(bdtFrom);
        fromGroup.add(usdFrom);
        fromGroup.add(pkrFrom);
        fromGroup.add(inrFrom);
        fromGroup.add(sarFrom);

        frame.add(bdtFrom);
        frame.add(usdFrom);
        frame.add(pkrFrom);
        frame.add(inrFrom);
        frame.add(sarFrom);

        JLabel toLabel = new JLabel("To:");
        toLabel.setBounds(30, 100, 50, 25);
        frame.add(toLabel);
        JRadioButton bdtTo = new JRadioButton("BDT");
        bdtTo.setBounds(80, 100, 60, 25);
        JRadioButton usdTo = new JRadioButton("USD");
        usdTo.setBounds(140, 100, 60, 25);
        JRadioButton pkrTo = new JRadioButton("PKR");
        pkrTo.setBounds(200, 100, 60, 25);
        JRadioButton inrTo = new JRadioButton("INR");
        inrTo.setBounds(260, 100, 60, 25);
        JRadioButton sarTo = new JRadioButton("SAR");
        sarTo.setBounds(320, 100, 60, 25);

        ButtonGroup toGroup = new ButtonGroup();
        toGroup.add(bdtTo);
        toGroup.add(usdTo);
        toGroup.add(pkrTo);
        toGroup.add(inrTo);
        toGroup.add(sarTo);

        frame.add(bdtTo);
        frame.add(usdTo);
        frame.add(pkrTo);
        frame.add(inrTo);
        frame.add(sarTo);

        JButton button = new JButton("Convert");
        button.setBounds(120, 140, 100, 30);
        frame.add(button);

        JLabel resultLabel = new JLabel("Result:");
        resultLabel.setBounds(30, 190, 520, 25);
        frame.add(resultLabel);

        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(30, 220, 520, 25);
        frame.add(errorLabel);

        button.addActionListener(e -> {
            errorLabel.setText("");
            resultLabel.setText("Result:");
            String text = input.getText().trim();

            if (!text.matches("[0-9.]+")) {
                errorLabel.setText("Invalid amount!");
                return;
            }

            double amt = Double.parseDouble(text);

            String from = null;
            if (bdtFrom.isSelected()) from = "BDT";
            else if (usdFrom.isSelected()) from = "USD";
            else if (pkrFrom.isSelected()) from = "PKR";
            else if (inrFrom.isSelected()) from = "INR";
            else if (sarFrom.isSelected()) from = "SAR";

            String to = null;
            if (bdtTo.isSelected()) to = "BDT";
            else if (usdTo.isSelected()) to = "USD";
            else if (pkrTo.isSelected()) to = "PKR";
            else if (inrTo.isSelected()) to = "INR";
            else if (sarTo.isSelected()) to = "SAR";

            if (from == null || to == null) {
                errorLabel.setText("Please select both From and To currencies!");
                return;
            }

            if (from.equals(to)) {
                errorLabel.setText("From and To currencies must be different!");
                return;
            }

            try {
                String apiUrl = "https://open.er-api.com/v6/latest/" + from;
                String json = readUrl(apiUrl);

                int ratesIndex = json.indexOf("\"rates\"");
                if (ratesIndex == -1) throw new Exception("Rates not found");

                int startIndex = json.indexOf(to + "\":", ratesIndex);
                if (startIndex == -1) throw new Exception(to + " rate not found");

                startIndex += (to.length() + 3);
                int endIndex = json.indexOf(",", startIndex);
                if (endIndex == -1) endIndex = json.indexOf("}", startIndex);

                String rateStr = json.substring(startIndex, endIndex).trim();
                double rate = Double.parseDouble(rateStr);

                double converted = amt * rate;
                resultLabel.setText(String.format("Result: %.2f %s", converted, to));

            } catch (Exception ex) {
                errorLabel.setText("Error fetching rate.");
                ex.printStackTrace();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
