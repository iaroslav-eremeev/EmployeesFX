package com.iaroslaveremeev.employeesfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainController {
    @FXML
    public Label MainLabel;

    public void initialize() throws IOException {
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
                Stage close = (Stage) this.MainLabel.getScene().getWindow();
                close.close();
            }
            else throw new FileNotFoundException();
        } catch (FileNotFoundException e) {
            App.showAlertWithoutHeaderText("Error!", "You didn't chose any file", Alert.AlertType.ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
