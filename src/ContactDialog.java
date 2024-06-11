import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Set;
import java.util.regex.Pattern;

public class ContactDialog extends JDialog {
    private final JTextField firstNameField = new JTextField(20);
    private final JTextField lastNameField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextArea biographyArea = new JTextArea(5, 20);  // Champ de biographie
    private final JComboBox<String> groupComboBox = new JComboBox<>();
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Annuler");
    private Contact contact;
    private final Set<String> groups;

    public ContactDialog(Frame owner, Set<String> groups) {
        super(owner, "Ajouter un contact", true);
        this.groups = groups;
        initializeComponents();
        setupValidation();
        okButton.addActionListener(e -> saveContact());
        cancelButton.addActionListener(e -> cancelDialog());
        pack();
        setLocationRelativeTo(null);
    }

    public ContactDialog(Frame owner, Contact contactToEdit, Set<String> groups) {
        super(owner, "Modifier le contact", true);
        this.contact = contactToEdit;
        this.groups = groups;
        initializeComponents();
        setupValidation();
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

        inputPanel.add(new JLabel("Prénom:"), gbc);
        inputPanel.add(firstNameField, gbc);
        inputPanel.add(new JLabel("Nom:"), gbc);
        inputPanel.add(lastNameField, gbc);
        inputPanel.add(new JLabel("Téléphone:"), gbc);
        inputPanel.add(phoneField, gbc);
        inputPanel.add(new JLabel("Email:"), gbc);
        inputPanel.add(emailField, gbc);
        inputPanel.add(new JLabel("Biographie:"), gbc);

        // Configure biographyArea for line wrap and word wrap
        biographyArea.setLineWrap(true);
        biographyArea.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(biographyArea), gbc);  // Ajouter le champ de biographie

        inputPanel.add(new JLabel("Groupe:"), gbc);
        groups.forEach(groupComboBox::addItem);
        inputPanel.add(groupComboBox, gbc);

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupValidation() {
        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                if (!isValidEmail(emailField.getText())) {
                    JOptionPane.showMessageDialog(ContactDialog.this,
                            "Veuillez entrer une adresse email valide.",
                            "Email invalide",
                            JOptionPane.ERROR_MESSAGE);
                    emailField.requestFocus();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean validateInput() {
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le champ du prénom ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le champ du nom ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidEmail(emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une adresse email valide.", "Email invalide", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveContact() {
        if (validateInput()) {
            contact = new Contact(firstNameField.getText(), lastNameField.getText(), phoneField.getText(), emailField.getText(), (String) groupComboBox.getSelectedItem(), biographyArea.getText());
            setVisible(false);
        }
    }

    private void updateContact() {
        if (validateInput()) {
            contact.setFirstName(firstNameField.getText());
            contact.setLastName(lastNameField.getText());
            contact.setPhoneNumber(phoneField.getText());
            contact.setEmail(emailField.getText());
            contact.setGroup((String) groupComboBox.getSelectedItem());
            contact.setBiography(biographyArea.getText());
            setVisible(false);
        }
    }

    private void cancelDialog() {
        contact = null; // Annulation de toute modification ou ajout
        setVisible(false);
    }

    private void setFields(Contact contactToEdit) {
        firstNameField.setText(contactToEdit.getFirstName());
        lastNameField.setText(contactToEdit.getLastName());
        phoneField.setText(contactToEdit.getPhoneNumber());
        emailField.setText(contactToEdit.getEmail());
        groupComboBox.setSelectedItem(contactToEdit.getGroup());
        biographyArea.setText(contactToEdit.getBiography());
    }

    public Contact showDialog() {
        setVisible(true);
        return contact;
    }
}
