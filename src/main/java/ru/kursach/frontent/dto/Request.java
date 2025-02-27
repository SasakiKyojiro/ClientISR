package ru.kursach.frontent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kursach.frontent.dto.enams.Status;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("theme")
    private String theme;
    @JsonProperty("body")
    private String body;
    @JsonProperty("date")
    private String date;
    @JsonProperty("status")
    private Status status;

    public Request(String theme, String body){
        this.theme = theme;
        this.body = body;
    }

    public void SetRequest(Request request){
        this.id = request.getId();
        this.theme = request.getTheme();
        this.body = request.getBody();
        this.date = request.getDate();
        this.status = request.getStatus();
    }
}
