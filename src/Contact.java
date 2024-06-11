public class Contact {

    private String name;
    private String phoneNumber;
    private String email;
    private String group;

    // Constructeur par défaut
    public Contact() {
        this.name = "";
        this.phoneNumber = "";
        this.email = "";
        this.group = "Général";
    }

    // Constructeur avec tous les paramètres
    public Contact(String name, String phoneNumber, String email, String group) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.group = group;
    }

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFormattedPhoneNumber() {
        return formatPhoneNumber(phoneNumber);
    }

    private String formatPhoneNumber(String phoneNumber) {
        String cleaned = phoneNumber.replaceAll("\\s", "");
        return cleaned.replaceAll("(.{2})", "$1 ").trim();
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", name, getFormattedPhoneNumber(), email, group);
    }
}
