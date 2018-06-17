/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class TinyCRMApplication {
    public static void main(String[] args) {
        SpringApplication.run(TinyCRMApplication.class, args);
    }

    // Match everything without a suffix (so not a static resource)
    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirect() {
        // Forward to home page so that route is preserved for React SPA
        return "forward:/";
    }
}
