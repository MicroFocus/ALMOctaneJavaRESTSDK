package com.hpe.adm.nga.sdk.network;

/**
 * Helper class to perform token exchange operations.
 */
public final class TokenExchangeHelper {
    private TokenExchangeHelper() {};

    /**
     * Minimal helper to extract "access_token" from JSON string
     * Works without external JSON libraries.
     */
    public static String extractAccessToken(String json) {
        // Look for "access_token":"<token>"
        String key = "\"access_token\"";
        int keyIndex = json.indexOf(key);
        if (keyIndex == -1) return null;

        int colonIndex = json.indexOf(':', keyIndex);
        if (colonIndex == -1) return null;

        int firstQuote = json.indexOf('"', colonIndex + 1);
        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (firstQuote == -1 || secondQuote == -1) return null;

        return json.substring(firstQuote + 1, secondQuote);
    }
}
