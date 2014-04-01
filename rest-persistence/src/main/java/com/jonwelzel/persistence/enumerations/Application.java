package com.jonwelzel.persistence.enumerations;

/**
 * Applications for which the user may provide his resources (personal data) to. These <b>are not</b> third party apps.
 * 
 * @author jwelzel
 * 
 */
public enum Application {

    IDENTITY("https://localhost:8181/identity");

    private Application(String url) {
        this.url = url;
    }

    private String url;

    public String getUrl() {
        return url;
    }

}
