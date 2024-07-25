package com.TransportManagementSystem.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// get token                             // key 
				String requestToken = request.getHeader("Authorization"); 
				
			//	System.out.println(requestToken);
				
				String userName=null;
				
				String token=null;
				
		 		if(requestToken!=null && requestToken.startsWith("Bearer")) { // token with Bearer
					
					token = requestToken.substring(7); // token after remove Bearer

					try {
						userName = this.jwtTokenHelper.getUserNameFromToken(token);
					}
					catch (IllegalArgumentException  e) {
						System.out.println("unable to get jwt token");
					   
					} 
					catch (ExpiredJwtException e) {
						System.out.println("jwt token has expired");
					}
					catch (MalformedJwtException e) {
						System.out.println("invalid jwt");
					}
					catch (Exception e) {
				        e.printStackTrace();   
					}
					
					 
				}else {
					
					System.out.println("jwt token does not begin with Bearer");
					logger.info("invlid header value");
				}
				
				
				// once we get the token , now validate
				
		 		// means user null nhi h or user ko abhi  authentication set nhi hua h (null h)
		 		if(userName !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
		 			
		 			
		 			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
		 			
		 			if(this.jwtTokenHelper.validateToken(token, userDetails)) { // if token is valid then it will validate the token and return true 
		 				
		 				// now do authentication
		 				
		 				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
		 				
		 				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		 				
		 				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		 				
		 			}else {
						
		 				System.out.println("invalid jwt token");
		 				
					}	
		 			
		 		}else {
					
		 			System.out.println("username is invalid or context is not null");
				}
				
		 		
		 		filterChain.doFilter(request, response);
			}
		
	}


