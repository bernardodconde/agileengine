package com.agile.engine.photo.utils;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestClientFactory {


	// ================
	// Public Methods
	// ================

	public <T> T getRestClient(String endpoint, Class<T> clazz) {

		T service = JAXRSClientFactory.create(endpoint, clazz, getProviders(), null, null);

		return buildRestClient(service);

	}

	public <T> T getSecureRestClient(String endpoint, Class<T> clazz, String token) throws Exception {

		String authorizationHeader = "Bearer " + token;

		T service = JAXRSClientFactory.create(endpoint, clazz, getProviders(), null, null);

		WebClient.client(service).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.header("Authorization", authorizationHeader);

		return buildRestClient(service);

	}

	// ================
	// private methods
	// ================

	private <T> T buildRestClient(T service) {

		LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();

		loggingInInterceptor.setPrettyLogging(true);
		loggingInInterceptor.setLogMultipart(true);

		WebClient.getConfig(service).getInInterceptors().add(loggingInInterceptor);

		LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();

		loggingOutInterceptor.setPrettyLogging(true);
		loggingOutInterceptor.setLogMultipart(true);

		WebClient.getConfig(service).getOutInterceptors().add(loggingOutInterceptor);


		return setTimedOut(service);

	}


	private ObjectMapper getObjectMapper() {

		ObjectMapper mapper = new ObjectMapper();

		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper;

	}

	private List<Object> getProviders() {

		List<Object> providers = new ArrayList<Object>();

		providers.add(new JacksonJsonProvider(getObjectMapper()));

		return providers;
	}

	private <T> T setTimedOut(T service) {

		HTTPConduit conduit = WebClient.getConfig(service).getHttpConduit();
		HTTPClientPolicy policy = new HTTPClientPolicy();
		policy.setReceiveTimeout(300000);
		conduit.setClient(policy);

		return service;

	}

}
