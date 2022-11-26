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
    public Button buttonEmployeeChosen;
    public ListView<Employee> chosenDevsList;
    public ListView<Employee> chosenTestersList;
    public ListView<Employee> chosenDesignersList;
    public ListView<Employee> chosenManagersList;

    public EmployeesRepo resRepo = new EmployeesRepo();
    private HashMap<String, ArrayList<Employee>> employeesHashMap = new HashMap<>();

    private HashMap<String, ListView<Employee>> listViewHashMap = new HashMap<>();

    @FXML
    public void initialize() throws IOException {
        EmployeesRepo employeesRepo = new EmployeesRepo("employees.json");
        this.employeesRepoComboBox.setItems(FXCollections.observableList(employeesRepo.getEmployees()));
        this.listViewHashMap.put("developer", chosenDevsList);
        this.listViewHashMap.put("project_manager", chosenManagersList);
        this.listViewHashMap.put("designer", chosenDesignersList);
        this.listViewHashMap.put("tester", chosenTestersList);
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
        this.employeesRepoComboBox.getItems().remove(selectedEmployee);
        this.listViewHashMap.get(selectedEmployee.getJob()).getItems().add(selectedEmployee);
    }

    public ArrayList<Employee> getEmployeesByJob(HashMap<String, ArrayList<Employee>> map, String job){
        return map.get(job);
    }
}
