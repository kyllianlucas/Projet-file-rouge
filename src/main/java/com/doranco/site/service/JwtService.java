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


@Component
public class JwtService {
	public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
	
	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	public Date extractExpirationDate(String token) {
		return extractClaim(token,Claims::getExpiration);
	}
	public <T> T extractClaim(String token, Function<Claims,T> claimConverter){
		Claims claims = extractAllClaims(token);
		return claimConverter.apply(claims);
	}
	public Claims extractAllClaims(String token) {
		return Jwts
		.parserBuilder()
		.setSigningKey(getSignKey())
		.build()
		.parseClaimsJws(token)
		.getBody();
	}
	 private Boolean isTokenExpired(String token) {
		 return extractExpirationDate(token).before(new Date());
	 }
	 public Boolean validateToken(String token, UserDetails userDetails)	{
		 final String username = extractUsername(token);
		 return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	 }
	 public String GenerateToken(String email){
		 Map<String, Object> claims = new HashMap<>();
		 return createToken(email, claims);
	 }
	 private String createToken(String email,Map<String,Object> claims) {
		return Jwts.builder()
		 .setClaims(claims)
		 .setSubject(email)
		 .setIssuedAt(new Date(System.currentTimeMillis()))
		 .setExpiration(new Date(System.currentTimeMillis()
		+1000*60*1))
		 .signWith(getSignKey(), 
		SignatureAlgorithm.HS256).compact();
	}
 	public Key getSignKey() {
		byte[] key = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(key);
	}
}

