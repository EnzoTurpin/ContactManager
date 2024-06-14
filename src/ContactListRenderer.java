import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Classe ContactListRenderer pour personnaliser l'affichage des éléments de la liste de contacts
public class ContactListRenderer extends JPanel implements ListCellRenderer<Contact> {
    // Étiquettes pour afficher le nom, le numéro de téléphone et l'email
    private JLabel nameLabel = new JLabel();
    private JLabel phoneLabel = new JLabel();
    private JLabel emailLabel = new JLabel();

    // Constructeur pour initialiser la mise en page des composants
    public ContactListRenderer() {
        // Utiliser un BorderLayout avec un espacement de 5 pixels
        setLayout(new BorderLayout(5, 5));
        // Définir une bordure vide de 5 pixels
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // Panneau pour organiser les étiquettes en grille de 3 lignes
        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.add(nameLabel);
        textPanel.add(phoneLabel);
        textPanel.add(emailLabel);

        // Ajouter le panneau de texte au centre de la mise en page
        add(textPanel, BorderLayout.CENTER);
    }

    // Getter pour l'étiquette du numéro de téléphone
    public JLabel getPhoneLabel() {
        return phoneLabel;
    }

    // Getter pour l'étiquette de l'email
    public JLabel getEmailLabel() {
        return emailLabel;
    }

    // Méthode pour configurer l'affichage des composants de la liste
    @Override
    public Component getListCellRendererComponent(JList<? extends Contact> list, Contact contact, int index, boolean isSelected, boolean cellHasFocus) {
        // Définir le texte des étiquettes avec les informations du contact
        nameLabel.setText(contact.getFirstName() + " " + contact.getLastName());
        phoneLabel.setText(contact.getPhoneNumber());
        emailLabel.setText(contact.getEmail());

        // Changer les couleurs de fond et de texte si l'élément est sélectionné
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        // Retourner le panneau configuré pour l'affichage de la cellule
        return this;
    }
}
