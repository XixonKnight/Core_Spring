package com.example.corespring.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by NhanNguyen on 4/22/2021
 *
 * @author : NhanNguyen
 * @since : 4/22/2021
 */
@Service
@Slf4j
public class JwtUtil implements Serializable {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    //    get username from jwt token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //    retrieve expiration date from jwt token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        try {
            return claimsResolver.apply(claims);
        } catch (Exception e) {
//            throw new JwtException("ExpiredJwtException");
            log.error("{}: ExpiredJwtException", e.getMessage());
            return null;
        }
    }

    //    for retrieving any information from token we will need the secret key
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //    check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //    generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    //    while creating the token -
//    1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//    2. Sign the JWT using the HS512 algorithm and secret key.
//    3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//    compaction of the JWT to a URL-safe string
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .setIssuer("Xixon-Knight")
                .setHeaderParam("tokenType", "Bearer ")
                .setAudience("You")
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }
    // Get the signing key for JWT token
// Get the signing key for JWT token
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}