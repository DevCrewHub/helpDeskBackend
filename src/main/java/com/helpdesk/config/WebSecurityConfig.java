package com.helpdesk.config;

//import com.helpdesk.enums.UserRole;
import com.helpdesk.services.jwt.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Main security configuration class for the application.
 * Configures JWT-based security, role-based access, and CORS settings.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter; // Custom filter to validate JWTs
	private final UserService userService; // Custom user details service

	/**
	 * Defines the security filter chain for handling HTTP requests.
	 * Configures authentication, authorization, session management, and filters.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection (commonly used with JWTs)
			.cors(cors -> { }) // Enable CORS configuration
			.authorizeHttpRequests(request -> request
				.requestMatchers("/api/auth/**").permitAll() // Public access to auth endpoints
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Public access to Swagger docs
				.requestMatchers("/api/admin/**").hasRole("ADMIN") // Only accessible by ADMIN role
				.requestMatchers("/api/customer/**").hasRole("CUSTOMER") // Only accessible by CUSTOMER role
				.requestMatchers("/api/agent/**").hasRole("AGENT") // Only accessible by AGENT role
				.requestMatchers("/api/comments/**").authenticated() // Requires authentication for comment APIs
				.anyRequest().authenticated()) // All other requests must be authenticated
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // Use stateless session (JWT)
			.authenticationProvider(authenticationProvider()) // Use custom authentication provider
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before default filter

		return http.build(); // Build and return the configured SecurityFilterChain
	}

	/**
	 * Configures allowed origins, methods, headers, and credentials for CORS requests.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of(
			"http://localhost:5173", 
			"https://helpdesk-support-system.netlify.app")); // Frontend URLs allowed to access backend
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
		config.setAllowedHeaders(List.of("*")); // Allow all headers
		config.setAllowCredentials(true); // Allow cookies/authorization headers
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); // Apply configuration to all paths
		return source;
	}

	/**
	 * Configures the password encoder used for encoding and verifying passwords.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Uses BCrypt hashing algorithm
	}

	/**
	 * Defines a custom authentication provider using the application's UserService.
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService.userDetailService()); // Custom user details service
		authProvider.setPasswordEncoder(passwordEncoder()); // Set the password encoder
		return authProvider;
	}

	/**
	 * Provides an authentication manager bean using the given AuthenticationConfiguration.
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager(); // Returns the authentication manager
	}
}
