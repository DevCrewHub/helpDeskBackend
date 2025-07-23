package com.helpdesk.config;

import com.helpdesk.services.jwt.UserService;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

/**
 * This filter is responsible for intercepting incoming HTTP requests and validating JWT tokens.
 * It runs once per request using Spring Security's OncePerRequestFilter.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil; // Utility class for JWT operations like validation and extraction
	private final UserService userService; // Service to load user details based on username

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		// Extract the Authorization header from the incoming request
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;

		// If the Authorization header is missing or does not start with "Bearer ", continue without authentication
		if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// Extract JWT token by removing the "Bearer " prefix
		jwt = authHeader.substring(7);

		// Extract the username (email) from the token
		userEmail = jwtUtil.extractUserName(jwt);

		// Proceed only if username is present and there is no authentication set in the context
		if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {

			// Load user details from the database
			UserDetails userDetails = userService.userDetailService().loadUserByUsername(userEmail);

			// Validate the token against user details
			if (jwtUtil.isTokenValid(jwt, userDetails)) {
				// Create a new empty security context
				SecurityContext context = SecurityContextHolder.createEmptyContext();

				// Create an authentication token using the user details and authorities
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				// Set additional authentication details from the request
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set the authentication in the context
				context.setAuthentication(authToken);
				SecurityContextHolder.setContext(context);
			}
		}

		// Continue with the filter chain
		filterChain.doFilter(request, response);
	}

	/**
	 * This method determines which requests should bypass this filter.
	 * In this case, authentication-related endpoints (like login or register) are excluded.
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getServletPath();
		return path.startsWith("/api/auth/");
	}
}
