import org.jboss.security.vault.SecurityVaultException;
import org.jboss.security.vault.SecurityVault;
import org.picketbox.plugins.vault.PicketBoxSecurityVault;
import java.util.*;
import org.jboss.logging.*;

public class jboss_decrypt_vault {
   private static final String ENC_FILE_DIR = "/home/kali/Documents/retest/vault";
   private static final String KEYSTORE_URL = "/home/kali/Documents/retest/vault/jbvault.jks";
   private static final String ITERATION_COUNT = "80";
   private static final String SALT = "gyYUgoPk";
   private static final String KEYSTORE_ALIAS = "jbvault";
   private static final String KEYSTORE_PASSWORD = "MASK-8jtwpxUMSUtmAI7IqHUhpNCK0zlF.01jdR/Ll42LoHr";


   public static void main(String[] args) throws Exception {                                                                                                                                                                                                              
        try                                                                                                                                                                                                                            
        {                                  
            String input = args[0];                                                                                                                                                                                           
            String value = getVaultedString("VAULT::" + args[0] + "::1");  
            System.out.println(value);                                                                                                                                                          
        }
        catch (SecurityVaultException ex)
        {
            System.err.println("Error encountered:");
            ex.printStackTrace();
        }
	}

   public static String getVaultedString(String s) throws SecurityVaultException
   {
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
                                                                                                                                                                                                                               
                                                                                                                                                                                                                               
