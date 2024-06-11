import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ContactManagerUI extends JFrame {

    private final DefaultListModel<Contact> model = new DefaultListModel<>();
    private final JList<Contact> list = new JList<>(model);
    private final JButton addButton = new JButton("Ajouter");
    private final JButton deleteButton = new JButton("Supprimer");
    private final JButton editButton = new JButton("Modifier");
    private final JTextField searchField = new JTextField(20); // Champ de recherche
    private final JComboBox<String> groupFilterComboBox = new JComboBox<>();
    private final JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Trier A-Z", "Trier Z-A", "Trier par date"});
    private List<Contact> allContacts = new ArrayList<>();
    private Map<Contact, Date> contactAddDates = new HashMap<>(); // Store the add date for each contact
    private Set<String> groups;

    public ContactManagerUI() {
        setTitle("Gestionnaire de Contacts");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        groups = CategoryManager.loadCategories();
        initializeUI();
        loadContacts();
        setSize(500, 400);
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        list.setCellRenderer(new ContactListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);

        list.setDragEnabled(true);
        list.setDropMode(DropMode.INSERT);
        list.setTransferHandler(new ContactTransferHandler(this::saveAllContacts));

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = setupFilterPanel(); // Panneau de filtre et de tri
        JPanel searchPanel = setupSearchPanel(); // Panneau de recherche
        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = setupButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setupButtonListeners();
        setupListListeners();
    }

    private JPanel setupFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Groupe:"));
        filterPanel.add(groupFilterComboBox);
        filterPanel.add(new JLabel("Trier:"));
        filterPanel.add(sortComboBox);

        groupFilterComboBox.addItem("Tous");
        groups.forEach(groupFilterComboBox::addItem);
        groupFilterComboBox.addItem("Ajouter un groupe...");

        groupFilterComboBox.addActionListener(e -> {
            String selected = (String) groupFilterComboBox.getSelectedItem();
            if ("Ajouter un groupe...".equals(selected)) {
                String newGroup = JOptionPane.showInputDialog(this, "Nom du nouveau groupe:");
                if (newGroup != null && !newGroup.trim().isEmpty() && !groups.contains(newGroup)) {
                    groups.add(newGroup);
                    groupFilterComboBox.insertItemAt(newGroup, groupFilterComboBox.getItemCount() - 1);
                    groupFilterComboBox.setSelectedItem(newGroup);
                    CategoryManager.saveCategories(groups);
                } else {
                    groupFilterComboBox.setSelectedIndex(0);
                }
            } else {
                filterContacts();
            }
        });

        sortComboBox.addActionListener(e -> sortContacts());

        return filterPanel;
    }

    private JPanel setupSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Rechercher :"));
        searchPanel.add(searchField);

        // Ajouter un placeholder
        searchField.setText("Chercher...");
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Chercher...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Chercher...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterContacts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterContacts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterContacts();
            }
        });

        return searchPanel;
    }

    private JPanel setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        return buttonPanel;
    }

    private void setupButtonListeners() {
        addButton.addActionListener(e -> addContact());
        deleteButton.addActionListener(e -> deleteSelectedContact());
        editButton.addActionListener(e -> editSelectedContact());
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedContact();
                }
            }
        });
    }

    private void setupListListeners() {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                Contact contact = model.getElementAt(index);

                Rectangle cellBounds = list.getCellBounds(index, index);
                Component component = list.getCellRenderer().getListCellRendererComponent(list, contact, index, false, false);

                if (component instanceof JPanel) {
                    JPanel panel = (JPanel) component;
                    Component clickedComponent = panel.getComponentAt(e.getPoint().x - cellBounds.x, e.getPoint().y - cellBounds.y);

                    if (clickedComponent instanceof JButton) {
                        JButton button = (JButton) clickedComponent;
                        if ("phoneButton".equals(button.getName())) {
                            callPhoneNumber(contact.getPhoneNumber());
                        } else if ("emailButton".equals(button.getName())) {
                            sendEmail(contact.getEmail());
                        }
                    } else if (clickedComponent instanceof JLabel) {
                        JLabel label = (JLabel) clickedComponent;
                        ContactListRenderer renderer = (ContactListRenderer) list.getCellRenderer();
                        if (label == renderer.getPhoneLabel()) {
                            callPhoneNumber(contact.getPhoneNumber());
                        } else if (label == renderer.getEmailLabel()) {
                            sendEmail(contact.getEmail());
                        }
                    }
                }
            }
        });
    }

    private void callPhoneNumber(String phoneNumber) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("tel:" + phoneNumber));
            } else {
                JOptionPane.showMessageDialog(this, "L'action de téléphonie n'est pas supportée sur ce système.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture de l'application de téléphonie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendEmail(String email) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                Desktop.getDesktop().mail(new URI("mailto:" + email));
            } else {
                JOptionPane.showMessageDialog(this, "L'action de messagerie n'est pas supportée sur ce système.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture de l'application de messagerie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadContacts() {
        allContacts = ContactIO.loadContacts();
        for (Contact contact : allContacts) {
            contactAddDates.put(contact, new Date()); // Assuming you want to set the current date for existing contacts
        }
        updateContactListDisplay(allContacts);
    }

    private void saveAllContacts() {
        ContactIO.saveAllContacts(new ArrayList<>(Collections.list(model.elements())));
    }

    private void addContact() {
        ContactDialog dialog = new ContactDialog(this, groups);
        Contact contact = dialog.showDialog();
        if (contact != null) {
            allContacts.add(contact);
            contactAddDates.put(contact, new Date()); // Store the current date as the add date
            filterContacts();
            saveAllContacts();
        }
    }

    private void deleteSelectedContact() {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex >= 0) {
            Contact selectedContact = list.getSelectedValue();
            allContacts.remove(selectedContact);
            contactAddDates.remove(selectedContact); // Remove the add date entry
            filterContacts();
            saveAllContacts();
        }
    }

    private void editSelectedContact() {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex >= 0) {
            Contact contact = model.getElementAt(selectedIndex);
            ContactDialog dialog = new ContactDialog(this, contact, groups);
            Contact updatedContact = dialog.showDialog();
            if (updatedContact != null) {
                allContacts.set(selectedIndex, updatedContact);
                Date addDate = contactAddDates.remove(contact);
                contactAddDates.put(updatedContact, addDate); // Preserve the original add date
                filterContacts();
                saveAllContacts();
            }
        }
    }

    private void filterContacts() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.equals("chercher...")) {
            searchText = "";
        }
        final String finalSearchText = searchText; // Déclarer la variable comme finale
        String selectedGroup = (String) groupFilterComboBox.getSelectedItem();

        List<Contact> filteredContacts = allContacts.stream()
                .filter(contact -> contact.getName().toLowerCase().contains(finalSearchText) ||
                        contact.getPhoneNumber().toLowerCase().contains(finalSearchText) ||
                        contact.getEmail().toLowerCase().contains(finalSearchText))
                .filter(contact -> "Tous".equals(selectedGroup) || contact.getGroup().equals(selectedGroup))
                .collect(Collectors.toList());

        updateContactListDisplay(filteredContacts);
    }

    private void updateContactListDisplay(List<Contact> contacts) {
        model.clear();
        contacts.forEach(model::addElement);
    }

    private void sortContacts() {
        String selectedSort = (String) sortComboBox.getSelectedItem();

        Comparator<Contact> comparator;
        switch (selectedSort) {
            case "Trier Z-A":
                comparator = Comparator.comparing(Contact::getName).reversed();
                break;
            case "Trier par date":
                comparator = Comparator.comparing(contactAddDates::get);
                break;
            case "Trier A-Z":
            default:
                comparator = Comparator.comparing(Contact::getName);
                break;
        }

        List<Contact> sortedContacts = allContacts.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        updateContactListDisplay(sortedContacts);
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.forLanguageTag("fr-FR"));
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            SwingUtilities.invokeLater(() -> new ContactManagerUI().setVisible(true));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace(System.out);
        }
    }
}
