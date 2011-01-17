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
package org.springframework.social.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.social.gowalla.provider.GowallaServiceProvider;
import org.w3c.dom.Element;

public class GowallaProviderElementParser extends AbstractServiceProviderElementParser {

	private static final String ACCESS_TOKEN_URL = "https://gowalla.com/api/oauth/token";
	private static final String AUTHORIZATION_URL = "https://gowalla.com/api/oauth/new?client_id={clientId}&redirect_uri={redirectUri}&scope={scope}";
	private static final String REQUEST_TOKEN_URL = null;

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String name = element.getAttribute("id");
		if (name == null || name.isEmpty()) {
			name = "gowalla";
		}

		return registerServiceProviderBean(parserContext, name, GowallaServiceProvider.class.getName(), "Gowalla",
				element.getAttribute("consumer-key"), element.getAttribute("consumer-secret"), null,
				REQUEST_TOKEN_URL, AUTHORIZATION_URL, ACCESS_TOKEN_URL, element.getAttribute("connection-repository"));
	}
}
