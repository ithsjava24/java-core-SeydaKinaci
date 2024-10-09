package org.example.warehouse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Category {
    private String name;
    private static HashMap<String, Category> categories = new HashMap<>();

    private Category(String name) {
        this.name = name;
    }

    public static Category of(String name) {
        if (name != null) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (!categories.containsKey(name)) {
                Category category = new InnerClass(name).getCategory();
                categories.put(name, category);
                return category;
            } else {
                return categories.get(name);
            }

        } else {
            throw new IllegalArgumentException("Category name can't be null");
        }

    }

    public String getName() {
        return name;
    }

    private static class InnerClass {

        public Category category;

        public InnerClass(String name) {
            this.category = new Category(name);
        }

        public Category getCategory() {
            return category;
        }

    }
}
