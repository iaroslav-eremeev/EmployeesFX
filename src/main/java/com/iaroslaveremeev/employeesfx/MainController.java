package com.iaroslaveremeev.employeesfx;

import com.iaroslaveremeev.employeesfx.repository.EmployeesRepo;
import com.iaroslaveremeev.employeesfx.model.Employee;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
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
    public File buttonFileOpen(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\tirsb\\IdeaProjects\\EmployeesFX"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "JSON files", "*.json", "*.JSON"));
        File file = fileChooser.showOpenDialog(null);
        try  {
            if(file != null){
                return file;
            }
            else throw new FileNotFoundException();
        } catch (FileNotFoundException e) {
            App.showAlertWithoutHeaderText("Error!", "You didn't chose any file", Alert.AlertType.ERROR);
        }
        return null;
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
