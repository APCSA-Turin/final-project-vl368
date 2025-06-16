package com.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        port(3000);
        staticFiles.location("/public");

        // Test endpoint
        get("/ping", (req, res) -> "Server is alive");

        // Add product
        post("/add-product", (req, res) -> {
            try {
                Product product = new Gson().fromJson(req.body(), Product.class);
                Document doc = Scraper.scrape(product.getUrl());
                Scraper.checkPrice(doc, product);
                ProductStorage.addProduct(product);
                return "Product added!";
            } catch (Exception e) {
                res.status(500);
                return "Error: " + e.getMessage();
            }
        });

        // Background checker (simplified)
        new Thread(() -> {
            while (true) {
                try {
                    List<Product> products = ProductStorage.loadProducts();
                    System.out.println("Checking " + products.size() + " products...");

                    for (Product p : products) {
                        try {
                            Document doc = Scraper.scrape(p.getUrl());
                            Scraper.checkPrice(doc, p);
                        } catch (Exception e) {
                            System.err.println("Failed to check " + p.getUrl() + ": " + e.getMessage());
                        }
                        Thread.sleep(5000); // 5s delay between products
                    }

                    ProductStorage.saveProducts(products);
                    Thread.sleep(300000); // 5min delay between full cycles
                } catch (Exception e) {
                    System.err.println("Background thread error: " + e.getMessage());
                }
            }
        }).start();
    }
}