package io.degasperi.cnpj.classes;

import java.util.HashMap;
import java.util.Map;

public class SearchResult {
	private Status status;
	private String message;
	private Map<String, Cnpj> result;

	private SearchResult() {
	}

	public static SearchResult success(Cnpj cnpj) {
		final SearchResult searchResult = new SearchResult();
		searchResult.status = Status.SUCCESS;
		searchResult.message = "OK";
		searchResult.result = new HashMap<>();
		searchResult.result.put(cnpj.getInscricao().replaceAll("[^0-9]", ""), cnpj);
		return searchResult;
	}

	public static SearchResult success(Map<String, Cnpj> cnpjs) {
		final SearchResult searchResult = new SearchResult();
		searchResult.status = Status.SUCCESS;
		searchResult.message = "OK";
		searchResult.result = new HashMap<>();
		searchResult.result.putAll(cnpjs);
		return searchResult;
	}

	public static SearchResult error(String message) {
		final SearchResult searchResult = new SearchResult();
		searchResult.status = Status.ERROR;
		searchResult.message = message;
		searchResult.result = null;
		return searchResult;
	}

	public Status getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public Map<String, Cnpj> getResult() {
		return result;
	}

	public static enum Status {
		SUCCESS, ERROR
	}
}
