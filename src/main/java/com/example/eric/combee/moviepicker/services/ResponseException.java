package com.example.eric.combee.moviepicker.services;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class ResponseException {

    /**
     * Create exception response
     **/
    public Mono<WebClientResponseException> createResponseException(ClientResponse response) {
        return response.body(BodyExtractors.toDataBuffers())
                .reduce(DataBuffer::write)
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .defaultIfEmpty(new byte[0])
                .map(bodyBytes -> {
                    Charset charset = response.headers().contentType()
                            .map(MimeType::getCharset)
                            .orElse(StandardCharsets.ISO_8859_1);

                    return new WebClientResponseException(
                            String.format(
                                    "Client response has erroneous status code: %d",
                                    response.statusCode().value()),
                            response.statusCode().value(),
                            response.statusCode().toString(),
                            response.headers().asHttpHeaders(),
                            bodyBytes,
                            charset);
                });
    }
}
