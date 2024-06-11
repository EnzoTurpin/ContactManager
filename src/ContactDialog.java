import java.awt.*;
import java.util.Set;
import javax.swing.*;

public class ContactDialog extends JDialog {

    private final JTextField nameField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JComboBox<String> groupComboBox = new JComboBox<>();
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Annuler");
    private Contact contact;

    public ContactDialog(Frame owner, Set<String> groups) {
        super(owner, "Ajouter un contact", true);
        initializeComponents();
        groups.forEach(groupComboBox::addItem);
        okButton.addActionListener(e -> saveContact());
        cancelButton.addActionListener(e -> cancelDialog());
        pack();
        setLocationRelativeTo(null);
    }

    public ContactDialog(Frame owner, Contact contactToEdit, Set<String> groups) {
        super(owner, "Modifier le contact", true);
        this.contact = contactToEdit;
        initializeComponents();
        groups.forEach(groupComboBox::addItem);
        setFields(contactToEdit);
        okButton.addActionListener(e -> updateContact());
        cancelButton.addActionListener(e -> cancelDialog());
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        inputPanel.add(new JLabel("Nom:"), gbc);
        inputPanel.add(nameField, gbc);
        inputPanel.add(new JLabel("Numéro de téléphone:"), gbc);
        inputPanel.add(phoneField, gbc);
        inputPanel.add(new JLabel("Adresse email:"), gbc);
        inputPanel.add(emailField, gbc);
        inputPanel.add(new JLabel("Groupe:"), gbc);
        inputPanel.add(groupComboBox, gbc);
        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setFields(Contact contactToEdit) {
        nameField.setText(contactToEdit.getName());
        phoneField.setText(contactToEdit.getPhoneNumber());
        emailField.setText(contactToEdit.getEmail());
        groupComboBox.setSelectedItem(contactToEdit.getGroup());
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty() || phoneField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private void configureContactFromInput() {
        if (contact == null) contact = new Contact();
        contact.setName(nameField.getText());
        contact.setPhoneNumber(phoneField.getText());
        contact.setEmail(emailField.getText());
        contact.setGroup((String) groupComboBox.getSelectedItem());
    }

    private void saveContact() {
        if (validateInput()) return;
        configureContactFromInput();
        setVisible(false);
    }

    private void updateContact() {
        if (validateInput()) return;
        configureContactFromInput();
        setVisible(false);
    }

    private void cancelDialog() {
        contact = null;
        setVisible(false);
    }

    public Contact showDialog() {
        setVisible(true);
        return contact;
    }
}
