package ru.kursach.frontent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kursach.frontent.dto.enams.TaxStatus;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tax {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("organization_name")
    private String organizationName;
    @JsonProperty("summ")
    private String sum;
    @JsonProperty("tax_type")
    private String taxType;
    @JsonProperty("paying_deadline")
    private LocalDate payingDeadline;
    @JsonProperty("status")
    private TaxStatus status;

    public Tax(String userName, String organizationName, String sum, String taxType, LocalDate data, TaxStatus status) {
        this.userName = userName;
        this.organizationName = organizationName;
        this.sum = sum;
        this.taxType = taxType;
        this.payingDeadline = data;
        this.status = status;
    }

    public void setTax(Tax tax) {
        this.userName = tax.getUserName();
        this.organizationName = tax.getOrganizationName();
        this.sum = tax.getSum();
        this.taxType = tax.getTaxType();
        this.payingDeadline = tax.getPayingDeadline();
        this.status = tax.getStatus();
    }
}
