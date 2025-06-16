package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ProductStorage {
    private static final String FILE_NAME = "products.json";
    private static final Gson gson = new Gson();

    public static List<Product> loadProducts() {
        try {
            Path path = Paths.get(FILE_NAME);
            if (!Files.exists(path)) return new ArrayList<>();
            String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            return gson.fromJson(json, new TypeToken<List<Product>>(){}.getType());
        } catch (IOException e) {
            System.out.println("Error reading product file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveProducts(List<Product> products) {
        try (Writer writer = Files.newBufferedWriter(Paths.get("products.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(products, writer);
        } catch (IOException e) {
            System.err.println("Error saving product data: " + e.getMessage());
        }
    }

}
