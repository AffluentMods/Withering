package me.affluent.decay.superclass;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;
import java.util.TreeMap;

public abstract class Descriptable extends ListenerAdapter {

    private static Map<String, String> descriptions = new TreeMap<>();

    public String name;

    protected void setName(String name) {
        name = name.toLowerCase().replaceAll(" ", "_");
        this.name = name;
        if (description != null) descriptions.put(name, description);
    }

    public String getName() {
        return name;
    }

    private String description;

    protected void setDescription(String description) {
        this.description = description;
        if (name != null) descriptions.put(name, description);
    }

    public String getDescription() {
        return description;
    }

    public static Map<String, String> getDescriptions() {
        return descriptions;
    }
}