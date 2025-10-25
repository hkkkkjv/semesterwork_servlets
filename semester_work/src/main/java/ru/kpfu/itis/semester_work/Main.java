package ru.kpfu.itis.semester_work;

import ru.kpfu.itis.semester_work.util.PasswordHasher;

public class Main {
    public static void main(String[] args) {
        PasswordHasher passwordHasher = new PasswordHasher();
        System.out.println(passwordHasher.hashPassword("prepodavatel1"));
    }
}
