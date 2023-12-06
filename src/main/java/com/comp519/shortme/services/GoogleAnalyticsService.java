//package com.comp519.shortme.services;
//
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.entity.StringEntity;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//public class GoogleAnalyticsService {
//
//    @Value("${google.analytics.measurement-id}")
//    private String measurementId;
//
//    @Value("${google.analytics.api-secret}")
//    private String apiSecret;
//
//    private final HttpClient httpClient;
//
//    public GoogleAnalyticsService() {
//        this.httpClient = HttpClients.createDefault();
//    }
//
//    public void trackEvent() {
//        try {
//            final String clientId = UUID.randomUUID().toString();
//            final String analyticsEndpoint = "https://www.google-analytics.com/mp/collect";
//            final String eventName = "redirection";
//
//            HttpPost request = new HttpPost(analyticsEndpoint + "?measurement_id=" + measurementId + "&api_secret=" + apiSecret);
//
//            String jsonPayload = "{"
//                    + "\"client_id\": \"" + clientId + "\","
//                    + "\"events\": [{\"name\": \"" + eventName + "\", \"params\": {}}]"
//                    + "}";
//
//            StringEntity entity = new StringEntity(jsonPayload);
//            request.setEntity(entity);
//            request.setHeader("Content-Type", "application/json");
//
//            httpClient.execute(request);
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle exception
//        }
//    }
//}
