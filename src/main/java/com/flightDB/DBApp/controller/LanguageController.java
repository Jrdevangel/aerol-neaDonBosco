package com.flightDB.DBApp.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LanguageController {

    @GetMapping("/change-language")
    public String changeLanguage(String lang, HttpServletResponse response) {
        if (!lang.matches("en|es")) {
            return "Invalid language";
        }

        Cookie cookie = new Cookie("lang", lang);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "Language changed to " + lang;
    }
}
