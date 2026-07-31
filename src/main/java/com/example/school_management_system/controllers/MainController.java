package com.example.school_management_system.controllers;
import com.example.school_management_system.models.Course;
import com.example.school_management_system.models.SchoolManager;
import com.example.school_management_system.models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class MainController {
    public SchoolManager schoolManager = new SchoolManager("My School");
    private Node dashboardView;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TextField NameInput;
    @FXML
    private TextField idInput;
    @FXML
    private TextField emailInput;
    @FXML
    private Label statusLabel;
    @FXML
    private BorderPane mainPane;
    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> courseID;
    @FXML
    private TableColumn<Course, String> title;
    @FXML
    private TableColumn<Course, Integer> numberOfCredits;
    @FXML
    private TextField courseIdInput;
    @FXML
    private TextField courseTitleInput;
    @FXML
    private TextField courseCreditsInput;

    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private ObservableList<Course> courseData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        studentData.setAll(schoolManager.getAllStudents());
        courseData.setAll(schoolManager.getAllCourses());
        studentTable.setItems(studentData);
        dashboardView = mainPane.getCenter();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    @FXML
    private void handleShowDashboard() {
        mainPane.setCenter(dashboardView);
        statusLabel.setText("Navigation: Student Dashboard");
        statusLabel.setStyle("-fx-text-fill: #27ae60;");
    }


    @FXML
    protected void handleAddStudent() {
        String name = NameInput.getText().trim();
        String email = emailInput.getText().trim();
        String id = idInput.getText().trim();

        if (name.isEmpty() || id.isEmpty() || email.isEmpty()) {
            statusLabel.setText("Error! All fields must be filled.");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        Student newStudent = new Student(name, email, id);
        if (schoolManager.addStudent(newStudent)) {
            studentData.add(newStudent);
            statusLabel.setText("Student: " + name + " added successfully!");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
            NameInput.clear();
            emailInput.clear();
            idInput.clear();
        } else {
            statusLabel.setText("Error: A student with ID " + id + " already exists.");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    @FXML
    private void setupCourseTable() {
        courseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        numberOfCredits.setCellValueFactory(new PropertyValueFactory<>("numberOfCredits"));
        courseTable.setItems(courseData);
    }


    @FXML
    protected void handleAddCourse() {
        String id = courseIdInput.getText().trim();
        String title = courseTitleInput.getText().trim();
        String creditsStr = courseCreditsInput.getText().trim();

        if (id.isEmpty() || title.isEmpty() || creditsStr.isEmpty()) {
            statusLabel.setText("Error! All fields must be filled.");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            Course newCourse = new Course(id, title, credits);
            if (schoolManager.addCourse(newCourse)) {
                courseData.add(newCourse);
                statusLabel.setText("Course '" + title + "' added successfully!");
                statusLabel.setStyle("-fx-text-fill: #27ae60;");
                courseIdInput.clear();
                courseTitleInput.clear();
                courseCreditsInput.clear();
            } else {
                statusLabel.setText("Error: A course with ID " + id + " already exists.");
                statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Error: Credits must be a valid number!");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    @FXML
    private void handleDeleteStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            schoolManager.removeStudent(selected.getId());
            studentData.remove(selected);
            statusLabel.setText("Student " + selected.getName() + " is successfully deleted!");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
        } else {
            statusLabel.setText("Error: No student selected to delete!");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    @FXML
    private void handleShowCourses() {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/school_management_system/courses-view.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setController(this);
            VBox courseView = loader.load();
            setupCourseTable();
            mainPane.setCenter(courseView);
            statusLabel.setText("Navigation: Course Management Loaded");
        } catch (IOException e) {
            statusLabel.setText("Error: Could not load Courses view!");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("You are about to delete " + selectedCourse.getTitle());
            alert.setContentText("Are you sure you want to delete this student? This action cannot be undone.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                courseData.remove(selectedCourse); // Update UI
                schoolManager.removeCourse(selectedCourse.getCourseID()); // Update Manager logic
                statusLabel.setText("Student deleted successfully.");
                statusLabel.setStyle("-fx-text-fill: #27ae60;");
            } else {
                statusLabel.setText("Deletion cancelled.");
                statusLabel.setStyle("-fx-text-fill: #34495e;");
            }
        } else {
            statusLabel.setText("Error: Please select a student first!");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    @FXML
    private void cleanStudentInput() {
        NameInput.clear();
        idInput.clear();
        emailInput.clear();
    }

}