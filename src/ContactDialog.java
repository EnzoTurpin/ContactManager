import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Set;
import java.util.regex.Pattern;

// Classe ContactDialog pour afficher une boîte de dialogue permettant d'ajouter ou de modifier un contact
public class ContactDialog extends JDialog {
    // Champs de texte pour les informations de contact
    private final JTextField firstNameField = new JTextField(20);
    private final JTextField lastNameField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextArea biographyArea = new JTextArea(5, 20);
    private final JComboBox<String> groupComboBox = new JComboBox<>();
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Annuler");
    private Contact contact;
    private final Set<String> groups;

    // Constructeur pour ajouter un nouveau contact
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

    // Constructeur pour modifier un contact existant
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

    // Initialiser les composants de la boîte de dialogue
    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Appliquer des filtres pour n'accepter que les lettres pour les prénoms et noms, et que les chiffres pour le téléphone
        ((AbstractDocument) firstNameField.getDocument()).setDocumentFilter(new LettersOnlyFilter());
        ((AbstractDocument) lastNameField.getDocument()).setDocumentFilter(new LettersOnlyFilter());
        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new DigitsOnlyFilter());

        // Ajouter les champs de texte et leurs étiquettes au panneau d'entrée
        inputPanel.add(new JLabel("Prénom:"), gbc);
        inputPanel.add(firstNameField, gbc);
        inputPanel.add(new JLabel("Nom:"), gbc);
        inputPanel.add(lastNameField, gbc);
        inputPanel.add(new JLabel("Téléphone:"), gbc);
        inputPanel.add(phoneField, gbc);
        inputPanel.add(new JLabel("Email:"), gbc);
        inputPanel.add(emailField, gbc);
        inputPanel.add(new JLabel("Biographie:"), gbc);

        // Configurer biographyArea pour le retour à la ligne
        biographyArea.setLineWrap(true);
        biographyArea.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(biographyArea), gbc);

        inputPanel.add(new JLabel("Groupe:"), gbc);
        groups.forEach(groupComboBox::addItem);
        inputPanel.add(groupComboBox, gbc);

        add(inputPanel, BorderLayout.CENTER);

        // Ajouter les boutons OK et Annuler au panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Configurer la validation de l'email
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

    // Valider le format de l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Valider les entrées utilisateur
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

    // Sauvegarder le contact après validation des entrées
    private void saveContact() {
        if (validateInput()) {
            contact = new Contact(firstNameField.getText(), lastNameField.getText(), phoneField.getText(), emailField.getText(), (String) groupComboBox.getSelectedItem(), biographyArea.getText());
            setVisible(false);
        }
    }

    // Mettre à jour le contact après validation des entrées
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

    // Annuler la boîte de dialogue
    private void cancelDialog() {
        contact = null;
        setVisible(false);
    }

    // Définir les champs avec les informations du contact à éditer
    private void setFields(Contact contactToEdit) {
        firstNameField.setText(contactToEdit.getFirstName());
        lastNameField.setText(contactToEdit.getLastName());
        phoneField.setText(contactToEdit.getPhoneNumber());
        emailField.setText(contactToEdit.getEmail());
        groupComboBox.setSelectedItem(contactToEdit.getGroup());
        biographyArea.setText(contactToEdit.getBiography());
    }

    // Afficher la boîte de dialogue et retourner le contact créé ou modifié
    public Contact showDialog() {
        setVisible(true);
        return contact;
    }

    // Filtre pour n'accepter que les lettres et les espaces
    private class LettersOnlyFilter extends DocumentFilter {
        private final Pattern regex = Pattern.compile("[\\p{L}\\s]*");

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (regex.matcher(string).matches()) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (regex.matcher(text).matches()) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    // Filtre pour n'accepter que les chiffres
    private class DigitsOnlyFilter extends DocumentFilter {
        private final Pattern regex = Pattern.compile("\\d*");

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (regex.matcher(string).matches()) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (regex.matcher(text).matches()) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
