import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

// Classe CategoryManager pour gérer les catégories
public class CategoryManager {

    // Chemin vers le fichier des catégories
    private static final String CATEGORY_FILE = "ressources/categories.txt";
    private static final Path CATEGORY_PATH = Paths.get(CATEGORY_FILE);

    // Méthode pour sauvegarder une catégorie dans le fichier
    public static void saveCategory(String category) {
        try {
            // Écriture de la catégorie dans le fichier, en ajoutant une nouvelle ligne
            Files.write(CATEGORY_PATH, (category + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            // En cas d'erreur d'IO, imprimer la pile d'exécution
            e.printStackTrace();
        }
    }

    // Méthode pour charger les catégories à partir du fichier
    public static Set<String> loadCategories() {
        try {
            // Lecture de toutes les lignes du fichier et ajout à un ensemble
            Set<String> categories = new HashSet<>(Files.readAllLines(CATEGORY_PATH));
            return categories;
        } catch (IOException e) {
            // En cas d'erreur d'IO, imprimer la pile d'exécution et retourner un ensemble vide
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    // Méthode pour sauvegarder un ensemble de catégories dans le fichier
    public static void saveCategories(Set<String> categories) {
        try {
            // Écriture de toutes les catégories dans le fichier, remplaçant le contenu existant
            Files.write(CATEGORY_PATH, String.join(System.lineSeparator(), categories).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            // En cas d'erreur d'IO, imprimer la pile d'exécution
            e.printStackTrace();
        }
    }
}
