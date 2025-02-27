package ru.kursach.frontent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Organization {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("organization_name")
    private String name;
    @JsonProperty("inn")
    private String inn;
    @JsonProperty("kpp")
    private String kpp;
    @JsonProperty("address")
    private String address;

    public void setOrganization(Organization organization) {
        this.id = organization.id;
        this.name = organization.name;
        this.inn = organization.inn;
        this.kpp = organization.kpp;
        this.address = organization.address;
    }
}
