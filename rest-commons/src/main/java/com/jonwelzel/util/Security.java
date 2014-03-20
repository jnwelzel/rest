package com.jonwelzel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utility class containing security-related methods.
 * 
 * @author jwelzel
 * 
 */
public class Security {

    private static SecureRandom secureGenerator;

    private static SecureRandom getSecureGenerator() throws NoSuchAlgorithmException {
        if (secureGenerator == null) {
            secureGenerator = SecureRandom.getInstance("SHA1PRNG");
        }
        return secureGenerator;
    }

    private static String hexEncode(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int idx = 0; idx < bytes.length; ++idx) {
            byte b = bytes[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }

    private static byte[] getRandomBinaryData() throws NoSuchAlgorithmException {
        // generate a random number
        String randomNum = new Integer(getSecureGenerator().nextInt()).toString();

        // get its digest
        MessageDigest sha;
        sha = MessageDigest.getInstance("SHA-1");
        return sha.digest(randomNum.getBytes());
    }

    /**
     * Generate a random and cryptographically strong hexa decimal value containing 40 characters.
     * 
     * @return For example "3a9a897f37e79c5c66b88daa2e5bea46c18d39e1"
     * @throws NoSuchAlgorithmException
     *             If the "SHA1" algorithm cannot be found.
     */
    public static String generateSecureHex() throws NoSuchAlgorithmException {
        return hexEncode(getRandomBinaryData());
    }

}
