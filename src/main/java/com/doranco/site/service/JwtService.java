package com.doranco.site.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service responsable de la gestion des tokens JWT, y compris la génération, l'extraction des informations et la validation des tokens.
 */
@Component
public class JwtService {

    /**
     * Clé secrète utilisée pour signer les tokens JWT.
     */
    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
    
    /**
     * Extrait le nom d'utilisateur (sujet) du token JWT.
     *
     * @param token le token JWT.
     * @return le nom d'utilisateur extrait du token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrait la date d'expiration du token JWT.
     *
     * @param token le token JWT.
     * @return la date d'expiration du token.
     */
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extrait une réclamation spécifique du token JWT en utilisant un convertisseur.
     *
     * @param <T> le type de la réclamation.
     * @param token le token JWT.
     * @param claimConverter la fonction qui convertit les réclamations.
     * @return la réclamation extraite.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimConverter) {
        Claims claims = extractAllClaims(token);
        return claimConverter.apply(claims);
    }
    
    /**
     * Extrait toutes les réclamations (claims) du token JWT.
     *
     * @param token le token JWT.
     * @return les réclamations contenues dans le token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si le token JWT est expiré.
     *
     * @param token le token JWT.
     * @return vrai si le token est expiré, faux sinon.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }
    
    /**
     * Valide un token JWT en vérifiant le nom d'utilisateur et l'expiration.
     *
     * @param token le token JWT.
     * @param userDetails les détails de l'utilisateur à vérifier.
     * @return vrai si le token est valide, faux sinon.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Génère un token JWT pour un email spécifié.
     *
     * @param email l'email pour lequel générer le token.
     * @return le token JWT généré.
     */
    public String GenerateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(email, claims);
    }

    /**
     * Crée un token JWT avec les réclamations fournies et l'email.
     *
     * @param email l'email pour lequel générer le token.
     * @param claims les réclamations à inclure dans le token.
     * @return le token JWT généré.
     */
    private String createToken(String email, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1)) // Expiration du token dans 1 minute
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtient la clé de signature utilisée pour signer les tokens JWT.
     *
     * @return la clé de signature.
     */
    public Key getSignKey() {
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }
}
