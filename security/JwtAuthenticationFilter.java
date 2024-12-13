/*
package com.paymentauth.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenHelper jwtTokenHelper;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader("Authorization");
        try {
            String username = null;
            String jwtToken = null;
            Authentication authentication = null;
            if (requestToken == null || !requestToken.startsWith("Bearer ")) {
                jwtToken = requestToken.substring(7);
                username = jwtTokenHelper.getUserNameFromToken(jwtToken);
                authentication = SecurityContextHolder.getContext().getAuthentication();
            } else {
                System.out.println("Jwt token is not begin with Bearer ");
            }

            if (username != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenHelper.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Invalid Jwt token");
                }
            }else {
                System.out.println("Username is null or context is not null");
            }

            filterChain.doFilter(request, response);

        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get Jwt token");
        } catch (ExpiredJwtException e) {
            System.out.println("Jwt token is expired");
        } catch (MalformedJwtException e) {
            System.out.println("Jwt token is malformed");
        }

    }
}
*/
