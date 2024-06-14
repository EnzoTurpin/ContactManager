// Classe Contact pour représenter les informations de contact
public class Contact {
    // Attributs de la classe Contact
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String group;
    private String biography;

    // Constructeur pour initialiser un contact avec les détails donnés
    public Contact(String firstName, String lastName, String phoneNumber, String email, String group, String biography) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.group = group;
        this.biography = biography;
    }

    // Getter pour le prénom
    public String getFirstName() {
        return firstName;
    }

    // Setter pour le prénom
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter pour le nom de famille
    public String getLastName() {
        return lastName;
    }

    // Setter pour le nom de famille
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter pour le numéro de téléphone
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setter pour le numéro de téléphone
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter pour l'email
    public String getEmail() {
        return email;
    }

    // Setter pour l'email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter pour le groupe
    public String getGroup() {
        return group;
    }

    // Setter pour le groupe
    public void setGroup(String group) {
        this.group = group;
    }

    // Getter pour la biographie
    public String getBiography() {
        return biography;
    }

    // Setter pour la biographie
    public void setBiography(String biography) {
        this.biography = biography;
    }

    // Méthode toString pour afficher les informations de contact dans un format lisible
    @Override
    public String toString() {
        return String.format("%s %s (%s)", firstName, lastName, group);
    }
}
