/*******************************************************************************
 * AuthenticationAPIKeyFilter.java
 *
 * Copyright (c) 2012 SeedBoxer Team.
 *
 * This file is part of SeedBoxer.
 *
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.seedboxer.web.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.seedboxer.core.domain.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class AuthenticationAPIKeyFilter extends GenericFilterBean {

	private static final String APIKEY_PARAM = "apikey";
	private final AuthenticationDetailsSource<HttpServletRequest,?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Autowired
	private AuthenticationAPIKeyEntryPoint authenticationEntryPoint;

	@Autowired
	private SeedBoxerUDS seedboxerUDS;

	public void setSeedboxerUDS(SeedBoxerUDS seedboxerUDS) {
		this.seedboxerUDS = seedboxerUDS;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        @SuppressWarnings("unchecked")
        Map<String, String[]> parms = request.getParameterMap();

        if(parms.containsKey(APIKEY_PARAM)) {
            String apikey = parms.get(APIKEY_PARAM)[0];

            if (Token.validate(apikey)) {
            	try {

	                UserDetails userDetails = seedboxerUDS.loadUserByAPIKey(apikey);
	                Authentication authentication = createSuccessfulAuthentication(request, userDetails);
	                SecurityContextHolder.getContext().setAuthentication(authentication);

	            } catch (UsernameNotFoundException notFound) {
	            	fail(request, response,
	            			new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.usernameNotFound",
	            					new Object[]{apikey}, "User with APIKey {0} not found")));

	            	return;
	            }
            } else {
            	fail(request, response,
            			new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.usernameNotFound",
            					new Object[]{apikey}, "Invalid APIKey {0}")));

            	return;
            }
        }
        chain.doFilter(request, response);
	}

    private Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        UsernamePasswordAuthenticationToken authRequest
        	= new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        return authRequest;
    }


    private void fail(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(null);

        authenticationEntryPoint.commence(request, response, failed);
    }

}
