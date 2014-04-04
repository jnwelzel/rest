package com.jonwelzel.web.oauth;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.MessageFormat;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;

/**
 * An OAuth signature method that implements RSA-SHA1.
 * 
 * @author Hubert A. Le Van Gong <hubert.levangong at Sun.COM>
 * @author Paul C. Bryan <pbryan@sun.com>
 */
public final class RsaSha1Method implements OAuth1SignatureMethod {

    @Inject
    @Log
    private Logger log;

    private static final String ERROR_INVALID_CONSUMER_SECRET = "Invalid consumer secret: {0}";
    private static final String ERROR_CANNOT_OBTAIN_PUBLIC_KEY = "Couldn't obtain public key from consumer secret.";

    public static final String NAME = "RSA-SHA1";

    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    private static final String KEY_TYPE = "RSA";

    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE";

    @Override
    public String name() {
        return NAME;
    }

    /**
     * Generates the RSA-SHA1 signature of OAuth request elements.
     * 
     * @param baseString
     *            the combined OAuth elements to sign.
     * @param secrets
     *            the secrets object containing the private key for generating the signature.
     * @return the OAuth signature, in base64-encoded form.
     * @throws InvalidSecretException
     *             if the supplied secret is not valid.
     */
    @Override
    public String sign(final String baseString, final OAuth1Secrets secrets) throws InvalidSecretException {

        final Signature signature;
        try {
            signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (final NoSuchAlgorithmException nsae) {
            throw new IllegalStateException(nsae);
        }

        byte[] decodedPrivateKey;
        try {
            decodedPrivateKey = Base64.decode(secrets.getConsumerSecret());
        } catch (final IOException ioe) {
            throw new InvalidSecretException(MessageFormat.format(ERROR_INVALID_CONSUMER_SECRET, ioe));
        }

        final KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(KEY_TYPE);
        } catch (final NoSuchAlgorithmException nsae) {
            throw new IllegalStateException(nsae);
        }

        final EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);

        final RSAPrivateKey rsaPrivateKey;
        try {
            rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (final InvalidKeySpecException ikse) {
            throw new IllegalStateException(ikse);
        }

        try {
            signature.initSign(rsaPrivateKey);
        } catch (final InvalidKeyException ike) {
            throw new IllegalStateException(ike);
        }

        try {
            signature.update(baseString.getBytes());
        } catch (final SignatureException se) {
            throw new IllegalStateException(se);
        }

        final byte[] rsasha1;

        try {
            rsasha1 = signature.sign();
        } catch (final SignatureException se) {
            throw new IllegalStateException(se);
        }

        return Base64.encode(rsasha1);
    }

    /**
     * Verifies the RSA-SHA1 signature of OAuth request elements.
     * 
     * @param elements
     *            OAuth elements signature is to be verified against.
     * @param secrets
     *            the secrets object containing the public key for verifying the signature.
     * @param signature
     *            base64-encoded OAuth signature to be verified.
     * @throws InvalidSecretException
     *             if the supplied secret is not valid.
     */
    @Override
    public boolean verify(final String elements, final OAuth1Secrets secrets, final String signature)
            throws InvalidSecretException {

        final Signature sig;

        try {
            sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (final NoSuchAlgorithmException nsae) {
            throw new IllegalStateException(nsae);
        }

        RSAPublicKey rsaPubKey = null;

        final String tmpkey = secrets.getConsumerSecret();
        if (tmpkey.startsWith(BEGIN_CERT)) {
            try {
                Certificate cert = null;
                final ByteArrayInputStream bais = new ByteArrayInputStream(tmpkey.getBytes());
                final BufferedInputStream bis = new BufferedInputStream(bais);
                final CertificateFactory certfac = CertificateFactory.getInstance("X.509");
                while (bis.available() > 0) {
                    cert = certfac.generateCertificate(bis);
                }
                rsaPubKey = (RSAPublicKey) cert.getPublicKey();
            } catch (final Exception ex) {
                log.error(ERROR_CANNOT_OBTAIN_PUBLIC_KEY, ex);
                return false;
            }
        }

        final byte[] decodedSignature;
        try {
            decodedSignature = Base64.decode(signature);
        } catch (final IOException e) {
            return false;
        }

        try {
            sig.initVerify(rsaPubKey);
        } catch (final InvalidKeyException ike) {
            throw new IllegalStateException(ike);
        }

        try {
            sig.update(elements.getBytes());
        } catch (final SignatureException se) {
            throw new IllegalStateException(se);
        }

        try {
            return sig.verify(decodedSignature);
        } catch (final SignatureException se) {
            throw new IllegalStateException(se);
        }
    }
}
