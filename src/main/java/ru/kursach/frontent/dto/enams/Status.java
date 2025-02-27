package ru.kursach.frontent.dto.enams;

public enum Status {
    consideration("В обработке"),
    satisfied("Удовлетворенно"),
    rejected("Отменен");

    private final String status;

    Status(String status) {
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
