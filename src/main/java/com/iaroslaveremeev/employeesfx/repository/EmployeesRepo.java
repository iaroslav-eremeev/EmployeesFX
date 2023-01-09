package com.iaroslaveremeev.employeesfx.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.employeesfx.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EmployeesRepo {
    private ArrayList<Employee> employees = new ArrayList<>();

    public EmployeesRepo() {
    }

    public EmployeesRepo(String link) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(link))) {
            this.employees = objectMapper.readValue(bufferedReader, new TypeReference<>() {});
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee){
        this.employees.add(employee);
    }

    public Employee removeEmployee(Employee employee) {
        this.employees.remove(employee);
        return employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeesRepo that = (EmployeesRepo) o;
        return Objects.equals(employees, that.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employees);
    }

    @Override
    public String toString() {
        return "EmployeesRepo{" +
                "employees=" + employees +
                '}';
    }
}
