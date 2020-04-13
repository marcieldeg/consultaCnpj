package io.degasperi.cnpj.web;

import java.net.ProxySelector;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.CookieStore;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicLineParser;
import org.apache.http.ssl.SSLContexts;

public class CloseableHttpClientFactory {
	public static CloseableHttpClient build()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		final CookieStore cookieStore = new BasicCookieStore();

		final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (cert, authType) -> true).build();
		final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
				NoopHostnameVerifier.INSTANCE);

		final ManagedHttpClientConnectionFactory connFactory = new ManagedHttpClientConnectionFactory(
				new DefaultHttpResponseParserFactory(new BasicLineParser(), //
						new DefaultHttpResponseFactory()));

		final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory>create()//
				.register("https", sslsf)//
				.register("http", PlainConnectionSocketFactory.INSTANCE)//
				.build();

		final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry, connFactory);

		return HttpClients.custom()//
				.setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))//
				.setRedirectStrategy(LaxRedirectStrategy.INSTANCE)
				.setConnectionManager(connectionManager)//
				.setDefaultCookieStore(cookieStore)//
				.build();
	}
}
