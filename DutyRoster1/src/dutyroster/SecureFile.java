/**
 * This class is used to encrypt and decrypt any type of string data. 
 * @author Othen W. Prock
 * @version 3 October 31st, 2017
*/
package dutyroster;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;  
import javax.crypto.spec.IvParameterSpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SecureFile {

    private static String ENCODING = "UTF-8";
    private static String KEY = "Sprays0123456789";
    private static String IVKEY = "0123456789Sprays"; 
    private String filePath;

    
    //Constructor
    public SecureFile(String inPath){
     
        filePath = inPath;
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
           
    }
 
    /**
     * This creates a cipher key the the encryption
     * @param cipherMode
     * @return
     * @throws Exception 
     */
    private Cipher getCipher(int cipherMode) throws Exception {
         
        final byte[] key = getKeyBytes(makeKey(KEY));
        final byte[] ivKey = getKeyBytes(makeKey(IVKEY));
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivKey);
        cipher.init(cipherMode, secretKeySpec, ivParameterSpec);
        return cipher;
    }

    /**
     * Encrypts a sting input and stores it to file
     * @param strIn
     * @throws Exception 
     */
    private void encrypt(String strIn) throws Exception {        
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);      
        byte[] outputBytes;
        outputBytes =  strIn.getBytes(ENCODING);
        outputBytes = cipher.doFinal(outputBytes);    
        Path path = Paths.get(filePath);
        Files.write(path, outputBytes); //creates, overwrites
    } 

    /**
     * This retrieves data from encrypted file, then decrypts it
     * @return
     * @throws Exception 
     */
    private String decrypt() throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);   
        Path path = Paths.get(filePath);
        byte[] buffer =  Files.readAllBytes(path);
        buffer = cipher.doFinal(buffer);
        return new String(buffer,ENCODING);
    }
    
    
    /**
     * This function should be used to store data because 
     * it handles any exceptions thrown by the encrypt function
     * @param strIn 
     */
    public void store(String strIn) {
       
        try {
            encrypt(strIn);
        } catch (Exception ex) {
            Logger.getLogger(SecureFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This function should be used to retrieving data because 
     * it handles any exceptions thrown by the decrypt function
     * @return 
     */
    public String retrieve() {
        
        try {
            return decrypt();
        } catch (Exception ex) {
           Logger.getLogger(SecureFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
        
    }
    
    /**
     * This function turns keys into byte code
     * @param mKey
     * @return
     * @throws UnsupportedEncodingException 
     */
    private byte[] getKeyBytes(String mKey) throws UnsupportedEncodingException {
        final byte[] byteKey = mKey.getBytes();

        return byteKey;
    }
    
    /**
     * This function uses the stored password as part of the key to
     * encrypt and decrypt employee data
     * @param inKey
     * @return 
     */
    private String makeKey(String inKey) {
        
        String passKey = "";
         
        if (filePath.equals("Settings"))
            return inKey;
        
        try {
            passKey = getPassword();
        } catch (Exception ex) {
            Logger.getLogger(SecureFile.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        return passKey + inKey.substring(0,inKey.length() - passKey.length());

    }
 
    
    /**
     * This function retrieves store password from file 
     * @return
     * @throws Exception 
     */
    private String getPassword() throws Exception{
        
        final byte[] key = getKeyBytes(KEY);
        final byte[] ivKey = getKeyBytes(IVKEY);
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivKey);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        
        Path path = Paths.get("Settings");
        byte[] buffer =  Files.readAllBytes(path);
        buffer = cipher.doFinal(buffer);
        String a = new String(buffer,ENCODING);
 
        String aArry[] = a.split("\\|", -1);
        
        if(aArry[0].length() > 0){
            
            return aArry[0];
        }

        return "";
    }    
    
}