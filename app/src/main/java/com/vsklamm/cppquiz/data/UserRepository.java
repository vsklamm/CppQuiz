package com.vsklamm.cppquiz.data;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public interface UserRepository {

    void saveCollection(String key, Set<Integer> collection);

    void saveCollection(String key, Map<Integer, Integer> collection);

    LinkedHashSet<Integer> getFromJson(String key);

    HashMap<Integer, Integer> getHashMap(String key);

}
