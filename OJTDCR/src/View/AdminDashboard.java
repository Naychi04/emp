package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Controller.AdminDashboardController;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboard extends JPanel {
    private JTextField txtNAndD;
    private AdminDashboardController controller;
    private JPanel gridpanel;

    public AdminDashboard() {
        setLayout(null);
        controller = new AdminDashboardController();

        JLabel lbAD = new JLabel("Admin Dashboard");
        lbAD.setFont(new Font("Arial", Font.BOLD, 22));
        lbAD.setBounds(23, 20, 282, 32);
        add(lbAD);

        txtNAndD = new JTextField();
        txtNAndD.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNAndD.setBounds(61, 68, 448, 32);
        add(txtNAndD);
        txtNAndD.setColumns(10);
        setPlaceholder(txtNAndD, "Enter Admin name or Department");

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchAdmins();
            }
        });
        btnSearch.setBackground(Color.RED);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setBounds(551, 67, 91, 32);
        add(btnSearch);

        setPreferredSize(new Dimension(800, 700));
        setBackground(new Color(245, 245, 245));

        // Create grid panel
        gridpanel = new JPanel();
        gridpanel.setLayout(new GridLayout(5, 1, 10, 10));
        gridpanel.setBackground(new Color(245, 245, 245));
        gridpanel.setBounds(23, 125, 700, 500);
        gridpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        loadAdminData();
        add(gridpanel);
    }
    
    private void loadAdminData() {
        gridpanel.removeAll();
        try {
            List<Map<String, String>> admins = controller.getAdminData();
            
            if (admins.isEmpty()) {
                JLabel noDataLabel = new JLabel("No admin data found");
                noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                gridpanel.add(noDataLabel);
            } else {
                int count = Math.min(admins.size(), 5);
                for (int i = 0; i < count; i++) {
                    Map<String, String> admin = admins.get(i);
                    addAdminCard(admin);
                }
                
                for (int i = count; i < 5; i++) {
                    JPanel emptyCard = new JPanel();
                    emptyCard.setBackground(new Color(245, 245, 245));
                    emptyCard.setPreferredSize(new Dimension(680, 80));
                    gridpanel.add(emptyCard);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading admin data: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        gridpanel.revalidate();
        gridpanel.repaint();
    }
    
    private void addAdminCard(Map<String, String> admin) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(680, 80));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel("<html><a href='#' style='color:black;text-decoration:underline'>" + admin.get("name") + "</a></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.BLUE);
        nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        nameLabel.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		Container parent = AdminDashboard.this.getParent();
                while (parent != null && !(parent instanceof AdminNavBar)) {
                    parent = parent.getParent();
                }
                
                if (parent instanceof AdminNavBar) {
                    ((AdminNavBar)parent).showAdminDetail(admin.get("name"));
                }
        	}
        	
        	public void mouseEntered(MouseEvent e) {
                nameLabel.setText("<html><a href='#' style='color:#0066cc;'>" + admin.get("name") + "</a></html>");
            }
        	
        	public void mouseExited(MouseEvent e) {
                nameLabel.setText("<html><a href='#' style='color:black;text-decoration:underline'>" + admin.get("name") + "</a></html>");
            }
		});
        
        JLabel deptLabel = new JLabel(admin.get("department"));
        deptLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        deptLabel.setForeground(new Color(100, 100, 100)); 
        deptLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(deptLabel);
        card.add(infoPanel, BorderLayout.CENTER);

        JButton btndelete = new JButton("Delete");
        btndelete.setBackground(Color.RED);
        btndelete.setForeground(Color.WHITE);
        btndelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                card, 
                "Delete " + admin.get("name") + "?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (controller.deleteAdmin(admin.get("name"))) {
                        loadAdminData(); // Refresh the data
                    } else {
                        JOptionPane.showMessageDialog(card, 
                            "Failed to delete admin", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(card, 
                        "Database error: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        card.add(btndelete, BorderLayout.EAST);
        gridpanel.add(card);
    }
    
    private void searchAdmins() {
        String searchText = txtNAndD.getText().trim();
        if (searchText.isEmpty() || searchText.equals("Enter Admin name or Department")) {
            loadAdminData();
            return;
        }
        
        // In a real implementation, you would call a controller method
        // to search with the criteria, but for now just filter existing data
        try {
            List<Map<String, String>> allAdmins = controller.getAdminData();
            List<Map<String, String>> filteredAdmins = new ArrayList<>();
            
            for (Map<String, String> admin : allAdmins) {
                if (admin.get("name").toLowerCase().contains(searchText.toLowerCase()) || 
                    admin.get("department").toLowerCase().contains(searchText.toLowerCase())) {
                    filteredAdmins.add(admin);
                }
            }
            
            gridpanel.removeAll();
            if (filteredAdmins.isEmpty()) {
                JLabel noDataLabel = new JLabel("No matching admins found");
                noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                gridpanel.add(noDataLabel);
            } else {
                int count = Math.min(filteredAdmins.size(), 5);
                for (int i = 0; i < count; i++) {
                    addAdminCard(filteredAdmins.get(i));
                }
                
                for (int i = count; i < 5; i++) {
                    JPanel emptyCard = new JPanel();
                    emptyCard.setBackground(new Color(245, 245, 245));
                    emptyCard.setPreferredSize(new Dimension(680, 80));
                    gridpanel.add(emptyCard);
                }
            }
            gridpanel.revalidate();
            gridpanel.repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error searching admin data: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
  
    
    private void setPlaceholder(JTextField textField, String placeholder) {
        // Set initial placeholder
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
 
        // To keep track of whether placeholder is currently shown
        final boolean[] showingPlaceholder = {true};
 
        // Focus behavior
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder[0]) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    showingPlaceholder[0] = false;
                }
            }
 
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                    showingPlaceholder[0] = true;
                }
            }
        });
 
        // Key listener — placeholder gets removed on first key press
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (showingPlaceholder[0]) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    showingPlaceholder[0] = false;
                }
            }
        });
 
        // Document listener — placeholder reappears if text is cleared
        textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                SwingUtilities.invokeLater(() -> {
                    if (!showingPlaceholder[0] && textField.getText().isEmpty()) {
                        textField.setForeground(Color.GRAY);
                        textField.setText(placeholder);
                        showingPlaceholder[0] = true;
                        // Move caret to start to simulate placeholder behavior
                        textField.setCaretPosition(0);
                    }
                });
            }
 
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("AdminDashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new AdminDashboard());
            frame.setSize(800, 730);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}