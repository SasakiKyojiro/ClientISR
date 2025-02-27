package ru.kursach.frontent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kursach.frontent.dto.enams.UserRole;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthObject {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("role")
    private UserRole role;

}
