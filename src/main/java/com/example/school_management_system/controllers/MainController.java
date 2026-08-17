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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;

public class MainController {
    private SchoolManager schoolManager;
    private Node dashboardView;
    @FXML
    private Label countsLabel;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TextField nameInput;
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
    @FXML private ComboBox<Student> studentComboBox;
    @FXML private ComboBox<Course> courseComboBox;
    @FXML private TableView<EnrollmentRow> enrollmentTable;
    @FXML private TableColumn<EnrollmentRow, String> enrollmentStudentNameColumn;
    @FXML private TableColumn<EnrollmentRow, String> enrollmentStudentIdColumn;
    @FXML private TableColumn<EnrollmentRow, String> enrollmentCourseTitleColumn;
    @FXML private TableColumn<EnrollmentRow, String> enrollmentCourseIdColumn;
    @FXML private TableColumn<EnrollmentRow, Number> enrollmentCreditsColumn;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalCoursesLabel;
    @FXML private Label enrolledStudentsLabel;
    @FXML private Label notEnrolledLabel;
    @FXML private TableView<DashboardCourseRow> breakdownTable;
    @FXML private TableColumn<DashboardCourseRow, String> breakdownCourseColumn;
    @FXML private TableColumn<DashboardCourseRow, String> breakdownIdColumn;
    @FXML private TableColumn<DashboardCourseRow, Integer> breakdownCountColumn;
    @FXML private ListView<Student> notEnrolledList;

