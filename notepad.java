import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Notepad Application
 * A simple text editor built using Java Swing.
 * Features: New, Open, Save, and basic text editing.
 */
public class notepad extends JFrame implements ActionListener {
    // Text area for editing
    JTextArea textArea;
    // Status bar for line/column display
    JLabel statusBar;
    // Current file path
    String currentFilePath = null;

    // Menus declared at class level for accessibility
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, formatMenu, viewMenu, helpMenu;
    JCheckBoxMenuItem statusBarItem;

    public notepad() {
        // Set up the frame
        setTitle("Notepad");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create text area
        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Create menu bar and menus
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        formatMenu = new JMenu("Format");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Add action listeners
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        // Add items to menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Create additional menus
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo"); // Placeholder, not implemented
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem selectAllItem = new JMenuItem("Select All");
        cutItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);
        selectAllItem.addActionListener(this);
        editMenu.add(undoItem); // No undo implementation
        editMenu.addSeparator();
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(selectAllItem);

        JMenu formatMenu = new JMenu("Format");
        JCheckBoxMenuItem wordWrapItem = new JCheckBoxMenuItem("Word Wrap");
        wordWrapItem.setSelected(true);
        wordWrapItem.addActionListener(this);
        JMenuItem fontItem = new JMenuItem("Font...");
        fontItem.addActionListener(this);
        formatMenu.add(wordWrapItem);
        formatMenu.add(fontItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem zoomInItem = new JMenuItem("Zoom In");
        JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
        JMenuItem resetZoomItem = new JMenuItem("Restore Default Zoom");
        zoomInItem.addActionListener(this);
        zoomOutItem.addActionListener(this);
        resetZoomItem.addActionListener(this);
        viewMenu.removeAll(); // Clear previous items
        viewMenu.add(zoomInItem);
        viewMenu.add(zoomOutItem);
        viewMenu.add(resetZoomItem);
        viewMenu.addSeparator();
        JCheckBoxMenuItem statusBarItem = new JCheckBoxMenuItem("Status Bar");
        statusBarItem.setSelected(true);
        statusBarItem.addActionListener(this);
        viewMenu.add(statusBarItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);

        // Add all menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Enhanced File menu
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem pageSetupItem = new JMenuItem("Page Setup"); // Placeholder
        JMenuItem printItem = new JMenuItem("Print"); // Placeholder
        saveAsItem.addActionListener(this);
        pageSetupItem.addActionListener(this);
        printItem.addActionListener(this);
        fileMenu.insert(saveAsItem, 3); // After Save
        fileMenu.insert(pageSetupItem, 4);
        fileMenu.insert(printItem, 5);

        // Enhanced Edit menu
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem findItem = new JMenuItem("Find...");
        JMenuItem findNextItem = new JMenuItem("Find Next");
        JMenuItem replaceItem = new JMenuItem("Replace...");
        JMenuItem goToItem = new JMenuItem("Go To...");
        JMenuItem timeDateItem = new JMenuItem("Time/Date");
        deleteItem.addActionListener(this);
        findItem.addActionListener(this);
        findNextItem.addActionListener(this);
        replaceItem.addActionListener(this);
        goToItem.addActionListener(this);
        timeDateItem.addActionListener(this);
        editMenu.add(deleteItem);
        editMenu.addSeparator();
        editMenu.add(findItem);
        editMenu.add(findNextItem);
        editMenu.add(replaceItem);
        editMenu.add(goToItem);
        editMenu.addSeparator();
        editMenu.add(timeDateItem);

        // Enhanced View menu
        // Only declare zoom menu items once
        viewMenu.removeAll();
        viewMenu.add(zoomInItem);
        viewMenu.add(zoomOutItem);
        viewMenu.add(resetZoomItem);
        viewMenu.addSeparator();
        viewMenu.add(statusBarItem);

        // Enhanced Help menu
        JMenuItem viewHelpItem = new JMenuItem("View Help");
        JMenuItem aboutNotepadItem = new JMenuItem("About Notepad");
        viewHelpItem.addActionListener(this);
        aboutNotepadItem.addActionListener(this);
        helpMenu.removeAll();
        helpMenu.add(viewHelpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutNotepadItem);
        // Add all menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Status bar implementation
        statusBar = new JLabel("Ln 1, Col 1");
        add(statusBar, BorderLayout.SOUTH);
        textArea.addCaretListener(e -> {
            try {
                int pos = textArea.getCaretPosition();
                int line = textArea.getLineOfOffset(pos) + 1;
                int col = pos - textArea.getLineStartOffset(line - 1) + 1;
                statusBar.setText("Ln " + line + ", Col " + col);
            } catch (Exception ex) {
                statusBar.setText("");
            }
        });
    }

