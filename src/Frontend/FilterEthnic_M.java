package Frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class FilterEthnic_M {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/SHOPPING";
        String username = "root";
        String password = "root1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Create the main JFrame
        JFrame frame = new JFrame("Filter Products");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(2,5));
        frame.setSize(800, 600);

        // Create UI components
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.setBackground(Color.decode("#F7B9C4"));

        JCheckBox smallSizeCheckBox = new JCheckBox("Small");
        JCheckBox mediumSizeCheckBox = new JCheckBox("Medium");
        JCheckBox largeSizeCheckBox = new JCheckBox("Large");
        JCheckBox extraLargeSizeCheckBox = new JCheckBox("Extra Large");

        JTextField minPriceField = new JTextField(10);
        JTextField maxPriceField = new JTextField(10);

        JCheckBox redCheckBox = new JCheckBox("Red");
        JCheckBox blueCheckBox = new JCheckBox("Blue");
        JCheckBox greenCheckBox = new JCheckBox("Green");
        JCheckBox blackCheckBox = new JCheckBox("Black");
        JCheckBox yellowCheckBox = new JCheckBox("Yellow");
        JCheckBox pinkCheckBox = new JCheckBox("Pink");
        JCheckBox greyCheckBox = new JCheckBox("Grey");
        JCheckBox navyCheckBox = new JCheckBox("Navy");
        JCheckBox whiteCheckBox = new JCheckBox("White");

        JTable table = new JTable();

        JButton applyFiltersButton = new JButton("Apply Filters");

        // Add components to filter panel
        filterPanel.add(new JLabel("Size:"));
        filterPanel.add(smallSizeCheckBox);
        filterPanel.add(mediumSizeCheckBox);
        filterPanel.add(largeSizeCheckBox);
        filterPanel.add(extraLargeSizeCheckBox);

        filterPanel.add(new JLabel("Price Range:"));
        filterPanel.add(new JLabel("Min:"));
        filterPanel.add(minPriceField);
        filterPanel.add(new JLabel("Max:"));
        filterPanel.add(maxPriceField);

        filterPanel.add(new JLabel("Color:"));
        filterPanel.add(redCheckBox);
        filterPanel.add(blueCheckBox);
        filterPanel.add(greenCheckBox);
        filterPanel.add(blackCheckBox);
        filterPanel.add(yellowCheckBox);
        filterPanel.add(pinkCheckBox);
        filterPanel.add(greyCheckBox);
        filterPanel.add(navyCheckBox);
        filterPanel.add(whiteCheckBox);

        filterPanel.add(applyFiltersButton);

        // Add filter panel to the SOUTH region
        frame.add(filterPanel, BorderLayout.SOUTH);

        // Add the JTable to JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        Connection finalConnection = connection;

        // Action Listener to handle button press
        applyFiltersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Apply filters button clicked");

                boolean isSmall = smallSizeCheckBox.isSelected();
                boolean isMedium = mediumSizeCheckBox.isSelected();
                boolean isLarge = largeSizeCheckBox.isSelected();
                boolean isExtra = extraLargeSizeCheckBox.isSelected();

                String minPrice = minPriceField.getText().trim();
                String maxPrice = maxPriceField.getText().trim();

                boolean isRed = redCheckBox.isSelected();
                boolean isBlue = blueCheckBox.isSelected();
                boolean isGreen = greenCheckBox.isSelected();
                boolean isBlack = blackCheckBox.isSelected();
                boolean isYellow = yellowCheckBox.isSelected();
                boolean isPink = pinkCheckBox.isSelected();
                boolean isGrey = greyCheckBox.isSelected();
                boolean isNavy = navyCheckBox.isSelected();
                boolean isWhite = whiteCheckBox.isSelected();

                filterProducts(finalConnection, table, isSmall, isMedium, isLarge, isExtra, minPrice, maxPrice,
                        isRed, isBlue, isGreen, isBlack, isPink, isYellow, isNavy, isWhite, isGrey);
            }
        });

        // Set frame visible
        frame.setVisible(true);
    }

    public static void filterProducts(Connection connection, JTable table,
                                      boolean isSmall, boolean isMedium, boolean isLarge, boolean isExtra,
                                      String minPrice, String maxPrice,
                                      boolean isRed, boolean isBlue, boolean isGreen, boolean isBlack,
                                      boolean isPink, boolean isYellow, boolean isNavy, boolean isWhite,
                                      boolean isGrey) {
        try {
            String query = "SELECT * FROM Ethnic WHERE Gender = 'M'";
            ArrayList<String> conditions = new ArrayList<>();

            // Add conditions for size
            if (isSmall || isMedium || isLarge || isExtra) {
                ArrayList<String> sizeConditions = new ArrayList<>();
                if (isSmall) sizeConditions.add("Size = 'S'");
                if (isMedium) sizeConditions.add("Size = 'M'");
                if (isLarge) sizeConditions.add("Size = 'L'");
                if (isExtra) sizeConditions.add("Size = 'XL'");
                conditions.add("(" + String.join(" OR ", sizeConditions) + ")");
            }

            // Add conditions for price range
            if (!minPrice.isEmpty()) {
                conditions.add("Price >= " + minPrice);
            }
            if (!maxPrice.isEmpty()) {
                conditions.add("Price <= " + maxPrice);
            }

            // Add conditions for color
            if (isRed || isBlue || isGreen || isBlack || isYellow || isPink || isGrey || isNavy || isWhite) {
                ArrayList<String> colorConditions = new ArrayList<>();
                if (isRed) colorConditions.add("Colour = 'Red'");
                if (isBlue) colorConditions.add("Colour = 'Blue'");
                if (isGreen) colorConditions.add("Colour = 'Green'");
                if (isBlack) colorConditions.add("Colour = 'Black'");
                if (isPink) colorConditions.add("Colour = 'Pink'");
                if (isYellow) colorConditions.add("Colour = 'Yellow'");
                if (isGrey) colorConditions.add("Colour = 'Grey'");
                if (isNavy) colorConditions.add("Colour = 'Navy'");
                if (isWhite) colorConditions.add("Colour = 'White'");
                conditions.add("(" + String.join(" OR ", colorConditions) + ")");
            }

            // Combine conditions into the query
            if (!conditions.isEmpty()) {
                query += " AND " + String.join(" AND ", conditions);
            }
            System.out.println("Generated Query: " + query);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Populate the table
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Vector<Vector<Object>> rowData = new Vector<>();
            Vector<String> columnNames = new Vector<>();

            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i));
                }
                rowData.add(row);
            }

            table.setModel(new DefaultTableModel(rowData, columnNames));
            System.out.println("Rows fetched: " + rowData.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
