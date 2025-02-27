package ru.kursach.frontent.dto.enams;

public enum TaxStatus {
    consideration("В обработке"),
    satisfied("Оплачен"),
    rejected("Оплата не пройдена");

    private final String status;

    TaxStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return status;
    }
}
