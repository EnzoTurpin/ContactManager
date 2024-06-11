import java.awt.*;
import java.net.URI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ContactListRenderer extends JPanel implements ListCellRenderer<Contact> {
    private JLabel nameLabel = new JLabel();
    private JLabel phoneLabel = new JLabel();
    private JLabel emailLabel = new JLabel();
    private JButton phoneButton = new JButton("✆");
    private JButton emailButton = new JButton("✉");

    public ContactListRenderer() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.add(nameLabel);
        textPanel.add(phoneLabel);
        textPanel.add(emailLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(phoneButton);
        buttonPanel.add(emailButton);

        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        phoneButton.setContentAreaFilled(false);
        phoneButton.setBorderPainted(false);
        phoneButton.setName("phoneButton");

        emailButton.setContentAreaFilled(false);
        emailButton.setBorderPainted(false);
        emailButton.setName("emailButton");

        phoneButton.addActionListener(e -> {
            String phoneNumber = phoneLabel.getText();
            openPhoneApp(phoneNumber);
        });

        emailButton.addActionListener(e -> {
            String email = emailLabel.getText();
            openEmailApp(email);
        });
    }

    private void openPhoneApp(String phoneNumber) {
        try {
            Desktop.getDesktop().browse(new URI("tel:" + phoneNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEmailApp(String email) {
        try {
            Desktop.getDesktop().mail(new URI("mailto:" + email));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JLabel getPhoneLabel() {
        return phoneLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contact> list, Contact contact, int index, boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText(contact.getFirstName() + " " + contact.getLastName());
        phoneLabel.setText(contact.getPhoneNumber());
        emailLabel.setText(contact.getEmail());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