    private final ObservableList<EnrollmentRow> enrollmentData = FXCollections.observableArrayList();
    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private ObservableList<Course> courseData = FXCollections.observableArrayList();
    private javafx.scene.Node studentsView;
    private boolean initialized = false;

    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        studentTable.setItems(studentData);
        studentTable.setPlaceholder(new Label("No students yet. Add one using the form below."));
        dashboardView = mainPane.getCenter();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        countsLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> pluralize(studentData.size(), "student", "students")
                                + " · "
                                + pluralize(courseData.size(), "course", "courses"),
                        studentData, courseData
                )
        );
        this.studentsView = mainPane.getCenter();
    }

    public void setSchoolManager(SchoolManager schoolManager) {
        this.schoolManager = schoolManager;
        studentData.setAll(schoolManager.getAllStudents());
        courseData.setAll(schoolManager.getAllCourses());
    }

    public SchoolManager getSchoolManager() {
        return schoolManager;
    }

    @FXML
    private void handleShowDashboard() {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/school_management_system/dashboard-view.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setController(this);
            VBox dashboardView = loader.load();
            setupDashboardView();
            mainPane.setCenter(dashboardView);
            statusLabel.setText("Navigation: Dashboard");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
        } catch (IOException e) {
            statusLabel.setText("Error: Could not load Dashboard view!");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleAddStudent() {
        String name = nameInput.getText().trim();
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
            nameInput.clear();
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
        courseTable.setPlaceholder(new Label("No courses yet. Add one using the form below."));
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
        if (selected == null) {
            statusLabel.setText("Error: No student selected to delete!");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("You are about to delete " + selected.getName());
        alert.setContentText("Are you sure you want to delete this student? This action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            schoolManager.removeStudent(selected.getId());
            studentData.remove(selected);
            statusLabel.setText("Student " + selected.getName() + " deleted successfully.");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
        } else {
            statusLabel.setText("Deletion cancelled.");
            statusLabel.setStyle("-fx-text-fill: #34495e;");
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
            alert.setContentText("Are you sure you want to delete this course? This action cannot be undone.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                courseData.remove(selectedCourse); // Update UI
                schoolManager.removeCourse(selectedCourse.getCourseID()); // Update Manager logic
                statusLabel.setText("Course deleted successfully.");
                statusLabel.setStyle("-fx-text-fill: #27ae60;");
            } else {
                statusLabel.setText("Deletion cancelled.");
                statusLabel.setStyle("-fx-text-fill: #34495e;");
            }
        } else {
            statusLabel.setText("Error: Please select a course first!");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    @FXML
    private void cleanStudentInput() {
        nameInput.clear();
        idInput.clear();
        emailInput.clear();
    }

    private static String pluralize(int count, String singular, String plural) {
        return count + " " + (count == 1 ? singular : plural);
    }

    @FXML
    private void handleShowEnrollments() {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/school_management_system/enrollment-view.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setController(this);
            VBox enrollmentView = loader.load();
            setupEnrollmentView();
            mainPane.setCenter(enrollmentView);
            statusLabel.setText("Navigation: Enrollment Management");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
        } catch (IOException e) {
            statusLabel.setText("Error: Could not load Enrollment view!");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            e.printStackTrace();
        }
    }

    private void setupEnrollmentView() {
        studentComboBox.setItems(FXCollections.observableArrayList(schoolManager.getAllStudents()));
        courseComboBox.setItems(FXCollections.observableArrayList(schoolManager.getAllCourses()));

        enrollmentStudentNameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().studentName()));
        enrollmentStudentIdColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().studentId()));
        enrollmentCourseTitleColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().courseTitle()));
        enrollmentCourseIdColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().courseId()));
        enrollmentCreditsColumn.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().credits()));

        enrollmentTable.setItems(enrollmentData);
        enrollmentTable.setPlaceholder(new Label("No enrollments yet. Select a student and a course above and click Enroll."));

        refreshEnrollmentData();
    }

    @FXML
    private void handleEnroll() {
        Student selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
        Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();

        if (selectedStudent == null || selectedCourse == null) {
            statusLabel.setText("Error: Please select both a student and a course.");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        if (selectedStudent.isEnrolled(selectedCourse)) {
            statusLabel.setText("Error: " + selectedStudent.getName()
                    + " is already enrolled in " + selectedCourse.getTitle() + ".");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        selectedStudent.enroll(selectedCourse);
        refreshEnrollmentData();
        statusLabel.setText(selectedStudent.getName() + " enrolled in " + selectedCourse.getTitle() + ".");
        statusLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    @FXML
    private void handleUnenroll() {
        EnrollmentRow selected = enrollmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Error: Please select an enrollment to remove.");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        Student student = schoolManager.findStudentById(selected.studentId());
        Course course = schoolManager.findCourseById(selected.courseId());
        if (student != null && course != null) {
            student.unenroll(course);
            refreshEnrollmentData();
            statusLabel.setText(student.getName() + " unenrolled from " + course.getTitle() + ".");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
        }
    }

    private void refreshEnrollmentData() {
        enrollmentData.clear();
        for (Student student : schoolManager.getAllStudents()) {
            for (Course course : student.getCoursesEnrolled()) {
                enrollmentData.add(new EnrollmentRow(
                        student.getName(),
                        student.getId(),
                        course.getTitle(),
                        course.getCourseID(),
                        course.getNumberOfCredits()
                ));
            }
        }
    }

    private void setupDashboardView() {
        var allStudents = schoolManager.getAllStudents();
        var allCourses = schoolManager.getAllCourses();

        long enrolledCount = allStudents.stream()
                .filter(s -> !s.getCoursesEnrolled().isEmpty())
                .count();
        long notEnrolledCount = allStudents.size() - enrolledCount;

        totalStudentsLabel.setText(String.valueOf(allStudents.size()));
        totalCoursesLabel.setText(String.valueOf(allCourses.size()));
        enrolledStudentsLabel.setText(String.valueOf(enrolledCount));
        notEnrolledLabel.setText(String.valueOf(notEnrolledCount));

        breakdownCourseColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().courseTitle()));
        breakdownIdColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().courseId()));
        breakdownCountColumn.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().studentCount()).asObject());

        List<DashboardCourseRow> rows = allCourses.stream()
                .map(c -> new DashboardCourseRow(c.getTitle(), c.getCourseID(), c.getEnrolledStudents().size()))
                .sorted(Comparator.comparingInt(DashboardCourseRow::studentCount).reversed())
                .toList();
        breakdownTable.setItems(FXCollections.observableArrayList(rows));

        List<Student> notEnrolled = allStudents.stream()
                .filter(s -> s.getCoursesEnrolled().isEmpty())
                .toList();
        notEnrolledList.setItems(FXCollections.observableArrayList(notEnrolled));
        notEnrolledList.setPlaceholder(new Label("All students are enrolled in at least one course."));
    }

    @FXML
    private void handleShowStudents() {
        mainPane.setCenter(studentsView);
        statusLabel.setText("Navigation: Students");
        statusLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    public record EnrollmentRow(String studentName, String studentId, String courseTitle, String courseId, int credits) {}
    public record DashboardCourseRow(String courseTitle, String courseId, int studentCount) {}
}