package com.example.socialnetworkjava2;

import com.example.socialnetworkjava2.domain.User;
import com.example.socialnetworkjava2.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class LoginSingupViewController {
    public Label errorField;
    @FXML
    public Button registerButton;
    private Service service;

    private Connection connection;
    @FXML
    public TextField emailTextField;
    @FXML
    public TextField passwordTextField;

    public void setService(Service service) throws IOException {
        this.service = service;

    }

    public void handleLogin(ActionEvent actionEvent) throws IOException{
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        User user =  service.findUserByEmail(email);
        if (user.getId() != null && user.getPassword().equals(password)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainViewController mainViewController = loader.getController();
            Scene scene = new Scene(root);

            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            mainViewController.setLoggedUser(user);
            mainViewController.setService(service, user);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        }
        else {
            Stage errorPopUp = new Stage();
            errorPopUp.setTitle("Error");
            Label errorLabel = new Label("Wrong email or password!");
            VBox errorBox = new VBox(errorLabel);
            errorBox.setAlignment(Pos.CENTER);
            Scene errorScene = new Scene(errorBox, 300, 200);
            errorPopUp.setScene(errorScene);
            errorPopUp.show();
        }
    }

    public void handleRegisterUserScene(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registerView.fxml"));
        Parent root = loader.load();
        RegisterViewController registerViewController = loader.getController();
        registerViewController.setService(service);
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