    /**
     * Handles menu actions
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "New":
                textArea.setText("");
                currentFilePath = null;
                setTitle("Notepad");
                break;
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Cut":
                textArea.cut();
                break;
            case "Copy":
                textArea.copy();
                break;
            case "Paste":
                textArea.paste();
                break;
            case "Select All":
                textArea.selectAll();
                break;
            case "Word Wrap":
                textArea.setLineWrap(!textArea.getLineWrap());
                break;
            case "Font...":
                // Show font chooser dialog
                Font currentFont = textArea.getFont();
                Font newFont = JFontChooser.showDialog(this, "Choose Font", currentFont);
                if (newFont != null) textArea.setFont(newFont);
                break;
            case "Zoom In":
                textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() + 2.0f));
                break;
            case "Zoom Out":
                textArea.setFont(textArea.getFont().deriveFont(Math.max(8.0f, textArea.getFont().getSize() - 2.0f)));
                break;
            case "Restore Default Zoom":
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                break;
            case "About":
                JOptionPane.showMessageDialog(this, "Notepad\nA simple text editor built in Java.", "About", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Save As":
                saveAsFile();
                break;
            case "Page Setup":
                JOptionPane.showMessageDialog(this, "Page Setup is not implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Print":
                try {
                    textArea.print();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error printing.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "Delete":
                textArea.replaceSelection("");
                break;
            case "Find...":
                findText();
                break;
            case "Find Next":
                findNextText();
                break;
            case "Replace...":
                replaceText();
                break;
            case "Go To...":
                goToLine();
                break;
            case "Time/Date":
                textArea.insert(new java.util.Date().toString(), textArea.getCaretPosition());
                break;
            case "Status Bar":
                statusBar.setVisible(!statusBar.isVisible());
                break;
            case "View Help":
                JOptionPane.showMessageDialog(this, "This is a simple Notepad application.\nFor help, visit: https://support.microsoft.com/en-us/windows/notepad-help", "View Help", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "About Notepad":
                JOptionPane.showMessageDialog(this, "Notepad\nVersion 1.0\nJava Swing Edition", "About Notepad", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    /**
     * Opens a text file and loads its content into the text area
     */
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
                currentFilePath = file.getAbsolutePath();
                setTitle("Notepad - " + file.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Saves the content of the text area to a file
     */
    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (currentFilePath != null) {
            fileChooser.setSelectedFile(new File(currentFilePath));
        }
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
                currentFilePath = file.getAbsolutePath();
                setTitle("Notepad - " + file.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Save As functionality
     */
    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
                currentFilePath = file.getAbsolutePath();
                setTitle("Notepad - " + file.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Find text dialog
     */
    private String lastSearch = "";
    private void findText() {
        String input = JOptionPane.showInputDialog(this, "Find:", lastSearch);
        if (input != null && !input.isEmpty()) {
            lastSearch = input;
            int pos = textArea.getText().indexOf(input, textArea.getCaretPosition());
            if (pos >= 0) {
                textArea.setCaretPosition(pos);
                textArea.select(pos, pos + input.length());
            } else {
                JOptionPane.showMessageDialog(this, "Text not found.", "Find", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    private void findNextText() {
        if (lastSearch != null && !lastSearch.isEmpty()) {
            int pos = textArea.getText().indexOf(lastSearch, textArea.getCaretPosition() + 1);
            if (pos >= 0) {
                textArea.setCaretPosition(pos);
                textArea.select(pos, pos + lastSearch.length());
            } else {
                JOptionPane.showMessageDialog(this, "No further matches.", "Find Next", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    /**
     * Replace text dialog
     */
    private void replaceText() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField findField = new JTextField(lastSearch);
        JTextField replaceField = new JTextField();
        panel.add(new JLabel("Find:"));
        panel.add(findField);
        panel.add(new JLabel("Replace with:"));
        panel.add(replaceField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Replace", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String find = findField.getText();
            String replace = replaceField.getText();
            String text = textArea.getText();
            textArea.setText(text.replace(find, replace));
        }
    }
    /**
     * Go To line dialog
     */
    private void goToLine() {
        String input = JOptionPane.showInputDialog(this, "Go to line:");
        if (input != null) {
            try {
                int line = Integer.parseInt(input);
                int pos = textArea.getLineStartOffset(line - 1);
                textArea.setCaretPosition(pos);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid line number.", "Go To", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            notepad notepad = new notepad();
            notepad.setVisible(true);
        });
    }
}

/**
 * Utility class for font selection dialog
 */
class JFontChooser {
    public static Font showDialog(Component parent, String title, Font initialFont) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> fontBox = new JComboBox<>(fonts);
        fontBox.setSelectedItem(initialFont.getFamily());
        JComboBox<Integer> sizeBox = new JComboBox<>(new Integer[]{8, 10, 12, 14, 16, 18, 20, 24, 28, 32});
        sizeBox.setSelectedItem(initialFont.getSize());
        JButton okButton = new JButton("OK");
        final Font[] selectedFont = {initialFont};
        okButton.addActionListener(e -> {
            selectedFont[0] = new Font((String) fontBox.getSelectedItem(), Font.PLAIN, (Integer) sizeBox.getSelectedItem());
            dialog.dispose();
        });
        JPanel panel = new JPanel();
        panel.add(new JLabel("Font:"));
        panel.add(fontBox);
        panel.add(new JLabel("Size:"));
        panel.add(sizeBox);
        panel.add(okButton);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return selectedFont[0];
    }
}
