import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;

public class MacEncryption {

    public static void main(String[] args) {
        try {
            String hexKey = "0XA0799CCC0C5BC09879A1BFCF2C99555D912A718A5A";
            byte[] keyBytes = hexStringToByteArray(hexKey);

            String plaintext = "Sensitive information, cardNumber: 1234567891234567";
            byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);

            byte[] macResult = generateRetailMac(keyBytes, plaintextBytes);
            byte[] truncatedResult = Arrays.copyOf(macResult, 4);
            String hexResult = bytesToHex(truncatedResult);

            System.out.println("Result=" + hexResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Convert a hex string to a byte array
    public static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    // Convert a byte array to a hex string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    // Generate a Retail MAC (Message Authentication Code)
    public static byte[] generateRetailMac(byte[] key, byte[] data) {
        try {
            // Split the key into two halves
            byte[] keyPart1 = Arrays.copyOf(key, 8);
            byte[] keyPart2 = Arrays.copyOfRange(key, 8, 16);

            // Pad the data
            byte[] paddedData = padData(data);

            // Initialize the DES keys
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey1 = keyFactory.generateSecret(new DESKeySpec(keyPart1));
            SecretKey secretKey2 = keyFactory.generateSecret(new DESKeySpec(keyPart2));

            // Initialize the cipher for encryption with key1
            Cipher encryptCipher = Cipher.getInstance("DES/CBC/NoPadding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey1, new IvParameterSpec(new byte[8]));

            // Initialize the cipher for decryption with key2
            Cipher decryptCipher = Cipher.getInstance("DES/CBC/NoPadding");
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey2, new IvParameterSpec(new byte[8]));

            // Perform the encryption
            byte[] encryptedData = encryptCipher.doFinal(paddedData);

            // Extract the last block
            byte[] lastBlock = Arrays.copyOfRange(encryptedData, encryptedData.length - 8, encryptedData.length);

            // Decrypt the last block with key2
            byte[] decryptedBlock = decryptCipher.doFinal(lastBlock);

            // Encrypt the decrypted block with key1
            byte[] finalBlock = encryptCipher.doFinal(decryptedBlock);

            return finalBlock;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Pad the input data according to ISO/IEC 9797-1 or ISO 7816-4
    public static byte[] padData(byte[] data) {
        int paddingLength = 8 - (data.length % 8);
        int paddedLength = data.length + paddingLength;
        byte[] paddedData = Arrays.copyOf(data, paddedLength);
        Arrays.fill(paddedData, data.length, paddedLength, (byte) 0);
        return paddedData;
    }
}
