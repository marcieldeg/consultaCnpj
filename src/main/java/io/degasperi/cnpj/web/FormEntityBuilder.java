package io.degasperi.cnpj.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class FormEntityBuilder {
	private final List<NameValuePair> params = new ArrayList<>();

	private FormEntityBuilder() {
	}

	public static FormEntityBuilder create() {
		return new FormEntityBuilder();
	}

	public FormEntityBuilder add(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
		return this;
	}

	public HttpEntity build() throws UnsupportedEncodingException {
		return new UrlEncodedFormEntity(params);
	}
}
