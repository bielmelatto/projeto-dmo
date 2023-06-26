package com.projeto.organizaidoso.enums;

public enum SharedEnum {
    SHARED_TASKS("SHARED_TASKS");

    private final String key;

    SharedEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
