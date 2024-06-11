import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

public class CategoryManager {

    private static final String CATEGORY_FILE = "resources/categories.txt";
    private static final Path CATEGORY_PATH = Paths.get(CATEGORY_FILE);

    public static void saveCategory(String category) {
        try {
            Files.write(CATEGORY_PATH, (category + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> loadCategories() {
        try {
            Set<String> categories = new HashSet<>(Files.readAllLines(CATEGORY_PATH));
            return categories;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
    
    public static void saveCategories(Set<String> categories) {
        try {
            Files.write(CATEGORY_PATH, String.join(System.lineSeparator(), categories).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
