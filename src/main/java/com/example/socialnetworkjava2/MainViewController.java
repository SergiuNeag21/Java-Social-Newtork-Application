package com.example.socialnetworkjava2;

import com.example.socialnetworkjava2.domain.FriendRequest;
import com.example.socialnetworkjava2.domain.Friendship;
import com.example.socialnetworkjava2.domain.User;
import com.example.socialnetworkjava2.service.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class MainViewController {
    private Service service;

    private Connection connection;

    private User loggedUser;


    public TableColumn columnX;

    public TableColumn columnY;

    public TableColumn columnZ;

    public Text menuText;
    

    @FXML
    public TableView userFriendsTable;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Label fullUserNameLabel;

    public Button sendRequestButton;
    public Button unsendRequestButton;
    public Button acceptRequestButton;
    public Button deleteRequestButton;
    public Button deleteFriendButton;

    public void setService(Service service, User user) {
        this.service = service;
        fullUserNameLabel.setText(user.getFirst_name() + " " + user.getLast_name());
        handleShowUserFriends(new ActionEvent());
    }


    //Afisez un tabel cu prietenii userului pe care suntem conectat + culori si asezare
    public void handleShowUserFriends(ActionEvent event){
        sendRequestButton.setVisible(false);
        unsendRequestButton.setVisible(false);
        acceptRequestButton.setVisible(false);
        deleteRequestButton.setVisible(false);
        deleteFriendButton.setVisible(true);
        menuText.setText("FRIENDS");
        List<User> friends = service.getUserFriendships(loggedUser.getId());
        ObservableList<User> friendshipData = FXCollections.observableArrayList(friends);

        columnX.setText("First name");
        columnX.setCellValueFactory(new PropertyValueFactory<>("first_name"));


        columnY.setText("Last name");
        columnY.setCellValueFactory(new PropertyValueFactory<>("last_name"));


        columnZ.setText("email");
        columnZ.setCellValueFactory(new PropertyValueFactory<>("email"));


        userFriendsTable.getColumns().setAll(columnX, columnY, columnZ);
        userFriendsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userFriendsTable.setItems(friendshipData);

    }

    public void handleDeleteFriendship(ActionEvent event){
        User user = (User) userFriendsTable.getSelectionModel().getSelectedItem();
        if (user == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eror");
            alert.setHeaderText("No user selected");
            alert.showAndWait();
            return;
        }

        service.deleteFriendshipByIdUser1IdUser2(loggedUser.getId(), user.getId());
        updateFriendsTable();

        showSuccessPopup("Friend deleted successfully!");

    }

    private void updateFriendsTable(){
        List<Friendship> friendships = service.getFriendships(loggedUser.getId());
        userFriendsTable.setItems(FXCollections.observableArrayList(friendships));
    }

    @FXML
    public void handleGoBack(ActionEvent actionEvent) throws IOException {
        // revenirea la stage-ul initial

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginRegisterView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 549, 700);
        LoginSingupViewController loginSingupViewController = fxmlLoader.getController();
        loginSingupViewController.setService(service);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Friend Request

   // in tabelul friend request voi avea userul ce a trimis cererea + data
    // + buton accept/delete cerere
    public void handleFriendRequest(ActionEvent actionEvent) throws IOException {
        deleteFriendButton.setVisible(false);
        sendRequestButton.setVisible(false);
        unsendRequestButton.setVisible(false);
        acceptRequestButton.setVisible(true);
        deleteRequestButton.setVisible(true);
        menuText.setText("FRIEND REQUEST");
        List<FriendRequest> friendRequests = service.getUserFriendRequests(loggedUser.getId());
        ObservableList<FriendRequest> friendshipData = FXCollections.observableArrayList(friendRequests);
        for (FriendRequest friendRequest:friendRequests){
            columnX.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures param) {

                    return new SimpleStringProperty(service.findOneUser(friendRequest.getId_sender()).getFirst_name());
                }
            });
            columnY.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures param) {
                    return new SimpleStringProperty(service.findOneUser(friendRequest.getId_sender()).getLast_name());
                }
            });

        }
        columnX.setText("First name");
//        columnX.setCellValueFactory(new PropertyValueFactory<>("id_sender"));

        columnY.setText("Last name");
