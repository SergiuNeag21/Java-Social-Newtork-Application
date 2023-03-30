package com.example.socialnetworkjava2.repository;

import com.example.socialnetworkjava2.domain.FriendRequest;
import com.example.socialnetworkjava2.domain.Friendship;
import com.example.socialnetworkjava2.domain.validate.Validator;
import com.example.socialnetworkjava2.utils.ChangeEvent;
import com.example.socialnetworkjava2.utils.ChangeEventType;
import com.example.socialnetworkjava2.utils.Event;
import com.example.socialnetworkjava2.utils.observer.Observable;
import com.example.socialnetworkjava2.utils.observer.Observer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDbRepo implements Repo<Long, Friendship>, Observable<Event> {
    private Connection connection;
    private Validator<Friendship> validator;
    private List<Observer<Event>> observers;

    public FriendshipDbRepo(Connection connection, Validator<Friendship> validator) {
        this.connection = connection;
        this.validator = validator;
        this.observers = new ArrayList<>();
    }


    @Override
    public Friendship findOne(Long id) {
        Friendship friendship = null;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM friendship WHERE id_user1=?");
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                friendship = new Friendship();
                friendship.setId(rs.getLong("id_friendship"));
                friendship.setId_user1(rs.getLong("id_user1"));
                friendship.setId_user2(rs.getLong("id_user2"));
                friendship.setDate(rs.getDate("date"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return friendship;
    }

    @Override
    public List<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM friendship");
            while (rs.next()) {
                Friendship friendship = new Friendship();
                friendship.setId(rs.getLong("id_friendship"));
                friendship.setId_user1(rs.getLong("id_user1"));
                friendship.setId_user2(rs.getLong("id_user2"));
                friendship.setDate(rs.getDate("date"));
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship friendship) {
        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO friendship (id_user1, id_user2, date) VALUES (?, ?, ?)");
            st.setLong(1, friendship.getId_user1());
            st.setLong(2, friendship.getId_user2());
            st.setDate(3, (Date) friendship.getDate());
            st.executeUpdate();
            notifyObserver(new ChangeEvent(ChangeEventType.ADD,friendship));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendship;
    }

    @Override
    public Friendship delete(Long id) {
        Friendship friendship = findOne(id);
        if (friendship != null) {
            try {
                PreparedStatement st = connection.prepareStatement("DELETE FROM friendship WHERE id_friendship=?");
                st.setLong(1, id);
                st.executeUpdate();
                notifyObserver(new ChangeEvent(ChangeEventType.DELETE,friendship));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return friendship;
    }
    @Override
    public Friendship update(Friendship entity) {
        return null;
    }

    @Override
    public void addObserver(Observer<Event> e) {

    }

    @Override
    public void notifyObserver(Event t) {
        observers.forEach(o -> o.update(t));
    }

    public List<Friendship> findAllForUser(Long userId) {
        List<Friendship> friendships = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM friendship WHERE id_user1=? or id_user2=?");
            st.setLong(1, userId);
            st.setLong(2, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Friendship friendship = new Friendship();
                friendship.setId(rs.getLong("id_friendship"));
                friendship.setId_user1(rs.getLong("id_user1"));
                friendship.setId_user2(rs.getLong("id_user2"));
                friendship.setDate(rs.getDate("date"));
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    public void deleteFriendshipByIdUser1IdUser2(Long id_user1, Long id_user2){
        Friendship friendship = findOne(id_user1);
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM friendship WHERE (id_user1 = ? and id_user2 = ?) or (id_user2 = ? and id_user1 = ?)");
            st.setLong(1, id_user1);
            st.setLong(2, id_user2);
            st.setLong(4, id_user2);
            st.setLong(3, id_user1);
            st.executeUpdate();

            notifyObserver(new ChangeEvent(ChangeEventType.DELETE,friendship));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Friendship> findAllFriendsForUser(Long userId) {
        List<Friendship> friendships = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM friendship WHERE id_user1 = ? or id_user2 = ?");
            st.setLong(1, userId);
            st.setLong(2, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Friendship friendship = new Friendship();
                friendship.setId(rs.getLong("id_friendship"));
                friendship.setId_user1(rs.getLong("id_user1"));
                friendship.setId_user2(rs.getLong("id_user2"));
                friendship.setDate(rs.getDate("date"));
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }
}
