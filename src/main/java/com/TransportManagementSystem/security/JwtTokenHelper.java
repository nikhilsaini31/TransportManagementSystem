package com.TransportManagementSystem.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenHelper {
	
public static final long JWT_TOKEN_VALIDITY= 5 * 60 * 60; // 5 hours
	
	private String SECRET_KEY ="afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf"; 
	 
	// retrieve/get username from jwt token 
	public String getUserNameFromToken(String token) {
		
		return getClaimFromToken(token,Claims::getSubject); 
	}
	
	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		
		return getClaimFromToken(token,Claims::getExpiration);
	}
	
	//create  getClaimFromToken method
	public <T> T getClaimFromToken(String token,Function<Claims, T> claimsResolver) {
		
		final Claims claims=getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information form token we will need th secret key
	private Claims getAllClaimsFromToken(String token) {
		
		return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
	}
	
	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date()); 
	}
	
	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claims=new HashMap<>();
		return createToken(claims,userDetails.getUsername());
	}

	
    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string

	private String createToken(Map<String, Object> claims, String subject) {
		
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY * 1000)).signWith(SignatureAlgorithm.HS512,SECRET_KEY).compact();
	}
	
	// validate token 
	public Boolean validateToken(String token,UserDetails userDetails) {
		
		final String username=getUserNameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
