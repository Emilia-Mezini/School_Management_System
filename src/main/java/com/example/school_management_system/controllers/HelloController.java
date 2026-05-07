package com.example.school_management_system.controllers;
import com.example.school_management_system.models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> idColumn;
    @FXML private TableColumn<Student,String> nameColumn;
    @FXML private TableColumn<Student,String> emailColumn;
    @FXML private TextField NameInput;
    @FXML private TextField idInput;
    @FXML private TextField emailInput;
    @FXML private Label statusLabel;

    private ObservableList<Student> studentData = FXCollections.observableArrayList();


    // Will automatically run after FXML is loaded
    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email") );
        studentTable.setItems(studentData);
    }

    // Will run when the "Add Student" button is clicked
    @FXML
    protected void handleAddStudent(){
        String name = NameInput.getText().trim();
        String email = emailInput.getText().trim();
        String id = idInput.getText().trim();
        if(name.isEmpty() || id.isEmpty()){
            //Error feedback status
            statusLabel.setText("Error! Both name and id must be filled.");
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
        else{
            studentData.add(new Student(name,email,id));
            //Success feedback status
            statusLabel.setText("Student: "+name+" added successfully!");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");

            NameInput.clear();
            emailInput.clear();
            idInput.clear();
        }
    }
}
