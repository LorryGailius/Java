// Biblioteka
// Faile duotuoju vardu saugomi knygu aprašai, kiekvienoje eilutėje po vieną knygą pavidalu: Autorius | Pavadinimas | Metai
// Perskaityti failą ir išvesti į ekraną knygų pavadinimus, kurias parašė duotasis autorius. Naudoti java.io.BufferedReader.readline() metodą, java.io.FileReader klasę.
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainFrame extends JFrame implements ActionListener {
    private JButton chooseFile;
    private JTextField search;
    private JTable table;
    private JLabel text;
    private JPanel AllFrame;
    private JPanel UIFrame;
    private JScrollPane TableFrame;
    private JButton button1;
    public String filePath;
    private String Buffer;
    public ArrayList<Book> library = new ArrayList<>();
    private String[] CollumnNames = {"Author", "Title", "Year published"};
    private DefaultTableModel model;

    public MainFrame() {

        // Set Jframe
        this.setTitle("Library");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Model used for the table shown
        model = new DefaultTableModel(CollumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Button
        chooseFile.setText("Select File...");
        chooseFile.setFocusable(false);
        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == chooseFile) {
                    try {
                        readLibrary();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Adds placeholder to search bar
        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                    search.setForeground(Color.black);
                if (search.getText().equals("Enter search term")) {
                    search.setText(null);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (search.getText().equals("")) {
                    search.setText("Enter search term");
                    search.setForeground(Color.gray);
                }
            }
        });

        // TextArea for searching books and authors
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String searchTerm = search.getText();
                filterTable(searchTerm);
            }
        });

        // Disabling reordering and setting the new model of table contents
        table.getTableHeader().setReorderingAllowed(false);
        table.setModel(model);

        //Jframe operations
        this.setPreferredSize(new Dimension(750, 400));
        this.add(AllFrame); //AllFrame consists of UIFrame("Reading from file"), TableFrame("Table of contents")
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    // Reads book data from file
    private void readLibrary() throws IOException {
        //Open a new instance of fileChooser for opening the lib.txt file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        int isfileChosen = fileChooser.showOpenDialog(null);

        if (isfileChosen == JFileChooser.APPROVE_OPTION) {
            filePath = String.valueOf(new File(fileChooser.getSelectedFile().getAbsolutePath()));

            if (filePath.endsWith(".txt") || filePath.endsWith(".in")) {
                // Save filePath and read the file
                FileReader reader = null;
                BufferedReader bufferRead = null;
                clearTable(false);
                Charset utf8 = StandardCharsets.UTF_8;
                // Opens the file for reading (does not compile without catching Exception)
                try {
                    reader = new FileReader(filePath, utf8);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                bufferRead = new BufferedReader(reader);

                while (true) {
                    try {
                        if (!bufferRead.ready()) break;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        // Read one line at a time
                        Buffer = bufferRead.readLine();
                        text.setText("Reading from: " + filePath); // Show the user the filepath
                        Book temp = new Book("", "", "");

                        String[] tokenized = Buffer.split("\\|");
                        // Each line in the file is split by a delimiter '|' and saves into ArrayList
                        if (bufferCheck(tokenized)) {
                            temp.Author = tokenized[0];
                            temp.Title = tokenized[1];
                            temp.Year = tokenized[2];
                            library.add(temp);
                        } else if (library.size() == 0) {
                            text.setText("File Error: No data could be collected");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                // Add all the read components to the table
                for (int j = 0; j < library.size(); j++) {
                    Object[] temp = {library.get(j).Author, library.get(j).Title, library.get(j).Year};
                    model.addRow(temp);
                }
                reader.close();
            } else {
                text.setText("File Error: Specified file is of unknown format");
            }
        }
    }

    // Clears all the info in table and search bar
    private void clearTable(boolean clearAll) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
                if (clearAll) {library.remove(0);}
        }
        search.setText(null);
        filterTable("");
    }

    // Check if buffer contains Author, Title and Year
    private boolean bufferCheck(String[] buffer) {
        if (buffer.length != 3) {
            return false;
        } else {
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i].length() <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Filters the table by a given string
    private void filterTable(String filter) {
        DefaultTableModel tModel = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(tModel) {
            @Override
            public boolean isSortable(int column) {  // Removes sorting by clicking on headers
                return getRowFilter() == null;
            }
        };
        table.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + filter)); // "(?i)" is used for case insensitive compare
    }

}
