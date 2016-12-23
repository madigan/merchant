package tech.otter.merchant.controller;

import java.util.HashMap;
import java.util.Map;

public class GameEvent {
    public String type;
    public Map<String, Object> data;

    public GameEvent(String type) {
        this.type = type;
        this.data = new HashMap<>();
    }

    public GameEvent set(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public <T> T get(String key, Class<T> cls) {
        return get(key, cls, null);
    }

    public <T> T get(String key, Class<T> cls, T deflt) {
        if(data.containsKey(key)) {
            if(cls.isInstance(data.get(key))) {
                return cls.cast(data.get(key));
            }
        }
        return deflt;
    }
}
