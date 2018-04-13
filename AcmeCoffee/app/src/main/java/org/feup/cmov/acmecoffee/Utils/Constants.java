package org.feup.cmov.acmecoffee.Utils;

public class Constants {
    public static String WRONG_EMAIL = "The email was already registered or is not valid.";
    public static String WRONG_PASSWORDS = "The passwords does not match or are invalid.";
    public static String WRONG_NIF = "NIF needs to have exactly 9 numbers.";
    public static String WRONG_REGISTER = "Something went wrong to sign up.";
    public static String WRONG_FIELDS_LOGIN = "Your email or password are not correct.";
    public static String VOUCHERS_LOAD_SUCCESS = "Vouchers were loaded.";
    public static String VOUCHERS_LOAD_ERROR = "Couldn't load the vouchers.";
    public static String ITEMS_LOAD_SUCCESS = "Menu was loaded.";
    public static String ITEMS_LOAD_ERROR = "Couldn't load the menu.";
    public static String COULDNT_GET_ITEMS = "Something went wrong to get items menu.";

    public static final int KEY_SIZE = 512;                              // minimum size for a RSA key in the Android keystore
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";     // Android keystore provider
    public static String keyname = "myIdKey";                            // Just a name for accessing this application keys in the keystore
}
