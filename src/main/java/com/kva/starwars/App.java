package com.kva.starwars;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 
 * @author kararora0
 *
 */

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet ds = new DispatcherServlet();
        ds.setThrowExceptionIfNoHandlerFound(Boolean.TRUE);
        return ds;
    }

    /*
     * @Bean public RestTemplate restTemplate(RestTemplateBuilder builder) { return
     * builder.setConnectTimeout(Duration.of(30, ChronoUnit.SECONDS)).build(); }
     */

    @Bean
    public RestTemplate restTemplate() {
        /*
         * CloseableHttpClient httpClient = HttpClients.custom() .setSSLHostnameVerifier(new
         * NoopHostnameVerifier()) .build(); HttpComponentsClientHttpRequestFactory requestFactory =
         * new HttpComponentsClientHttpRequestFactory(); requestFactory.setHttpClient(httpClient);
         * RestTemplate restTemplate = new RestTemplate(requestFactory); return restTemplate;
         */

        return new RestTemplate(getHttpClientRequestFactory());
    }

    private HttpComponentsClientHttpRequestFactory getHttpClientRequestFactory() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }
}
