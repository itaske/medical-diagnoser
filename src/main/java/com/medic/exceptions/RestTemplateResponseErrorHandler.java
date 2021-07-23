package com.medic.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InterruptedIOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonParser = null;
        if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND)
            jsonParser = objectMapper.readTree(httpResponse.getBody());

        if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.SERVER_ERROR) {
            throw new InterruptedIOException(httpResponse.toString());
            // handle SERVER_ERROR
        } else if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.CLIENT_ERROR) {
            // handle CLIENT_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException(jsonParser.get("message").get(0).asText());
            } else if (httpResponse.getStatusCode() == HttpStatus.UNAUTHORIZED){
                throw new ForbiddenRequestException("User is not Authorized to make this request, please contact management for more info");
            } else{
                throw new BadRequestException("Bad Request");
            }
        } else{
            throw new InterruptedRequestException("Request was Interrupted");
        }
    }
}
