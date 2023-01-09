package com.iaroslaveremeev.employeesfx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.employeesfx.model.Employee;
import com.iaroslaveremeev.employeesfx.repository.EmployeesRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.*;

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
    void initialize(){
        this.listViewHashMap.put("developer", chosenDevsList);
        this.listViewHashMap.put("project_manager", chosenManagersList);
        this.listViewHashMap.put("designer", chosenDesignersList);
        this.listViewHashMap.put("tester", chosenTestersList);
    }

    public void initializeComboBox(File file) throws IOException {
        EmployeesRepo employeesRepo = new EmployeesRepo(file.getAbsolutePath());
        this.employeesRepoComboBox.setItems(FXCollections.observableList(employeesRepo.getEmployees()));
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
                initializeComboBox(file);
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
        /*try {*/
            Employee selectedEmployee = this.employeesRepoComboBox.getSelectionModel().getSelectedItem();
            /*if (selectedEmployee != null){
                App.showAlertWithoutHeaderText("Info!", "selectedEmployee is not null",
                        Alert.AlertType.INFORMATION);
            }*/
            this.employeesHashMap.getOrDefault(selectedEmployee.getJob(), new ArrayList<>());
            this.employeesHashMap.computeIfAbsent(selectedEmployee.getJob(), k -> new ArrayList<>())
                    .add(selectedEmployee);
            this.resRepo.addEmployee(selectedEmployee);
            this.employeesRepoComboBox.getItems().remove(selectedEmployee);
            this.listViewHashMap.get(selectedEmployee.getJob()).getItems().add(selectedEmployee);
        /*}
        catch (NullPointerException np){
            App.showAlertWithoutHeaderText("Error!", "You didn't chose any employee. Try again",
                    Alert.AlertType.ERROR);
        }*/
    }

    public void showAllDevelopers(ActionEvent actionEvent) {
        StringBuilder developersToShow = new StringBuilder();
        developersToShow.append("These are the developers that you chose for your team:\n\n");
        for (int i = 0; i < this.chosenDevsList.getItems().size(); i++) {
            developersToShow.append(i + 1).append(". ").append(this.chosenDevsList.getItems().get(i).toString());
        }
        App.showAlertWithoutHeaderText("Developers", developersToShow.toString(),
                Alert.AlertType.INFORMATION);
    }

    public void deleteAllDevelopers(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Deleting developers");
        alert.setHeaderText("Please confirm deleting all developers");
        alert.setContentText("The employees you deleted will return to the list");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            for (int i = 0; i < this.chosenDevsList.getItems().size(); i++) {
                Employee employee = this.chosenDevsList.getItems().get(i);
                this.resRepo.removeEmployee(employee);
                ObservableList<Employee> comboBoxEmployees = this.employeesRepoComboBox.getItems();
                comboBoxEmployees.add(employee);
                this.employeesRepoComboBox.setItems(comboBoxEmployees);
            }
            this.chosenDevsList.getItems().clear();
        }
    }
}
