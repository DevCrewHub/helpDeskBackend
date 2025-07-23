package com.helpdesk.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.helpdesk.entities.User;
import com.helpdesk.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

	private final UserRepository userRepository;

	// Generates a JWT token for the given user details
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	// Generates a JWT token with additional claims
	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(extraClaims) // set extra claims
				.setSubject(userDetails.getUsername()) // set subject as username
				.setIssuedAt(new Date(System.currentTimeMillis())) // current time as issued date
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // expires in 24 hours
				.signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign with key and HS256 algorithm
				.compact(); // build the token
	}

	// Returns the signing key used to sign the JWT
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode("413F442847284862506553685660597033733676397924422645294848406351");
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Validates the JWT token for the given user details
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	// Extracts the username (subject) from the token
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Checks if the token is expired
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Extracts the expiration date from the token
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Extracts a specific claim from the token using a function
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Extracts all claims from the JWT token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey()) // set the signing key
				.build()
				.parseClaimsJws(token)
				.getBody(); // get the token's body (claims)
	}

	// Retrieves the currently authenticated user from the security context
	public User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName(); // get username from authentication
			Optional<User> optionalUser = userRepository.findByUserName(username);
			return optionalUser.orElse(null); // return user or null if not found
		}

		return null; // no authenticated user
	}
}