//        columnX.setCellValueFactory(new PropertyValueFactory<>("id_receiver"));

        columnZ.setText("Date");
        columnZ.setCellValueFactory(new PropertyValueFactory<>("date"));

        userFriendsTable.getColumns().setAll(columnX, columnY, columnZ);
        userFriendsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        userFriendsTable.setItems(friendshipData);
    }

    //gestiunea add/delete request
    public void handleSearchFriendTable(ActionEvent actionEvent) throws IOException {
        deleteFriendButton.setVisible(false );
        sendRequestButton.setVisible(true);
        unsendRequestButton.setVisible(true);
        acceptRequestButton.setVisible(false);
        deleteRequestButton.setVisible(false);
        menuText.setText("FIND FRIENDS");

        List<User> noFriends = service.findAllUsersNoFriends(loggedUser.getId());

        columnX.setText("First name");
        columnX.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        columnY.setText("Last name");
        columnY.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        columnZ.setText("Email");
        columnZ.setCellValueFactory(new PropertyValueFactory<>("email"));
        userFriendsTable.getColumns().setAll(columnX, columnY, columnZ);

        ObservableList<User> data = FXCollections.observableArrayList(noFriends);

        userFriendsTable.setItems(data);
    }

    public void handleSendRequest(ActionEvent actionEvent) throws IOException{
        User user = (User) userFriendsTable.getSelectionModel().getSelectedItem();
        //Verific daca friend requestul nu a fost deja trimis de unu dintre useri
        if (user == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eror");
            alert.setHeaderText("No user selected");
            alert.showAndWait();
            return;
        }
        if (service.hasFriendRequestFromUser(loggedUser.getId(), user.getId())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You already have a request from this user");
            alert.showAndWait();
            return;
        }

        if (service.alreadySentFriendRequestToUser(loggedUser.getId(), user.getId())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You already sent a request to this user");
            alert.showAndWait();
            return;
        }
        Date date = Date.valueOf(java.time.LocalDate.now());
        FriendRequest request = new FriendRequest(loggedUser.getId(), user.getId(), date);
        service.addFriendRequest(request);
        showSuccessPopup("Friend request sent successfully!");
    }

    private void showSuccessPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //accept cerere de prietenie primite

    public void acceptFriendRequest(ActionEvent actionEvent) throws IOException{
        FriendRequest friendRequest = (FriendRequest) userFriendsTable.getSelectionModel().getSelectedItem();
        if (friendRequest == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No request selected");
            alert.showAndWait();
            return;
        }
        User sender = service.findOneUser(friendRequest.getId_sender());
        Date date = Date.valueOf(java.time.LocalDate.now());
        Friendship friendship = new Friendship(sender.getId(), loggedUser.getId(), date);
        service.addFriendship(friendship);

        service.deleteFriendRequest(friendRequest.getId_sender());

        updateRequestsTable();

        showSuccessPopup("Friend request accepted successfully!");
    }

    // unsend friend request

    public void unsendFriendRequest(ActionEvent actionEvent) throws IOException{
        User user = (User) userFriendsTable.getSelectionModel().getSelectedItem();
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No user selected");
            alert.showAndWait();
            return;
        }
        if (service.findOneFriendRequestBySenderReceiver(loggedUser.getId(), user.getId()) == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You didn't send a friend request to this user!");
            alert.showAndWait();
            return;
        }
        service.deleteRequestBySenderReceiver(loggedUser.getId(), user.getId());


        showSuccessPopup("The friend request has been successfully unsent!");
    }


    // delete friend request de catre userul ce primeste requestul
    public void deleteReceivedFriendRequest(ActionEvent actionEvent) throws IOException{
        FriendRequest friendRequest = (FriendRequest) userFriendsTable.getSelectionModel().getSelectedItem();
        if (friendRequest == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No request selected");
            alert.showAndWait();
            return;
        }
        service.deleteFriendRequest(friendRequest.getId_sender());

        updateRequestsTable();

        showSuccessPopup("Friend request deleted successfully!");
    }


    private void updateRequestsTable(){
        List<FriendRequest> requests = service.getRequests(loggedUser.getId());
        userFriendsTable.setItems(FXCollections.observableArrayList(requests));
    }


}
