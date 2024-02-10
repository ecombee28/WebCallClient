package com.example.eric.combee.moviepicker.utility;

import com.example.eric.combee.moviepicker.model.response.MovieDetailModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingUtility {

    @Autowired
    private ObjectMapper objectMapper;

    public void logInfo(MovieDetailModel body, String description) throws JsonProcessingException {

        if (body != null) {
            String payload = objectMapper.writeValueAsString(body);
            log.info(description + "," + payload);
        } else {
            log.info(description);
        }
    }


    public void logError(MovieDetailModel body, String description, Exception ex) throws JsonProcessingException {

        if (body != null) {
            String payload = objectMapper.writeValueAsString(body);
            log.error(payload + "," + description + "," + ex.getMessage());
        } else {
            log.error(description + "," + ex.getMessage());
        }
    }

    public void logWebClientError(String description, Throwable ex) {
        log.error(description + "," + ex.getMessage());
    }

}
