package com.example.school_management_system;

import com.example.school_management_system.controllers.MainController;
import com.example.school_management_system.models.SchoolManager;
import com.example.school_management_system.persistence.PersistenceService;
import com.example.school_management_system.persistence.SchoolData;
import com.example.school_management_system.persistence.SchoolDataMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class SchoolManagementApp extends Application {
    private static final Path SAVE_FILE =
            Path.of(System.getProperty("user.home"), ".sms-data.json");

    private final PersistenceService persistenceService = new PersistenceService(SAVE_FILE);
    private MainController mainController;

    @Override
    public void start(Stage stage) throws IOException {
        SchoolManager schoolManager = loadOrCreate();
        FXMLLoader fxmlLoader = new FXMLLoader(SchoolManagementApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        mainController = fxmlLoader.getController();
        mainController.setSchoolManager(schoolManager);
        stage.setTitle("School Management System");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> saveOnExit());
        stage.show();
    }

    private SchoolManager loadOrCreate() {
        try {
            Optional<SchoolData> data = persistenceService.load();
            if (data.isPresent()) {
                return SchoolDataMapper.toDomain(data.get());
            }
        } catch (IOException e) {
            showError("Failed to load saved data",
                    "Could not read the save file at " + SAVE_FILE + ".\n"
                            + "Starting with an empty state.\n\n" + e.getMessage());
        }
        return new SchoolManager("My School");
    }

    /**
     * Serialize the current SchoolManager to JSON and write to disk.
     * Called by the stage's close handler.
     */
    private void saveOnExit() {
        try {
            SchoolData data = SchoolDataMapper.toData(mainController.getSchoolManager());
            persistenceService.save(data);
        } catch (IOException e) {
            showError("Failed to save data",
                    "Could not save your changes to " + SAVE_FILE + ".\n\n" + e.getMessage());
        }
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("School Management System");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}