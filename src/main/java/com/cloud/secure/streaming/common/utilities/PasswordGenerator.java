package com.cloud.secure.streaming.common.utilities;

import org.apache.commons.lang.RandomStringUtils;

public final class PasswordGenerator {
    /**
     * The random number generator.
     */
    private static final java.util.Random r = new java.util.Random();

    /**
     * I, L and O are good to leave out as are numeric zero and one.
     */
    private static final String DIGITS = "0123456789";
    private static final String LOWERCASE_CHARS = "abcdefghjkmnpqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final String SYMBOLS = "~`!@#$%^&*()_+-=[]{};:|,./?><''";
    private static final String ALL = DIGITS + LOWERCASE_CHARS + UPPERCASE_CHARS + SYMBOLS;
    private static final char[] uppercaseArray = UPPERCASE_CHARS.toCharArray();
    private static final char[] lowercaseArray = LOWERCASE_CHARS.toCharArray();
    private static final char[] digitsArray = DIGITS.toCharArray();
    private static final char[] symbolsArray = SYMBOLS.toCharArray();
    private static final char[] allArray = ALL.toCharArray();

    /**
     * Generate a random password based on security rules
     *
     * @return password random
     */
    public static String generatePassword(int length) {
        StringBuilder sb = new StringBuilder();
        if (length < 8) {
            length = 8;
        }
        // get at least one lowercase letter
        sb.append(RandomStringUtils.random(1, lowercaseArray));
        // get at least one uppercase letter
        sb.append(RandomStringUtils.random(1, uppercaseArray));
        // get at least one digit
        sb.append(RandomStringUtils.random(1, digitsArray));
        // get at least one symbol
        sb.append(RandomStringUtils.random(1, symbolsArray));

        // fill in remaining with random letters
        sb.append(RandomStringUtils.random(length - 4, allArray));

        return sb.toString();
    }
}
