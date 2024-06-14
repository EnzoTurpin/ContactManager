import org.json.JSONArray;
import org.json.JSONObject;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Classe ContactIO pour gérer l'entrée/sortie des contacts
public class ContactIO {

    // Chemins des fichiers de contacts
    private static final String CONTACTS_FILE = "ressources/contacts.json";
    private static final String UNENCRYPTED_CONTACTS_FILE = "ressources/unencrypted_contacts.json";
    // Clé secrète pour le chiffrement
    private static final SecretKey key = EncryptionUtility.getOrGenerateKey();
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";

    // Méthode pour charger les contacts à partir du fichier crypté
    public static List<Contact> loadContacts() {
        List<Contact> contacts = new ArrayList<>();
        File file = new File(CONTACTS_FILE);
        if (!file.exists() || file.length() == 0) {
            initializeEmptyEncryptedFile(file);
            return contacts;
        }

        try {
            // Lire et décrypter le contenu du fichier
            String encryptedContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String decryptedContent = EncryptionUtility.decrypt(encryptedContent, key);
            JSONArray contactsJson = new JSONArray(decryptedContent);

            // Convertir chaque entrée JSON en objet Contact
            for (int i = 0; i < contactsJson.length(); i++) {
                JSONObject contactObject = contactsJson.getJSONObject(i);
                Contact contact = new Contact(
                    contactObject.getString("firstName"),
                    contactObject.getString("lastName"),
                    contactObject.optString("phoneNumber", ""),
                    contactObject.optString("email", ""),
                    contactObject.optString("group", ""),
                    contactObject.optString("biography", "")
                );
                contacts.add(contact);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des contacts.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(System.out);
        }
        return contacts;
    }

    // Méthode pour sauvegarder tous les contacts dans les fichiers crypté et non crypté
    public static void saveAllContacts(List<Contact> contacts) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        JSONArray contactsJsonForEncryption = new JSONArray();

        StringBuilder unencryptedJsonBuilder = new StringBuilder();
        unencryptedJsonBuilder.append("[\n");

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            JSONObject contactObject = new JSONObject();

            try {
                // Ajouter les détails du contact à l'objet JSON
                contactObject.put("firstName", contact.getFirstName());
                contactObject.put("lastName", contact.getLastName());
                contactObject.put("phoneNumber", contact.getPhoneNumber());
                contactObject.put("email", contact.getEmail());
                contactObject.put("group", contact.getGroup());
                contactObject.put("biography", contact.getBiography());

                contactsJsonForEncryption.put(contactObject);

                // Construire la chaîne JSON non cryptée pour sauvegarde
                String unencryptedContactJson = String.format(
                    "    {\n" +
                    "        \"firstName\": \"%s\",\n" +
                    "        \"lastName\": \"%s\",\n" +
                    "        \"phoneNumber\": \"%s\",\n" +
                    "        \"email\": \"%s\",\n" +
                    "        \"group\": \"%s\",\n" +
                    "        \"biography\": \"%s\"\n" +
                    "    }",
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getPhoneNumber(),
                    contact.getEmail(),
                    contact.getGroup(),
                    contact.getBiography()
                );

                unencryptedJsonBuilder.append(unencryptedContactJson);
                if (i < contacts.size() - 1) {
                    unencryptedJsonBuilder.append(",\n");
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        unencryptedJsonBuilder.append("\n]");

        try {
            // Encrypter et sauvegarder les contacts dans le fichier crypté
            String encryptedData = EncryptionUtility.encrypt(contactsJsonForEncryption.toString(), key);
            try (FileWriter writer = new FileWriter(CONTACTS_FILE, false)) {
                writer.write(encryptedData);
            }
            System.out.println("Contacts encrypted and saved successfully.");

            // Sauvegarder les contacts dans le fichier non crypté
            File unencryptedFile = new File(UNENCRYPTED_CONTACTS_FILE);
            if (!unencryptedFile.exists()) {
                boolean fileCreated = unencryptedFile.createNewFile();
                if (fileCreated) {
                    System.out.println("Unencrypted contacts file created successfully.");
                } else {
                    System.out.println("Failed to create unencrypted contacts file.");
                }
            }
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(unencryptedFile), StandardCharsets.UTF_8)) {
                writer.write(unencryptedJsonBuilder.toString());
            }
            System.out.println("Unencrypted contacts saved successfully.");

        } catch (Exception e) {
            System.out.println("Error saving contacts.");
            e.printStackTrace(System.out);
        }
    }

    // Méthode pour initialiser un fichier crypté vide
    private static void initializeEmptyEncryptedFile(File file) {
        try {
            if (file.createNewFile()) {
                try (FileWriter writer = new FileWriter(file)) {
                    String encryptedData = EncryptionUtility.encrypt("[]", key);
                    writer.write(encryptedData);
                }
                System.out.println("Empty encrypted file initialized successfully.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossible de créer ou d'initialiser le fichier de contacts avec des données cryptées.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(System.out);
        }
    }
}
