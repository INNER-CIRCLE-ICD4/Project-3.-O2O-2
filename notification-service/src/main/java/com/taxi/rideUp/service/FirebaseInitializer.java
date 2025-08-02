package com.taxi.rideUp.service;

/**
 * packageName : com.taxi.rideUp.service
 * fileName    : FirebaseInitializer
 * author      : hsj
 * date        : 2025. 8. 3.
 * description :
 */
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Component
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() {
        try (InputStream serviceAccount = getClass().getClassLoader()
            .getResourceAsStream("firebase-service-account.json")) {

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            throw new RuntimeException("Firebase 초기화 실패", e);
        }
    }
}

