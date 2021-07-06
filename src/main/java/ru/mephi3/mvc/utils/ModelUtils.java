package ru.mephi3.mvc.utils;

import org.springframework.ui.Model;

public class ModelUtils {
    public static void addIfNotExist(Model model, String name, Object object) {
        if (model == null)
            throw new IllegalArgumentException("Model is not null");
        if (!model.containsAttribute(name))
            model.addAttribute(name, object);
    }
}
