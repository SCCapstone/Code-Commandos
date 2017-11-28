/**
 * This class is used to encrypt and decrypt any type of string data. 
 * @author Othen W. Prock
 * @version 1.0 October 31st, 2017
*/
package dutyroster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;   
import javax.crypto.spec.IvParameterSpec;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author othen
 */
public class SecureFile {

  private byte[] key, ivKey; 
  private static String ENCODING = "UTF-8";
  private static String KEY = "Bar12345Bar12345";
  private static String IVKEY = "RandomInitVector";
  private String filePath;
  
    public SecureFile(String filePath){
     
        this.filePath = filePath;
        File file=new File(filePath);
        Path path = file.toPath();
            
    if (!file.exists()){
         
        try { 
             file.createNewFile();
             Files.setAttribute(path, "dos:hidden", true);
        } 
        catch (IOException ex) {
             Logger.getLogger(SecureFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
     
     
     try {
          key = getKeyBytes(KEY);
          ivKey = getKeyBytes(IVKEY);
    } catch (UnsupportedEncodingException ex) {
          Logger.getLogger(SecureFile.class.getName()).log(Level.SEVERE, null, ex);
    }

 }
 
 
  private byte[] getKeyBytes(String mKey) throws UnsupportedEncodingException {
        final byte[] byteKey = mKey.getBytes();

        return byteKey;
    }

    public Cipher getCipher(int cipherMode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivKey);
        cipher.init(cipherMode, secretKeySpec, ivParameterSpec);
        return cipher;
    }


    public void encrypt(String strIn) throws Exception {
            
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        
        byte[] outputBytes;
        outputBytes =  strIn.getBytes(ENCODING);
        outputBytes = cipher.doFinal(outputBytes);
         
        Path path = Paths.get(filePath);
        Files.write(path, outputBytes); //creates, overwrites

    } 
 

    public String decrypt() throws Exception {
 
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
       
        Path path = Paths.get(filePath);
        
       
        byte[] buffer =  Files.readAllBytes(path);
        
        buffer = cipher.doFinal(buffer);
        
        return new String(buffer,ENCODING);
             
    }
    

    public void store(String strIn) {
       
        try {
            encrypt(strIn);
        } catch (Exception ex) {
            Logger.getLogger(SecureFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public String retrieve() {
        
        try {
            return decrypt();
        } catch (Exception ex) {
           Logger.getLogger(SecureFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
        
    }
 
}