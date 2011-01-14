/*
 * Copyright 2010 the original author or authors.
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
package org.springframework.social.connect;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

/**
 * General-purpose base class for ServiceProvider implementations.
 * @author Keith Donald
 * @param <S> The service API hosted by this service provider.
 */
public abstract class AbstractServiceProvider<S> implements ServiceProvider<S> {
	
	protected final ServiceProviderParameters parameters;

	protected final AccountConnectionRepository connectionRepository;
	
	/**
	 * Creates a ServiceProvider.
	 * @param parameters the parameters needed to implement the behavior in this class
	 * @param connectionRepository a data access interface for managing account connection records
	 */
	public AbstractServiceProvider(ServiceProviderParameters parameters,
			AccountConnectionRepository connectionRepository) {
		this.parameters = parameters;
		this.connectionRepository = connectionRepository;
	}

	// provider meta-data
	
	public String getName() {
		return parameters.getName();
	}
	
	public String getDisplayName() {
		return parameters.getDisplayName();
	}
	
	public String getApiKey() {
		return parameters.getApiKey();
	}

	public Long getAppId() {
		return parameters.getAppId();
	}
	
	// connection management
	public void addConnection(Serializable accountId, String accessToken, String providerAccountId) {
		OAuthToken oauthAccessToken = new OAuthToken(accessToken);
		S serviceOperations = createServiceOperations(oauthAccessToken);
		connectionRepository.addConnection(accountId, getName(), oauthAccessToken, providerAccountId,
				buildProviderProfileUrl(providerAccountId, serviceOperations));
	}

	public boolean isConnected(Serializable accountId) {
		return connectionRepository.isConnected(accountId, getName());
	}

	public boolean isConnected(Serializable accountId, String providerAcountId) {
		return connectionRepository.isConnected(accountId, getName(), providerAcountId);
	}

	public void refreshConnection(Serializable accountId, String providerAccountId) {
		throw new UnsupportedOperationException("Connection refresh is not supported for this provider.");
	}

	public void disconnect(Serializable accountId) {
		connectionRepository.disconnect(accountId, getName());
	}
	
	public void disconnect(Serializable accountId, String providerAccountId) {
		connectionRepository.disconnect(accountId, getName(), providerAccountId);
	}

	@Transactional
	public S getServiceOperations(Serializable accountId) {
		if (accountId == null || !isConnected(accountId)) {
			return createServiceOperations(null);
		}
		OAuthToken accessToken = connectionRepository.getAccessToken(accountId, getName());
		return createServiceOperations(accessToken);
	}

	public S getServiceOperations(OAuthToken accessToken) {
		return createServiceOperations(accessToken);
	}

	public S getServiceOperations(Serializable accountId, String providerAccountId) {
		OAuthToken accessToken = connectionRepository.getAccessToken(accountId, getName(), providerAccountId);
		return createServiceOperations(accessToken);
	}

	public Collection<AccountConnection> getConnections(Serializable accountId) {
		return connectionRepository.getAccountConnections(accountId, getName());
	}

	public String buildAuthorizeUrl(Map<String, String> authorizationParameters) {
		Map<String, String> authParametersCopy = new HashMap<String, String>(authorizationParameters);
		authParametersCopy.put("clientId", getApiKey());
		return parameters.getAuthorizeUrl().expand(authParametersCopy).toString();
	}

	// additional finders

	public String getProviderAccountId(Serializable accountId) {
		return connectionRepository.getProviderAccountId(accountId, getName());
	}

	// subclassing hooks
	/**
	 * Construct the strongly-typed service API template that callers may use to invoke the service offered by this service provider.
	 * Subclasses should override to return their concrete service implementation.
	 * @param accessToken the granted access token needed to make authorized requests for protected resources
	 */
	protected abstract S createServiceOperations(OAuthToken accessToken);

	/**
	 * Use the service API to fetch the id the member has been assigned in the provider's system.
	 * This id is stored locally to support linking to the user's connected profile page.
	 * It is also used for finding connected friends, see {@link #findMembersConnectedTo(List)}.
	 */
	protected abstract String fetchProviderAccountId(S serviceOperations);

	/**
	 * Build the URL pointing to the member's public profile on the provider's system.
	 * @param providerAccountId the id the member is known by in the provider's system.
	 * @param serviceOperations the service API
	 */
	protected abstract String buildProviderProfileUrl(String providerAccountId, S serviceOperations);

	/**
	 * The {@link #getApiKey() apiKey} secret.
	 */
	protected String getSecret() {
		return parameters.getSecret();
	}

	public void connect(Serializable accountId, OAuthToken accessToken) {
		S serviceOperations = createServiceOperations(accessToken);
		String providerAccountId = fetchProviderAccountId(serviceOperations);
		connectionRepository.addConnection(accountId, getName(), accessToken, providerAccountId,
				buildProviderProfileUrl(providerAccountId, serviceOperations));
	}
}