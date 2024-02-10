package com.example.eric.combee.moviepicker.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${tmdb.writeTimeout}")
    private int writeTimeout;
    @Value("${tmdb.connectionTimeout}")
    private int connectTimeout;
    @Value("${tmbd.baseURL}")
    private String baseUrl;


    @Bean(name = "webClientBase")
    public WebClient buildWebClient() throws SSLException {

        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        ConnectionProvider csProvider = ConnectionProvider.builder("CS Provider")
                .maxIdleTime(Duration.ofMillis(1000))
                .pendingAcquireMaxCount(3)
                .build();


        // Create reactor netty HTTP client
        HttpClient httpClient = HttpClient.create(csProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(
                                30000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(
                                writeTimeout, TimeUnit.MILLISECONDS)))
                .secure(sslSpec -> sslSpec.sslContext(sslContext));


        // Configure and build web client
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

    }

}
