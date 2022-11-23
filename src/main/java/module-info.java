module com.iaroslaveremeev.employeesfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.iaroslaveremeev.employeesfx to javafx.fxml;
    exports com.iaroslaveremeev.employeesfx;
    exports com.iaroslaveremeev.employeesfx.model;
    opens com.iaroslaveremeev.employeesfx.model to javafx.fxml;
    exports com.iaroslaveremeev.employeesfx.repository;
    opens com.iaroslaveremeev.employeesfx.repository to javafx.fxml;
}