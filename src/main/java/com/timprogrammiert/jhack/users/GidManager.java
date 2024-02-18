package com.timprogrammiert.jhack.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class GidManager {
    private static Map<Integer, Group> groupsByGid = new HashMap<>();
    private static final int MIN_GID = 1500;
    private static final int MAX_GID = 2000;

    public static Integer generateUid(){
        Random random = new Random();
        Integer uid;
        do {
            uid = random.nextInt(MIN_GID, MAX_GID);
        }while (groupsByGid.containsKey(uid));
        return uid;
    }
    public static void addGroupToList(Group group){
        groupsByGid.put(group.getGroupInfo().getGuid(), group);
    }
}
