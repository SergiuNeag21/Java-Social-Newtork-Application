package com.example.socialnetworkjava2;



import com.example.socialnetworkjava2.domain.validate.FriendRequestValidator;
import com.example.socialnetworkjava2.domain.validate.FriendshipValidator;
import com.example.socialnetworkjava2.domain.validate.UserValidator;
import com.example.socialnetworkjava2.repository.FriendRequestDbRepo;
import com.example.socialnetworkjava2.repository.FriendshipDbRepo;
import com.example.socialnetworkjava2.repository.UserDbRepo;
import com.example.socialnetworkjava2.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/socialnetwork5";
        String username =  "postgres";
        String password =  "postgres";

        Connection connection = DriverManager.getConnection(url, username, password);
        UserDbRepo userRepository = new UserDbRepo(connection, new UserValidator());
        FriendshipDbRepo friendshipRepository = new FriendshipDbRepo(connection, new FriendshipValidator());
        FriendRequestDbRepo friendRequestRepository = new FriendRequestDbRepo(connection, new FriendRequestValidator());
        Service service = new Service(userRepository, friendshipRepository, friendRequestRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginRegisterView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 549, 700);

        LoginSingupViewController loginSingupViewController = fxmlLoader.getController();
        loginSingupViewController.setService(service);

        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {launch();}
}