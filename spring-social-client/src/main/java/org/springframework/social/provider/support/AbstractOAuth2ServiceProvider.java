/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.provider.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.social.provider.AuthorizationProtocol;
import org.springframework.social.provider.AuthorizedRequestToken;
import org.springframework.social.provider.OAuthToken;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Base class for ServiceProvider implementations that use OAuth 2
 * authorization.
 * 
 * @author Craig Walls
 * @param <S> The service API hosted by this service provider.
 */
public abstract class AbstractOAuth2ServiceProvider<S> extends AbstractServiceProvider<S> {

	public AbstractOAuth2ServiceProvider(ServiceProviderParameters parameters,
			AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	public AccessToken fetchNewRequestToken(String callbackUrl) {
		throw new UnsupportedOperationException(
				"You may not fetch a request token for an OAuth 2-based service provider");
	}

	public void connect(Serializable accountId, AuthorizedRequestToken requestToken) {
		throw new UnsupportedOperationException(
				"Connections with request token are not supported for an OAuth 2-based service provider");
	}

	public void connect(Serializable accountId, String redirectUri, String code) {
		connect(accountId, fetchAccessToken(redirectUri, code));
	}

	public AccessToken fetchAccessToken(AuthorizedRequestToken requestToken) {
		throw new UnsupportedOperationException(
				"Fetching access tokens with request token are not supported for an OAuth 2-based service provider");
	}

	public AccessToken fetchAccessToken(String redirectUri, String code) {
		Map<String, String> tokenRequestParameters = new HashMap<String, String>();
		tokenRequestParameters.put("client_id", parameters.getApiKey());
		tokenRequestParameters.put("client_secret", parameters.getSecret());
		tokenRequestParameters.put("code", code);
		tokenRequestParameters.put("redirect_uri", redirectUri);
		tokenRequestParameters.put("grant_type", "authorization_code");
		AccessToken accessToken = fetchOAuth2AccessToken(tokenRequestParameters);
		return accessToken;
	}

	@Override
	public void refreshConnection(Serializable accountId, String providerAccountId) {
		String refreshToken = connectionRepository.getRefreshToken(accountId, getId(), providerAccountId);
		if (refreshToken == null) {
			throw new UnsupportedOperationException("Connection refresh is not supported for this provider.");
		}

		// TODO : Look into reducing some of the duplication between this method
		// and the connect() method above
		Map<String, String> tokenRequestParameters = new HashMap<String, String>();
		tokenRequestParameters.put("client_id", parameters.getApiKey());
		tokenRequestParameters.put("client_secret", parameters.getSecret());
		tokenRequestParameters.put("refresh_token", refreshToken);
		tokenRequestParameters.put("grant_type", "refresh_token");
		AccessToken accessToken = fetchOAuth2AccessToken(tokenRequestParameters);
		S serviceOperations = createServiceOperations(accessToken);
		String username = fetchProviderAccountId(serviceOperations);
		connectionRepository.updateConnection(accountId, getId(), accessToken, username);
	}

	public AuthorizationProtocol getAuthorizationProtocol() {
		return AuthorizationProtocol.OAUTH_2;
	}

	protected AccessToken fetchOAuth2AccessToken(Map<String, String> tokenRequestParameters) {
		@SuppressWarnings("unchecked")
		Map<String, String> result = getRestOperations().postForObject(parameters.getAccessTokenUrl(),
				tokenRequestParameters, Map.class);
		return new AccessToken(result.get("access_token"), null, result.get("refresh_token"));
	}

	protected RestOperations getRestOperations() {
		return new RestTemplate();
	}
}
