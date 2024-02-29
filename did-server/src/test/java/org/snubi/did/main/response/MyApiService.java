package org.snubi.did.main.response;

import org.springframework.web.client.RestTemplate;

public class MyApiService {
    private RestTemplate restTemplate;

    public MyApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchDataFromApi(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
