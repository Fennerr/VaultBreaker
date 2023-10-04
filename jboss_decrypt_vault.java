import org.jboss.security.vault.SecurityVaultException;
import org.jboss.security.vault.SecurityVault;
import org.picketbox.plugins.vault.PicketBoxSecurityVault;
import java.util.*;
import org.jboss.logging.*;

public class jboss_decrypt_vault {

    public static void main(String[] args) throws Exception {
        if (args.length < 7) {
            System.err.println("Please provide all the necessary arguments.");
            return;
        }
        String ENC_FILE_DIR = args[0];
        String KEYSTORE_URL = args[1];
        String ITERATION_COUNT = args[2];
        String SALT = args[3];
        String KEYSTORE_ALIAS = args[4];
        String KEYSTORE_PASSWORD = args[5];
        String input = args[6];
        try {
            String value = getVaultedString("VAULT::" + input + "::1", ENC_FILE_DIR, KEYSTORE_URL, ITERATION_COUNT, SALT, KEYSTORE_ALIAS, KEYSTORE_PASSWORD);  
            System.out.println(value);                                                                                                                                                          
        }
        catch (SecurityVaultException ex) {
            System.err.println("Error encountered:");
            ex.printStackTrace();
        }
    }

    public static String getVaultedString(String s, String ENC_FILE_DIR, String KEYSTORE_URL, String ITERATION_COUNT, String SALT, String KEYSTORE_ALIAS, String KEYSTORE_PASSWORD) throws SecurityVaultException {
        // create vault instance
        SecurityVault vault = new PicketBoxSecurityVault();

        // initialize vault
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(PicketBoxSecurityVault.ENC_FILE_DIR,      ENC_FILE_DIR);
        options.put(PicketBoxSecurityVault.KEYSTORE_URL,      KEYSTORE_URL);
        options.put(PicketBoxSecurityVault.ITERATION_COUNT,   ITERATION_COUNT);
        options.put(PicketBoxSecurityVault.SALT,              SALT);
        options.put(PicketBoxSecurityVault.KEYSTORE_ALIAS,    KEYSTORE_ALIAS);
        options.put(PicketBoxSecurityVault.KEYSTORE_PASSWORD, KEYSTORE_PASSWORD);
        vault.init(options);

        String[] token = s.split("::");
        char[] pass = vault.retrieve(
                        token[1],            // vault block
                        token[2],            // attribute name
                        token[3].getBytes()  // shared key
                        );
        return new String(pass);
    }
}