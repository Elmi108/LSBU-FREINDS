package com.android.lsbufriends.utils.constants;

public class NameConstants {
    public static final String USER_REF = "User";
    public static final String POSTS_REF = "Posts";
    public static final String COMMENTS_REF = "Comments";
    // digit + lowercase char + uppercase char + punctuation + symbol
    public static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
}
