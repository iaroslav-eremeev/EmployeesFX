package com.iaroslaveremeev.employeesfx;

import com.iaroslaveremeev.employeesfx.repository.EmployeesRepo;
import com.iaroslaveremeev.employeesfx.model.Employee;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainController {
    @FXML
    public ComboBox<Employee> employeesRepoComboBox;

    public ArrayList<Employee> employeesInComboBox;
    public Button buttonEmployeeChosen;
    public ListView chosenDevsList;
    public ListView chosenTestersList;
    public ListView chosenDesignersList;
    public ListView chosenManagersList;

    public EmployeesRepo resRepo = new EmployeesRepo();
    public HashMap<String, ArrayList<Employee>> employeesHashMap = new HashMap<>();

    @FXML
    public void initialize() throws IOException {
        EmployeesRepo employeesRepo = new EmployeesRepo("employees.json");
        this.employeesInComboBox = employeesRepo.getEmployees();
        this.employeesRepoComboBox.setItems(FXCollections.observableList(this.employeesInComboBox));
    }

    @FXML
    public void buttonFileOpen(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "TXT files", "*.txt", "*.TXT"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "CSV files", "*.csv", "*.CSV"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            System.out.println(file);
        }
    }

    public void onButtonEmployeeChosenClick(ActionEvent actionEvent) {
        Employee selectedEmployee = this.employeesRepoComboBox.getSelectionModel().getSelectedItem();
        this.employeesHashMap.getOrDefault(selectedEmployee.getJob(), new ArrayList<>());
        this.employeesHashMap.computeIfAbsent(selectedEmployee.getJob(), k -> new ArrayList<>())
                .add(selectedEmployee);
        this.resRepo.addEmployee(selectedEmployee);
        this.employeesInComboBox.remove(selectedEmployee);
        this.employeesRepoComboBox.setItems(FXCollections.observableList(this.employeesInComboBox));
        if (this.employeesHashMap.get("developer") != null){
            this.chosenDevsList.getItems().clear();
            this.chosenDevsList.getItems().add(getEmployeesByJob(this.employeesHashMap, "developer"));
        }
        if (this.employeesHashMap.get("project_manager") != null){
            this.chosenManagersList.getItems().clear();
            this.chosenManagersList.getItems().add(getEmployeesByJob(this.employeesHashMap, "project_manager"));
        }
        if (this.employeesHashMap.get("designer") != null){
            this.chosenDesignersList.getItems().clear();
            this.chosenDesignersList.getItems().add(getEmployeesByJob(this.employeesHashMap, "designer"));
        }
        if (this.employeesHashMap.get("tester") != null){
            this.chosenTestersList.getItems().clear();
            this.chosenTestersList.getItems().add(getEmployeesByJob(this.employeesHashMap, "tester"));
        }
    }

    public String getEmployeesByJob(HashMap<String, ArrayList<Employee>> map, String job){
        ArrayList<Employee> employees = map.get(job);
        return employees.toString().replaceAll("\\[|\\]", "")
                .replaceAll("experience\n, ", "experience\n");
    }
}
