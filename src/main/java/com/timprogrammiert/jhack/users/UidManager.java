package com.timprogrammiert.jhack.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class UidManager {
    private static Map<Integer, User> userByUid = new HashMap<>();
    private static final int MIN_UID = 100;
    private static final int MAX_UID = 2000;

    public static Integer generateUid(){
        Random random = new Random();
        Integer uid;
        do {
            uid = random.nextInt(MIN_UID, MAX_UID);
        }while (userByUid.containsKey(uid));
        return uid;
    }
    public static void addUserToList(User user){
        userByUid.put(user.getAccountInfo().getUid(), user);
    }
}
