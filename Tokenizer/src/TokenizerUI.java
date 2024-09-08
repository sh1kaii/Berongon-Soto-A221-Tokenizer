import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TokenizerUI extends JFrame {

    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton tokenizeButton;
    private List<Token> tokens; // List to store tokens

    public TokenizerUI() {
        // Set up the JFrame
        setTitle("Text Tokenizer");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Input text label and text area
        JLabel inputLabel = new JLabel("Enter text to tokenize:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(inputLabel, gbc);

        inputArea = new JTextArea(8, 50);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(inputScrollPane, gbc);

        // Tokenize button
        tokenizeButton = new JButton("Tokenize");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(tokenizeButton, gbc);

        // Output text label and text area
        JLabel outputLabel = new JLabel("Tokenized output:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(outputLabel, gbc);

        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(outputScrollPane, gbc);

        // Button click event handler
        tokenizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputArea.getText();
                if (inputText.contains("~")) {
                    tokenizeText(inputText);
                    displayTokens();
                } else {
                    outputArea.setText("Error: No delimiter found.");
                }
            }
        });
    }

    // Token class to store token value and its type
    private class Token {
        String value;
        String type;

        Token(String value, String type) {
            this.value = value;
            this.type = type;
        }
    }

    // Method to determine the type of token
    private String getTokenType(String token) {
        token = token.trim();

        if (token.isEmpty()) {
            return null; // Skip empty tokens
        } else if (token.matches("[a-zA-Z]+")) {
            return "Word";
        } else if (token.matches("\\d+(\\.\\d+)?")) {
            return "Number";
        } else if (token.matches("[.,!?;:-]")) {
            return "Punctuation";
        } else if (token.matches("[a-zA-Z0-9]+")) {
            return "Alphanumeric";
        } else if (token.equals("\n")) {
            return "End of Line";
        } else {
            return null; // Skip tokens that don't match any type
        }
    }

    // Tokenize the input text by splitting at delimiter and processing each segment
    private void tokenizeText(String text) {
        tokens = new ArrayList<>();
        String[] rawTokens = text.split("~");

        for (String segment : rawTokens) {
            segment = segment.trim();
            if (segment.length() > 0) {
                // Split segment into tokens while keeping punctuation
                String[] parts = segment.split("(?<=\\p{P})|(?=\\p{P})|\\s+");
                for (String part : parts) {
                    part = part.trim();
                    if (!part.isEmpty()) {
                        String type = getTokenType(part);
                        if (type != null) {
                            tokens.add(new Token(part, type));
                        }
                    }
                }
            }
        }
    }

    // Display the tokens and their granular breakdown
    private void displayTokens() {
        StringBuilder output = new StringBuilder();
        output.append("Phase 1 Output:\n");

        for (Token token : tokens) {
            output.append("Token: \"").append(token.value).append("\" - Type: ").append(token.type).append("\n");
        }

        output.append("===================================================\n");
        output.append("Phase 2 Output (Granular Breakdown):\n");

        for (Token token : tokens) {
            output.append("Token: \"").append(token.value).append("\" -> ");
            for (char ch : token.value.toCharArray()) {
                output.append("'").append(ch).append("', ");
            }
            if (token.value.length() > 0) {
                output.setLength(output.length() - 2); // Remove trailing comma and space
            }
            output.append("\n");
        }

        outputArea.setText(output.toString());
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TokenizerUI().setVisible(true);
            }
        });
    }
}