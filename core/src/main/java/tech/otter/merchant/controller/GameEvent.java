package tech.otter.merchant.controller;

import com.badlogic.gdx.utils.reflect.ClassReflection;

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

    public <T> T get(String key) {
        return get(key, null);
    }

    /**
     * This is like totally not safe.
     * @param key The key to look up
     * @param deflt The default value to return should the key not be present.
     * @param <T> The type of return object.
     * @return
     */
    public <T> T get(String key, T deflt) {
        if(data.containsKey(key)) {
            return (T)data.get(key);
        }
        return deflt;
    }
}
