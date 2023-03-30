package com.example.socialnetworkjava2.repository;

import com.example.socialnetworkjava2.domain.FriendRequest;
import com.example.socialnetworkjava2.domain.validate.Validator;
import com.example.socialnetworkjava2.utils.ChangeEvent;
import com.example.socialnetworkjava2.utils.ChangeEventType;
import com.example.socialnetworkjava2.utils.Event;
import com.example.socialnetworkjava2.utils.observer.Observable;
import com.example.socialnetworkjava2.utils.observer.Observer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDbRepo implements Repo<Long, FriendRequest>, Observable<Event> {
    private Connection connection;
    private Validator<FriendRequest> validator;
    private List<Observer<Event>> obeservers;

    public FriendRequestDbRepo(Connection connection, Validator<FriendRequest> validator) {
        this.connection = connection;
        this.validator = validator;
        this.obeservers = new ArrayList<>();
    }

    @Override
    public FriendRequest findOne(Long id) {
        FriendRequest friendRequest = null;
        try {
            String sql = "SELECT * FROM friend_request WHERE id_sender = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                friendRequest = new FriendRequest();
                friendRequest.setId(resultSet.getLong("id_friendRequest"));
                friendRequest.setId_sender(resultSet.getLong("id_sender"));
                friendRequest.setId_receiver(resultSet.getLong("id_receiver"));
                friendRequest.setDate(resultSet.getDate("date"));
                return friendRequest;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    @Override
    public List<FriendRequest> findAll() {
        try {
            String sql = "SELECT * FROM friend_request";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<FriendRequest> FriendRequest = new ArrayList<>();
            while (resultSet.next()) {
                FriendRequest friendRequest = new FriendRequest();
                friendRequest.setId(resultSet.getLong("id_friendRequest"));
                friendRequest.setId_sender(resultSet.getLong("id_sender"));
                friendRequest.setId_receiver(resultSet.getLong("id_receiver"));
                friendRequest.setDate(resultSet.getDate("date"));
                FriendRequest.add((FriendRequest) friendRequest);
            }
            return FriendRequest;
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
//        FriendRequest friendRequest = entity;
        validator.validate(friendRequest);
        try {
            String sql = "INSERT INTO friend_request (id_sender, id_receiver, date) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, friendRequest.getId_sender());
            statement.setLong(2, friendRequest.getId_receiver());
            statement.setDate(3, (Date) friendRequest.getDate());
            statement.executeUpdate();
            notifyObserver(new ChangeEvent(ChangeEventType.ADD,friendRequest));
        } catch (SQLException e) {
            e.getStackTrace();
        }

        return friendRequest;
    }

    //stergerea si findOne au ca parametru id-ul senderului deoarece pentru orice user conectat
    //poate avea un singur friend_request de la userul respectiv
    @Override
    public FriendRequest delete(Long id) {
        FriendRequest friendRequest = findOne(id);
        try {
            String sql = "DELETE FROM friend_request WHERE id_sender = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
            notifyObserver(new ChangeEvent(ChangeEventType.DELETE,friendRequest));
        } catch (SQLException e) {
            e.getStackTrace();;
        }
        return friendRequest;
    }

    public void deleteRequestBySenderReceiver(Long id_sender, Long id_receiver){
        FriendRequest friendRequest = findOne(id_receiver);
        try {
            String sql = "DELETE FROM friend_request WHERE id_sender = ? and id_receiver = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id_sender);
            statement.setLong(2, id_receiver);
            statement.executeUpdate();
            notifyObserver(new ChangeEvent(ChangeEventType.DELETE,friendRequest));
        } catch (SQLException e) {
            e.getStackTrace();;
        }
    }

    public List<FriendRequest> findAllForUser(Long userId) {
        List<FriendRequest> friendRequests = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM friend_request WHERE id_receiver=?");
            st.setLong(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                FriendRequest friendRequest = new FriendRequest();
                friendRequest.setId(rs.getLong("id_friendRequest"));
                friendRequest.setId_sender(rs.getLong("id_sender"));
                friendRequest.setId_receiver(rs.getLong("id_receiver"));
                friendRequest.setDate(rs.getDate("date"));
                friendRequests.add(friendRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }

    @Override
    public FriendRequest update(FriendRequest entity) {
        return null;
    }


    public FriendRequest findOneBySenderReceiver(Long id_sender, Long id_receiver) {
        FriendRequest friendRequest = null;
        try {
            String sql = "SELECT * FROM friend_request WHERE id_sender = ? and id_receiver = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id_sender);
            statement.setLong(2, id_receiver);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                friendRequest = new FriendRequest();
                friendRequest.setId(resultSet.getLong("id_friendRequest"));
                friendRequest.setId_sender(resultSet.getLong("id_sender"));
                friendRequest.setId_receiver(resultSet.getLong("id_receiver"));
                friendRequest.setDate(resultSet.getDate("date"));
                return friendRequest;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    @Override
    public void addObserver(Observer<Event> e) {

    }

    @Override
    public void notifyObserver(Event t) {

    }
}
