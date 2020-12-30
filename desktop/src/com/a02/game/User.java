package com.a02.game;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 4050798975698164181L;
    private String username;
    private String password; //TODO: seguridad/encriptar
    private String name;
    private int age;
    private String mail;
    private boolean admin;

    public User(String username, String password, String name, int age, String mail) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.mail = mail;
        this.admin = false;
    }

    public User() {

    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", mail='" + mail + '\'' +
                ", admin=" + admin +
                '}';
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
