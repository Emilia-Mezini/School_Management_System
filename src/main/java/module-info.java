module com.example.school_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    opens com to javafx.base;
    opens com.example.school_management_system to javafx.fxml;
    exports com.example.school_management_system;
    exports com.example.school_management_system.controllers;
    opens com.example.school_management_system.controllers to javafx.fxml;
}