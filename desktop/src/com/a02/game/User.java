package com.a02.game;

public class User {
    private String username;
    private String password;
    private String name;
    private int age;
    private String mail;

    public User(String username, String password, String name, int age, String mail) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
