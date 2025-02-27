package ru.kursach.frontent.http.api;

import ru.kursach.frontent.dto.AuthRequest;
import ru.kursach.frontent.http.Client;

import java.io.IOException;

public class AuthorizationsClient extends Client {
    private final String apiUrl = baseUrl + "/auth/login";

    public String getUser(AuthRequest authRequest) throws IOException {
        return post(apiUrl, authRequest);
    }
}
