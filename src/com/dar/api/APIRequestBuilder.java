package com.dar.api;

public class APIRequestBuilder<E extends IAPI> {
	private StringBuilder request;
	boolean noParam = true;
	
	public APIRequestBuilder<E> addDomain(E domain) {
		noParam = true;
		request = new StringBuilder();
		request.append(domain);
		return this;
	}
	
	public APIRequestBuilder<E> addStr(String str) {
		if(noParam) request.append(str);
		return this;
	}
	
	public APIRequestBuilder<E> addParam(E param,String value) {
		if(noParam) request.append('?'); else request.append('&');
		request.append(param).append(value);
		return this;
	}
	
	public String get() {
		return request.toString();
	}
	

}
