package com.example;

import org.jsoup.Jsoup;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.example.Product;

// code from https://www.zenrows.com/blog/web-scraping-java#connect-to-target-website
// proxies from https://free-proxy-list.net/
// proxy code from https://www.zenrows.com/blog/jsoup-403#ip-rotation
// element doc: https://docs.oracle.com/javase/8/docs/api/org/w3c/dom/Element.html
// elements doc: https://docs.oracle.com/javase/8/docs/api/javax/lang/model/util/Elements.html
// https://stackoverflow.com/questions/56073103/how-to-get-json-data-from-html-using-jsoup
// https://jsoup.org/cookbook/extracting-data/selector-syntax
// https://jsoup.org/apidocs/org/jsoup/Connection.html
// https://www.baeldung.com/java-with-jsoup

public class Scraper {

    public static Document scrape(String url) {
        // initalizes the userAgent
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36";
        // initalizes the document just to prevent compiler error message
        Document doc = new Document(url);
        // retry mechanism in case of failure
        int attempts = 0;
        boolean success = false;
        while (!success && attempts <= 5) {
            attempts++;
            if (attempts >= 5) {
                System.err.println("Failed to connect after 5 proxy attempts: " + url);
            }
            try {
                // retrieves a random proxy
                Proxy proxy = Proxy.getRandomProxy();
                // generates a random time for the timeout
                // random timeout to avoid getting blocked 
                int randTime = (int)(Math.random()*(2000+1))+1000;
                // set up the connection options with Jsoup
                // a bunch of extra like connection settings to make it work
                // otherwise it would just keep throwing http 404 errors forever
                doc = Jsoup
                        .connect(url)
                        .ignoreContentType(true) 
                        .proxy(proxy.address(), proxy.port())
                        .timeout(randTime)
                        .userAgent(userAgent)
                        .get();
                // String htmlOutput = doc.outerHtml();
                success = true; // exit the loop if successful 
                return doc;
            } catch (IOException e) {
                // if jsoup throws an exception, change proxies via random proxy
                System.out.println("Rotating proxies");
            }
        }
        return doc;
    }

    // parse document from scrape() into a product object
public static void parseDoc(Document doc, String url) {
    Product product = new Product();
    product.setUrl(url);

    // try selectors to find product name
    Element nameElement = doc.selectFirst("h1, .product-title, .pdp-title, .product-name");
    // checks if any value was found
    if (nameElement != null) {
        // sets the name to only the name text and cleaning up the quotation marks (from being a string)
        product.setName(nameElement.text().replace("\"", ""));
    } 
    else {
        // if value not found
        product.setName("Unknown Product Name");
    }

    // try selectors to find price
    Element priceElement = doc.selectFirst(".product-price, .price, .product-price-container, #price, .pdp-price");
    // checks if any value was found
    String priceText = null;

    if (priceElement != null) {
        priceText = priceElement.text();
    }

    if (priceText != null && !priceText.isEmpty()) {
        double price = Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
        product.setPrice(price);
    }

    else {
        // if value not found
        product.setPrice("Unknown Price");
    }

    // try selectors to find image
    Element imgContainer = doc.selectFirst("img.product-image, img.main-image, .pdp-mobile-image-slider img, .product-gallery img");
    // checks if value is found
    if (imgContainer != null) {
        // adds it to product object 
        String imgSrc = imgContainer.absUrl("src");
        product.setImage(imgSrc);
    }

    System.out.println(product);
    }

    public static void checkPrice(Document doc, Product product) {

        // try selectors to find price
        Element priceElement = doc.selectFirst(".product-price, .price, .product-price-container, #price, .pdp-price");
        // checks if any value was found
        String priceText = null;

        if (priceElement != null) {
            priceText = priceElement.text();
        }

        if (priceText != null && !priceText.isEmpty()) {
            double price = Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
            product.setPrice(price);
        }
        else {
            // if value not found
            product.setPrice("Unknown Price");
        }
    }
}
