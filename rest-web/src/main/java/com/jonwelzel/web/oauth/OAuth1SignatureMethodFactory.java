package com.jonwelzel.web.oauth;

import javax.inject.Inject;

public class OAuth1SignatureMethodFactory {

    @Inject
    private static HmaSha1Method hmaSha1;

    @Inject
    private static RsaSha1Method rsaSha1;

    @Inject
    private static PlaintextMethod plaintext;

    public static OAuth1SignatureMethod getMethod(String method) {
        switch (method) {
        case HmaSha1Method.NAME:
            return hmaSha1;
        case RsaSha1Method.NAME:
            return rsaSha1;
        case PlaintextMethod.NAME:
            return plaintext;
        default:
            break;
        }
        return null;
    }

}
