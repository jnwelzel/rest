package com.jonwelzel.web.security;

import java.security.NoSuchAlgorithmException;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.ValueGenerator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import com.jonwelzel.util.SecurityUtils;

public class TokenGenerator implements ValueGenerator {

    @Override
    public String generateValue() throws OAuthSystemException {
        String result;
        try {
            result = SecurityUtils.generateSecureHex();
        } catch (NoSuchAlgorithmException e) {
            result = new MD5Generator().generateValue();
        }
        return result;
    }

    @Override
    public String generateValue(String param) throws OAuthSystemException {
        return generateValue();
    }

}
