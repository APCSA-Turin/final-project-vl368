package com.example;
import com.example.Product;


import java.util.Arrays;
import java.util.List;

public class Proxy {
    // instance variables
    private String IP;
    private int port;

    // proxy constructor
    public Proxy(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    // getter methods
    public String address() {
        return IP;
    }

    public int port() {
        return port;
    }

    // list of proxies
    private static final List<Proxy> PROXIES = Arrays.asList(
        new Proxy("43.134.57.33", 7654),
        new Proxy("47.245.117.43", 80),
        new Proxy("67.43.236.18", 3927),
        new Proxy("43.159.39.73", 3654),
        new Proxy("14.229.120.170", 8080),
        new Proxy("171.239.237.199", 10010),
        new Proxy("65.108.203.35", 28080),
        new Proxy("20.210.39.153", 8561),
        new Proxy("20.27.11.248", 8561),
        new Proxy("20.210.39.153", 8561),
        new Proxy("194.4.57.199", 3128),
        new Proxy("45.56.113.27", 17981),
        new Proxy("14.229.120.170", 8080),
        new Proxy("201.150.119.170", 999),
        new Proxy("49.12.169.69", 3200),
        new Proxy("45.170.226.250", 999),
        new Proxy("38.250.126.201", 999),
        new Proxy("23.237.210.82", 80),
        new Proxy("187.249.114.252", 8080),
        new Proxy("27.79.129.62", 16000),
        new Proxy("67.43.236.19", 26025),
        new Proxy("43.156.236.142", 7654),
        new Proxy("188.69.231.89", 8080)
    );

    // retrieves a random proxy
    public static Proxy getRandomProxy() {
        return PROXIES.get((int) (Math.random()*PROXIES.size()));
    }
}
