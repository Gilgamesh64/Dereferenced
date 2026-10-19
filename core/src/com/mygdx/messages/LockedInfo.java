package com.mygdx.messages;

import java.util.HashMap;

public class LockedInfo {
    private final HashMap<String, Boolean> boolInfo;
    private final HashMap<String, Integer> integerInfo;
    private final HashMap<String, Float> floatInfo;
    private final HashMap<String, String> stringInfo;

    /**
     * Create locked info based on an ObjectInfo instance.
     *
     * @param info an ObjectInfo object to get the Maps from.
     */
    public LockedInfo(ObjectInfo info) {
        boolInfo = info.boolInfo;
        integerInfo = info.integerInfo;
        floatInfo = info.floatInfo;
        stringInfo = info.stringInfo;
    }

    public Boolean getBoolInfo(String key) {
        if (boolInfo == null) return null;
        return boolInfo.get(key);
    }

    public Integer getIntegerInfo(String key) {
        if (integerInfo == null) return null;
        return integerInfo.get(key);
    }

    public Float getFloatInfo(String key) {
        if (floatInfo == null) return null;
        return floatInfo.get(key);
    }

    public String getStringInfo(String key) {
        if (stringInfo == null) return null;
        return stringInfo.get(key);
    }
}
