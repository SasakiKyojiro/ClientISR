package ru.kursach.frontent.http.api;

import ru.kursach.frontent.dto.User;
import ru.kursach.frontent.http.Client;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AdminClient extends Client {
    private final String apiUrl = baseUrl + "/admin/users";

    public String getAllUser() throws IOException {
        return get(apiUrl + "?limit="+limit+"&offset="+offset);
    }

    public String getUser(String name) throws IOException {
        String encodedUrl = URLEncoder.encode(name, StandardCharsets.UTF_8);
        return get(apiUrl + "?name=" + encodedUrl);
    }

    public String addUser(User user) throws IOException {
        return post(apiUrl, user);
    }

    public String updateUser(User updateUser) throws IOException {
        return put(apiUrl + "/" + updateUser.getId(), updateUser);
    }

    public String deleteUser(User selectedPerson) throws IOException {
        return delete(apiUrl + "/" + selectedPerson.getId());
    }

    public String passwordReset(User selectedUser) throws IOException {
        return put(apiUrl + "/" + selectedUser.getId() + "/reset-password");
    }
}
