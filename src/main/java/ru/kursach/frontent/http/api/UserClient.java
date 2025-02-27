package ru.kursach.frontent.http.api;

import ru.kursach.frontent.dto.Request;
import ru.kursach.frontent.http.Client;

import java.io.IOException;

public class UserClient extends Client {
    private String apiUrl;
    public UserClient() {
        addPropertyChangeListener(evt -> {
            if ("uuid".equals(evt.getPropertyName())) {
                apiUrl = baseUrl + "/user/" + uuid + "/";
            }
        });
    }

    public String putRequest(Request request) throws IOException {
        return post(apiUrl + "requests", request);
    }

    public String getRequest() throws IOException {
        return get(apiUrl + "requests");
    }

    public String getTax() throws IOException {
        return get(apiUrl + "tax-assessments");
    }
}
