package View;

import Controller.AdminDetailController;
import Model.AdminDetailModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdminDetailView1 extends JPanel {
    private JTextField txtLastName, txtPhoneNumber, txtEmail, txtHiringDate, txtDepartment, txtJobTitle;
    private JCheckBox chkIsActive, chkIsAgreement, chkIsManager;
    private AdminDetailModel currentModel;

    public AdminDetailView1() {
        setLayout(null);
        setPreferredSize(new Dimension(1000, 753));
        setBackground(new Color(245, 245, 245));

        JLabel lblHeader = new JLabel("Admin Details");
        lblHeader.setBounds(40, 20, 300, 30);
        lblHeader.setFont(new Font("MS UI Gothic", Font.BOLD, 24));
        add(lblHeader);

        JPanel panel = new JPanel();
        panel.setBounds(56, 89, 419, 596);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        add(panel);

        addFormFields(panel);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(61, 540, 100, 30);
        panel.add(btnSave);

        btnSave.addActionListener(e -> saveEmployeeDetails());
    }

    private void addFormFields(JPanel panel) {
        txtLastName = createTextFieldWithLabel(panel, "Name", 20);
        txtLastName.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                txtLastName.setBorder(txtLastName.getText().matches("[a-zA-Z\\s]*")
                        ? UIManager.getBorder("TextField.border")
                        : BorderFactory.createLineBorder(Color.RED));
            }
        });

        txtEmail = createTextFieldWithLabel(panel, "Email", 87);
        txtPhoneNumber = createTextFieldWithLabel(panel, "Phone Number", 152);
        txtHiringDate = createTextFieldWithLabel(panel, "Hiring Date", 217);

        chkIsActive = createCheckBox(panel, "Is Active", 287);
        chkIsAgreement = createCheckBox(panel, "Is Agreement", 317);
        chkIsManager = createCheckBox(panel, "Is Manager", 347);

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

    private void saveEmployeeDetails() {
        if (txtLastName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPhoneNumber.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Required fields missing.");
            return;
        }

        if (currentModel == null) {
            JOptionPane.showMessageDialog(this, "No employee loaded to save.");
            return;
        }

        currentModel.setName(txtLastName.getText());
        currentModel.setEmail(txtEmail.getText());
        currentModel.setPhone(txtPhoneNumber.getText());
        currentModel.setHiringDate(txtHiringDate.getText());
        currentModel.setActive(chkIsActive.isSelected());
        currentModel.setAgreement(chkIsAgreement.isSelected());
        currentModel.setManager(chkIsManager.isSelected());
        currentModel.setDepartment(txtDepartment.getText());
        currentModel.setJobTitle(txtJobTitle.getText());

        AdminDetailController controller = new AdminDetailController(currentModel);
        boolean saved = controller.saveAdminDetails();


        JOptionPane.showMessageDialog(this, saved ? "Saved successfully!" : "Save failed.");
    }

    public void loadAdminDetail(String name) {
        AdminDetailController controller = new AdminDetailController(null);
        AdminDetailModel model = controller.fetchAdminByName(name);
        if (model != null) {
            currentModel = model;
            txtLastName.setText(model.getName());
            txtEmail.setText(model.getEmail());
            txtPhoneNumber.setText(model.getPhone());
            txtHiringDate.setText(model.getHiringDate());
            chkIsActive.setSelected(model.isActive());
            chkIsAgreement.setSelected(model.isAgreement());
            chkIsManager.setSelected(model.isManager());
            txtDepartment.setText(model.getDepartment());
            txtJobTitle.setText(model.getJobTitle());
        } else {
            JOptionPane.showMessageDialog(this, "Employee not found!");
        }
    }
}
