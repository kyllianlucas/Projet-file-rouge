package com.doranco.site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CaptchaService {

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String SECRET_KEY = "6Le34CUqAAAAAHSEskWGVh4upiQ_TkbObqzt-lKf"; // Remplacez par votre clé secrète reCAPTCHA

    @Autowired
    private final RestTemplate restTemplate;

    public boolean verifyCaptcha(String captchaToken) {
        String url = String.format("%s?secret=%s&response=%s", RECAPTCHA_VERIFY_URL, SECRET_KEY, captchaToken);
        CaptchaResponse response = restTemplate.postForObject(url, null, CaptchaResponse.class);
        return response.isSuccess();
    }

    private static class CaptchaResponse {
        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
