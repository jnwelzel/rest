package com.jonwelzel.web.oauth;

import java.util.List;
import java.util.Map;

import com.jonwelzel.commons.entities.OAuth1Consumer;
import com.jonwelzel.commons.entities.OAuth1Token;

/**
 * Contract for a provider that supports managing OAuth tokens and consumer secrets. The provider should be either
 * defined in the {@link OAuth1ServerFeature} or registered as a standard provider.
 * 
 * @author Martin Matula
 */
public interface OAuth1Provider {

    /**
     * Gets consumer corresponding to a given consumer key.
     * 
     * @param consumerKey
     *            consumer key
     * @return corresponding consumer secret or {@literal null} if no consumer with the given key is known
     */
    OAuth1Consumer getConsumer(String consumerKey);

    /**
     * Creates a new request token for a given consumerKey.
     * 
     * @param consumerKey
     *            consumer key to create a request token for
     * @param callbackUrl
     *            callback url for this request token request
     * @param attributes
     *            additional service provider-specific parameters (this can be used to indicate what level of access is
     *            requested - i.e. readonly, or r/w, etc.)
     * @return new request token
     */
    OAuth1Token newRequestToken(String consumerKey, String callbackUrl, Map<String, List<String>> attributes);

    /**
     * Returns the request token by the consumer key and token value.
     * 
     * @param token
     *            request token value
     * @return request token or {@literal null} if no such token corresponding to a given consumer key is found
     */
    OAuth1Token getRequestToken(String token);

    /**
     * Creates a new access token. This method must validate the passed arguments and return {@literal null} if any of
     * them is invalid.
     * 
     * @param requestToken
     *            authorized request token
     * @param verifier
     *            verifier passed to the callback after authorization
     * @return new access token or null if the arguments are invalid (e.g. there is no such request token as in the
     *         argument, or the verifier does not match)
     */
    OAuth1Token newAccessToken(OAuth1Token requestToken, String verifier);

    /**
     * Returns the access token by the consumer key and token value.
     * 
     * @param token
     *            access token value
     * @return access token or {@literal null} if no such found
     */
    OAuth1Token getAccessToken(String token);
}
