package ru.kursach.frontent.dto.enams;

public enum UserRole {
    Admin("Администратор"),
    Worker("Работник"),
    User("Пользователь");


    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
