package com.mills.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryan on 22/10/16.
 */
public enum ResponseEnum {

    YES,
    NO,
    MAYBE,
    NO_RESPONSE;

    private static Map<String, ResponseEnum> namesMap = new HashMap<>();

    static {
        for(ResponseEnum responseEnum : ResponseEnum.values()) {
            namesMap.put(responseEnum.toString().toLowerCase(), responseEnum);
        }
    }

    @JsonCreator
    public static ResponseEnum forValue(String value) {
        return namesMap.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, ResponseEnum> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null;
    }

}
