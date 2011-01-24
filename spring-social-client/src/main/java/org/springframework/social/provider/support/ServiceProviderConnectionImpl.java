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

import org.springframework.social.provider.ServiceProviderConnection;

/**
 * Generic ServiceProviderConnection implementation used by {@link AbstractServiceProvider}.
 * @author Keith Donald
 * @param <S> the service API
 */
public class ServiceProviderConnectionImpl<S> implements ServiceProviderConnection<S> {

	private final Long id;
	
	private final S api;

	private final Serializable accountId;
	
	private final String providerId;

	private final ConnectionRepository connectionRepository;
	
	public ServiceProviderConnectionImpl(Long id, S api, Serializable accountId, String providerId, ConnectionRepository connectionRepository) {
		this.id = id;
		this.api = api;
		this.connectionRepository = connectionRepository;
		this.accountId = accountId;
		this.providerId = providerId;
	}
	
	public Long getId() {
		return id;
	}

	public S getApi() {
		return api;
	}

	public void disconnect() {
		connectionRepository.removeConnection(accountId, providerId, id);
	}

}
