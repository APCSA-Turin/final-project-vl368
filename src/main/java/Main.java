package com.example;

import static spark.Spark.*;

import com.google.gson.Gson;
import org.jsoup.nodes.Document;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Serve static files (index.html, etc.)
        staticFiles.externalLocation("public");

        // Endpoint to add a product via POST /add-product
        post("/add-product", (req, res) -> {
            Gson gson = new Gson();
            Product newProduct = gson.fromJson(req.body(), Product.class);

            List<Product> products = ProductStorage.loadProducts();
            newProduct.setAlertSent(false);
            newProduct.setPrice(0); // initialize current price
            products.add(newProduct);
            ProductStorage.saveProducts(products); // completely overwrites the file

            res.type("text/plain");
            return "Product added successfully!";
        });

        // Background job when pinged (e.g. by Uptime Robot)
        get("/run-scraper", (req, res) -> {
            List<Product> products = ProductStorage.loadProducts();
            boolean updated = false;

            for (Product p : products) {
                double previousPrice = p.getPrice();  
                
                Document doc = Scraper.scrape(p.getUrl());
                Scraper.checkPrice(doc, p);

                if (p.getTargetPrice() >= p.getPrice() && !p.isAlertSent()) {
                    Mail.sendMail(p.getEmail(), p);
                    p.setAlertSent(true);
                    updated = true;
                } else if (p.getPrice() != previousPrice) {
                    updated = true;
                }
            }

            if (updated) {
                ProductStorage.saveProducts(products);
            }

            return "Scraping complete.";
        });
    }
}
