package com.vinay.spring_boot_assignment.api_gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;


@Component
public class JwtUtil {
    public static final String SECRET = "3ea841983b91224be6d03e20ccef470c28d127a045d538add683288e3baf2c649fd335c692430265988ae18b4d150933ffc8dfa15303c328ae322dbed88aa170a21f1c4f8aa9d24616a016a1a125970dba27e1091d0bf2749228727251f811ea108059b1774acb59308dbbb7fb90d0e742570ce33e846d1481a9ac15705a7270";

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
