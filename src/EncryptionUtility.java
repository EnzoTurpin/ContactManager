import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtility {

  private static final String ALGORITHM = "AES";
  private static final Path KEY_FILE_PATH = Paths.get("ressources/keyfile.txt");

  // Récupère la clé existante ou en génère une nouvelle si nécessaire.
  public static SecretKey getOrGenerateKey() {
    try {
      if (Files.exists(KEY_FILE_PATH)) {
        // Lecture et décodage de la clé depuis le fichier.
        return new SecretKeySpec(
          Base64.getDecoder().decode(Files.readString(KEY_FILE_PATH)),
          ALGORITHM
        );
      } else {
        // Génération d'une nouvelle clé, sauvegarde, et retour.
        SecretKey secretKey = KeyGenerator.getInstance(ALGORITHM).generateKey();
        Files.writeString(
          KEY_FILE_PATH,
          Base64.getEncoder().encodeToString(secretKey.getEncoded())
        );
        return secretKey;
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return null;
    }
  }

  // Chiffre le texte en entrée avec la clé fournie.
  public static String encrypt(String input, SecretKey key) throws Exception {
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
  }

  // Déchiffre le texte en entrée avec la clé fournie.
  public static String decrypt(String input, SecretKey key) throws Exception {
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, key);
    return new String(cipher.doFinal(Base64.getDecoder().decode(input)));
  }
}
