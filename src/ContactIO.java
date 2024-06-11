import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ContactIO {

    private static final String CONTACTS_FILE = "resources/contacts.json";
    private static final SecretKey key = EncryptionUtility.getOrGenerateKey();

    public static List<Contact> loadContacts() {
        List<Contact> contacts = new ArrayList<>();
        File file = new File(CONTACTS_FILE);
        if (!file.exists() || file.length() == 0) {
            initializeEmptyEncryptedFile(file);
            return contacts;
        }

        try {
            String encryptedContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String decryptedContent = EncryptionUtility.decrypt(encryptedContent, key);
            JSONArray contactsJson = new JSONArray(decryptedContent);

            for (int i = 0; i < contactsJson.length(); i++) {
                JSONObject contactObject = contactsJson.getJSONObject(i);
                Contact contact = new Contact(
                        contactObject.getString("name"),
                        contactObject.getString("phoneNumber"),
                        contactObject.getString("email"),
                        contactObject.optString("group", "Général")
                );
                contacts.add(contact);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des contacts.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(System.out);
        }
        return contacts;
    }

    public static void saveAllContacts(List<Contact> contacts) {
        JSONArray contactsJsonForEncryption = new JSONArray();
        StringBuilder unencryptedJsonBuilder = new StringBuilder();
        unencryptedJsonBuilder.append("[\n");

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            JSONObject contactObject = new JSONObject();
            contactObject.put("name", contact.getName());
            contactObject.put("phoneNumber", contact.getPhoneNumber());
            contactObject.put("email", contact.getEmail());
            contactObject.put("group", contact.getGroup());
            contactsJsonForEncryption.put(contactObject);

            String unencryptedContactJson = String.format(
                    "    {\n" +
                            "        \"name\": \"%s\",\n" +
                            "        \"phoneNumber\": \"%s\",\n" +
                            "        \"email\": \"%s\",\n" +
                            "        \"group\": \"%s\"\n" +
                            "    }",
                    contact.getName(),
                    contact.getPhoneNumber(),
                    contact.getEmail(),
                    contact.getGroup()
            );

            unencryptedJsonBuilder.append(unencryptedContactJson);
            if (i < contacts.size() - 1) {
                unencryptedJsonBuilder.append(",\n");
            }
        }

        unencryptedJsonBuilder.append("\n]");

        try {
            String encryptedData = EncryptionUtility.encrypt(contactsJsonForEncryption.toString(), key);
            try (FileWriter writer = new FileWriter(CONTACTS_FILE, false)) {
                writer.write(encryptedData);
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("resources/unencrypted_contacts.json"), StandardCharsets.UTF_8)) {
                writer.write(unencryptedJsonBuilder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private static void initializeEmptyEncryptedFile(File file) {
        try {
            if (file.createNewFile()) {
                try (FileWriter writer = new FileWriter(file)) {
                    String encryptedData = EncryptionUtility.encrypt("[]", key);
                    writer.write(encryptedData);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossible de créer ou d'initialiser le fichier de contacts avec des données cryptées.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(System.out);
        }
    }
}
