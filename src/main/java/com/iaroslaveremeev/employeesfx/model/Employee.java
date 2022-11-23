package com.iaroslaveremeev.employeesfx.model;

import java.util.Objects;

public class Employee {
    private String name;
    private String surname;
    private int experience;
    private String job;

    public Employee() {
    }

    public Employee(String name, String surname, int experience, String job) {
        this.name = name;
        this.surname = surname;
        this.experience = experience;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return experience == employee.experience && Objects.equals(name, employee.name) && Objects.equals(surname, employee.surname) && Objects.equals(job, employee.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, experience, job);
    }

    public String showNameSurname() {
        return name + " " + surname;
    }

    @Override
    public String toString() {
        return name + " " + surname + ", " + job + ", " + experience + " years of experience\n";
    }
}

