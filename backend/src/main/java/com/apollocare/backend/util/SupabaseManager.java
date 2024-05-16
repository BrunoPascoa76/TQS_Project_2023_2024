package com.apollocare.backend.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;


import com.apollocare.backend.models.Consultation;


import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class SupabaseManager {

    private final WebClient web;
    private final ObjectMapper mapper = new ObjectMapper();

    public SupabaseManager(@Value("${supabase.apiKey}") String apiKey, @Value("${supabase.baseURL}") String baseURL) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("apikey", apiKey);
        web = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
                .build();
    }

    public ResponseEntity<String> getRequest(String uri) {
        return web.get().uri(uri).retrieve().toEntity(String.class).block();
    }

    public ResponseEntity<String> postRequest(String uri, String body) {
        return web.post().uri(uri).bodyValue(body).retrieve().toEntity(String.class).block();
    }

    public List<Consultation> parseConsultationList(String responseBody) {
        if (StringUtils.hasText(responseBody)) {
            try {
                return mapper.readValue(responseBody, new TypeReference<List<Consultation>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }
}
