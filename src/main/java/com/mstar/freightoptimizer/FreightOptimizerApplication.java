package com.mstar.freightoptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FreightOptimizerApplication {

    static {
        try {
            System.loadLibrary("jniortools"); // OR-Tools JNI bridge
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load OR-Tools native lib: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(FreightOptimizerApplication.class, args);
    }

}
