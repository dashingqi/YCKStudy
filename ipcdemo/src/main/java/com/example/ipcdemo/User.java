package com.example.ipcdemo;

import java.io.Serializable;

public class User implements Serializable {

    public static  final long serialVersionUID = 1L;

    private String name;
    private int age;
    private String sex;
    private String heihei;
    private String haha;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
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
}
