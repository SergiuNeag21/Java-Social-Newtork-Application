package com.example.socialnetworkjava2;

import com.example.socialnetworkjava2.domain.User;
import com.example.socialnetworkjava2.service.Service;
import com.example.socialnetworkjava2.utils.Event;
import com.example.socialnetworkjava2.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.List;

public class RegisterViewController implements Observer<Event>{
    public Text errorField;
    private Service service;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;


    void setService(Service service){
        this.service = service;
        service.addObserver((Observer<Event>) this);
    }

    public void handleRegisterUser(ActionEvent actionEvent) throws IOException, InterruptedException{
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            errorField.setText("All fields are required!");
            return;
        }else{
            if (service.findUserByEmail(emailField.getText()).getId() != null){
                errorField.setText("This e-mail already exist!");
                return;
            }
        }
        User user = new User();
        user.setFirst_name(firstNameField.getText());
        user.setLast_name(lastNameField.getText());
        user.setEmail(emailField.getText());
        user.setPassword(passwordField.getText());
        service.addUser(user);

        if (service.findUserByEmail(user.getEmail()).getId()!= null){
            errorField.setText("\n" +
                    "You have successfully registered!");
            Thread.sleep(1000);

        }else{
            errorField.setText("The account could not be created!");
        }
    }

    @Override
    public void update(Event event) {

    }


    @FXML
    public void handleGoBack(ActionEvent actionEvent) throws IOException{
        // revenirea la stage-ul initial

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginRegisterView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 549, 700);
        LoginSingupViewController loginSingupViewController = fxmlLoader.getController();
        loginSingupViewController.setService(service);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
