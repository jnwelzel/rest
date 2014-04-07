package com.jonwelzel.commons.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class containing security-related methods.
 * 
 * @author jwelzel
 * 
 */
public class SecurityUtils {

    private static Logger log = LoggerFactory.getLogger("SecurityUtils");
    private static SecureRandom secureGenerator;

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    public static final int SALT_BYTE_SIZE = 24;
    public static final int HASH_BYTE_SIZE = 24;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;

    private static SecureRandom getSecureGenerator() throws NoSuchAlgorithmException {
        if (secureGenerator == null) {
            secureGenerator = SecureRandom.getInstance("SHA1PRNG");
        }
        return secureGenerator;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     * 
     * @param array
     *            the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
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
        return toHex(getRandomBinaryData());
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     * 
     * @param password
     *            the password to hash
     * @return a salted PBKDF2 hash of the password
     */
    public static String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     * 
     * @param password
     *            the password to hash
     * @return a salted PBKDF2 hash of the password
     */
    public static String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Generate a random salt
        byte[] salt = new byte[SALT_BYTE_SIZE];
        getSecureGenerator().nextBytes(salt);

        // Hash the password
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }

    /**
     * Validates a password using a hash.
     * 
     * @param password
     *            the password to check
     * @param correctHash
     *            the hash of the valid password
     * @return true if the password is correct, false if not
     */
    public static boolean validatePassword(String password, String correctHash) {
        return validatePassword(password.toCharArray(), correctHash);
    }

    /**
     * Validates a password using a hash.
     * 
     * @param password
     *            the password to check
     * @param correctHash
     *            the hash of the valid password
     * @return true if the password is correct, false if not
     */
    public static boolean validatePassword(char[] password, String correctHash) {
        // Decode the hash into its parameters
        String[] params = correctHash.split(":");
        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
        byte[] salt = fromHex(params[SALT_INDEX]);
        byte[] hash = fromHex(params[PBKDF2_INDEX]);
        // Compute the hash of the provided password, using the same salt,
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return testHash != null ? slowEquals(hash, testHash) : false;
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method is used so that password hashes cannot
     * be extracted from an on-line system using a timing attack and then attacked off-line.
     * 
     * @param a
     *            the first byte array
     * @param b
     *            the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * Computes the PBKDF2 hash of a password.
     * 
     * @param password
     *            the password to hash.
     * @param salt
     *            the salt
     * @param iterations
     *            the iteration count (slowness factor)
     * @param bytes
     *            the length of the hash to compute in bytes
     * @return the PBDKF2 hash of the password or null if any errors occur
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf;
        byte[] result = null;
        try {
            skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            result = skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            log.error("The \"PBKDF2WithHmacSHA1\" encryption algorithm needed to compute the hash could not be found.");
        } catch (InvalidKeySpecException e) {
            log.error("Could not generate hash because the provided \"PBKDF2WithHmacSHA1\" key specification is invalid.");
        }
        return result;
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     * 
     * @param hex
     *            the hex string
     * @return the hex string decoded into a byte array
     */
    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

}
