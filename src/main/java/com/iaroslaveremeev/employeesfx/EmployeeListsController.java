package com.iaroslaveremeev.employeesfx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.employeesfx.model.Employee;
import com.iaroslaveremeev.employeesfx.repository.EmployeesRepo;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeListsController {
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
    public void initialize(File file) throws IOException {
        EmployeesRepo employeesRepo = new EmployeesRepo(file.getAbsolutePath());
        this.employeesRepoComboBox.setItems(FXCollections.observableList(employeesRepo.getEmployees()));
        this.listViewHashMap.put("developer", chosenDevsList);
        this.listViewHashMap.put("project_manager", chosenManagersList);
        this.listViewHashMap.put("designer", chosenDesignersList);
        this.listViewHashMap.put("tester", chosenTestersList);
    }

    @FXML
    public void buttonFileOpen(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\tirsb\\IdeaProjects\\EmployeesFX"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "JSON files", "*.json", "*.JSON"));
        File file = fileChooser.showOpenDialog(null);
        try  {
            if(file != null){
                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getResource("employeeLists.fxml"));
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setScene(new Scene(fxmlLoader.load(), 500, 750));
                EmployeeListsController employeeListsController = fxmlLoader.getController();
                employeeListsController.initialize(file);
                stage.show();
                Stage close = (Stage) this.employeesRepoComboBox.getScene().getWindow();
                close.close();
            }
            else throw new FileNotFoundException();
        } catch (FileNotFoundException e) {
            App.showAlertWithoutHeaderText("Error!", "You didn't chose any file", Alert.AlertType.ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonSaveFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\tirsb\\IdeaProjects\\EmployeesFX"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "JSON files", "*.json", "*.JSON"));
        File file = fileChooser.showSaveDialog(null);
        try {
            if (file != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String filename = file.getName();
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
                    ArrayList<Employee> chosenEmployees = new ArrayList<>();
                    chosenEmployees.addAll(this.employeesHashMap.get("developer"));
                    chosenEmployees.addAll(this.employeesHashMap.get("designer"));
                    chosenEmployees.addAll(this.employeesHashMap.get("tester"));
                    chosenEmployees.addAll(this.employeesHashMap.get("project_manager"));
                    objectMapper.writeValue(bufferedWriter, chosenEmployees);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else throw new FileNotFoundException();
        } catch (FileNotFoundException e) {
            App.showAlertWithoutHeaderText("Error!", "You didn't chose any file", Alert.AlertType.ERROR);
        }
    }

    public void onButtonEmployeeChosenClick(ActionEvent actionEvent) {
        try {
            Employee selectedEmployee = this.employeesRepoComboBox.getSelectionModel().getSelectedItem();
            this.employeesHashMap.getOrDefault(selectedEmployee.getJob(), new ArrayList<>());
            this.employeesHashMap.computeIfAbsent(selectedEmployee.getJob(), k -> new ArrayList<>())
                    .add(selectedEmployee);
            this.resRepo.addEmployee(selectedEmployee);
            this.employeesRepoComboBox.getItems().remove(selectedEmployee);
            this.listViewHashMap.get(selectedEmployee.getJob()).getItems().add(selectedEmployee);
        }
        catch (NullPointerException np){
            App.showAlertWithoutHeaderText("Error!", "You didn't chose any employee. Try again",
                    Alert.AlertType.ERROR);
        }
    }
}
