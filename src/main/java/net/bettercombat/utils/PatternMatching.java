package net.bettercombat.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatching {
    private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap<>();

    public static boolean matches(String subject, String nullableRegex) {
        if (subject == null) {
            return false;
        }
        if (nullableRegex == null || nullableRegex.isEmpty()) {
            return false;
        }
        Pattern pattern = PATTERN_CACHE.computeIfAbsent(nullableRegex,
                r -> Pattern.compile(r, Pattern.CASE_INSENSITIVE));
        Matcher matcher = pattern.matcher(subject);
        return matcher.find();
    }
}
