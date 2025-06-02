package View;

import Controller.AdminDetailController;
import Model.AdminDetailModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdminDetailView extends JPanel {
    private JTextField txtName, txtPhoneNumber, txtEmail, txtHiringDate, txtDepartment, txtJobTitle;
    private JCheckBox chkIsActive, chkIsAgreement, chkIsManager;
    private AdminDetailModel currentModel;
    private ActionListener backActionListener;

    public AdminDetailView() {
        currentModel = new AdminDetailModel();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 700));
        setBackground(new Color(245, 245, 245));

        // Header
        JLabel lblHeader = new JLabel("Admin Details");
        lblHeader.setBounds(40, 20, 300, 30);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblHeader);

        // Main form panel
        JPanel formPanel = new JPanel();
        formPanel.setBounds(50, 71, 419, 596);
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        add(formPanel);

        // Form fields
        addFormFields(formPanel);

        // Buttons
        JButton btnSave = new JButton("Save");
        btnSave.setBounds(61, 540, 100, 30);
        btnSave.setBackground(Color.RED);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(btnSave);
        btnSave.addActionListener(e -> saveAdminDetails());

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(171, 540, 100, 30);
        btnBack.setBackground(Color.RED);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(btnBack);
        btnBack.addActionListener(e -> {
            if (backActionListener != null) {
                backActionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "back"));
            }
        });
    }

    public void setBackActionListener(ActionListener listener) {
        this.backActionListener = listener;
    }
    
    private void addFormFields(JPanel panel) {
        // Name field with validation
        txtName = createTextFieldWithLabel(panel, "Name", 20);
        txtName.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                txtName.setBorder(txtName.getText().matches("[a-zA-Z\\s]*")
                        ? UIManager.getBorder("TextField.border")
                        : BorderFactory.createLineBorder(Color.RED));
            }
        });

        // Other fields
        txtEmail = createTextFieldWithLabel(panel, "Email", 87);
        txtPhoneNumber = createTextFieldWithLabel(panel, "Phone Number", 152);
        txtHiringDate = createTextFieldWithLabel(panel, "Hiring Date", 217);

        // Checkboxes
        chkIsActive = createCheckBox(panel, "Is Active", 287);
        chkIsAgreement = createCheckBox(panel, "Is Agreement", 317);
        chkIsManager = createCheckBox(panel, "Is Manager", 347);

        // Additional fields
        txtDepartment = createTextFieldWithLabel(panel, "Department", 377);
        txtJobTitle = createTextFieldWithLabel(panel, "Job Title", 456);
    }

    private JTextField createTextFieldWithLabel(JPanel panel, String label, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(10, y, 100, 20);
        panel.add(lbl);
        
        JTextField field = new JTextField();
        field.setBounds(10, y + 25, 300, 25);
        panel.add(field);
        return field;
    }

    private JCheckBox createCheckBox(JPanel panel, String label, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(10, y, 100, 20);
        panel.add(lbl);
        
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBounds(110, y - 2, 25, 25);
        checkBox.setBackground(Color.WHITE);
        panel.add(checkBox);
        return checkBox;
    }

    private void saveAdminDetails() {
        if (!validateFields()) {
            return;
        }

        updateModelFromFields();
        
        AdminDetailController controller = new AdminDetailController(currentModel);
        boolean saved = controller.saveAdminDetails();

        JOptionPane.showMessageDialog(this, 
            saved ? "Saved successfully!" : "Save failed.",
            "Save Status",
            saved ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    private boolean validateFields() {
        if (txtName.getText().trim().isEmpty() || 
            txtEmail.getText().trim().isEmpty() || 
            txtPhoneNumber.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields (Name, Email, Phone Number).",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void updateModelFromFields() {
        currentModel.setName(txtName.getText().trim());
        currentModel.setEmail(txtEmail.getText().trim());
        currentModel.setPhone(txtPhoneNumber.getText().trim());
        currentModel.setHiringDate(txtHiringDate.getText().trim());
        currentModel.setActive(chkIsActive.isSelected());
        currentModel.setAgreement(chkIsAgreement.isSelected());
        currentModel.setManager(chkIsManager.isSelected());
        currentModel.setDepartment(txtDepartment.getText().trim());
        currentModel.setJobTitle(txtJobTitle.getText().trim());
    }

    public void loadAdminDetail(String name) {
        AdminDetailController controller = new AdminDetailController(null);
        AdminDetailModel model = controller.fetchAdminByName(name);
        
        if (model != null) {
            currentModel = model;
            populateFieldsFromModel();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Admin not found!", 
                "Error",
                JOptionPane.ERROR_MESSAGE);
            
        }
    }

    private void populateFieldsFromModel() {
        txtName.setText(currentModel.getName());
        txtEmail.setText(currentModel.getEmail());
        txtPhoneNumber.setText(currentModel.getPhone());
        txtHiringDate.setText(currentModel.getHiringDate());
        chkIsActive.setSelected(currentModel.isActive());
        chkIsAgreement.setSelected(currentModel.isAgreement());
        chkIsManager.setSelected(currentModel.isManager());
        txtDepartment.setText(currentModel.getDepartment());
        txtJobTitle.setText(currentModel.getJobTitle());
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Details");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            AdminDetailView view = new AdminDetailView();
            view.loadAdminDetail("Test Admin"); // For testing
            
            frame.setContentPane(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}