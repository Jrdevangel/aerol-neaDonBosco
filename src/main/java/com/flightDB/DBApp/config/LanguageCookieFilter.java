package com.flightDB.DBApp.config;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.util.Locale;

public class LanguageCookieFilter implements Filter {

    private static final String LANGUAGE_COOKIE_NAME = "lang";
    private static final String DEFAULT_LANGUAGE = "en";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Cookie[] cookies = httpRequest.getCookies();
        String language = DEFAULT_LANGUAGE;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (LANGUAGE_COOKIE_NAME.equals(cookie.getName())) {
                    language = cookie.getValue();
                }
            }
        }

        Locale locale = new Locale(language);
        LocaleContextHolder.setLocale(locale);
        Cookie languageCookie = new Cookie(LANGUAGE_COOKIE_NAME, language);
        languageCookie.setPath("/");
        languageCookie.setHttpOnly(true);
        httpResponse.addCookie(languageCookie);
        chain.doFilter(request, response);
    }
}