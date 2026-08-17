module com.example.school_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.example.school_management_system.models to javafx.base;
    opens com.example.school_management_system to javafx.fxml;
    opens com.example.school_management_system.controllers to javafx.fxml;
    opens com.example.school_management_system.persistence to com.fasterxml.jackson.databind;

    exports com.example.school_management_system;
    exports com.example.school_management_system.controllers;
}