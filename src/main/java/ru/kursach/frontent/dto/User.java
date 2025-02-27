package ru.kursach.frontent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.kursach.frontent.dto.enams.UserRole;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("login")
    private String login;
    @JsonProperty("email")
    private String email;
    @JsonProperty("tel")
    private String phone;
    @JsonProperty("authority")
    private UserRole authority;

    public User(String name, String login, String email, String phone, UserRole authority) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.phone = phone;
        this.authority = authority;
    }

    public void SetUser(@NonNull User user) {
        this.id = user.id;
        this.name = user.name;
        this.login = user.login;
        this.email = user.email;
        this.phone = user.phone;
        this.authority = user.authority;
    }
}
