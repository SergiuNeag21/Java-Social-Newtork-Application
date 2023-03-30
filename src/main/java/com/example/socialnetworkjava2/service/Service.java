package com.example.socialnetworkjava2.service;


import com.example.socialnetworkjava2.domain.FriendRequest;
import com.example.socialnetworkjava2.domain.Friendship;
import com.example.socialnetworkjava2.domain.User;
import com.example.socialnetworkjava2.repository.FriendRequestDbRepo;
import com.example.socialnetworkjava2.repository.FriendshipDbRepo;
import com.example.socialnetworkjava2.repository.UserDbRepo;
import com.example.socialnetworkjava2.utils.Event;
import com.example.socialnetworkjava2.utils.observer.Observable;
import com.example.socialnetworkjava2.utils.observer.Observer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service implements Observable<Event> {
    private UserDbRepo userRepository;
    private FriendshipDbRepo friendshipRepository;

    private FriendRequestDbRepo friendRequestRepository;

    private Connection connection;
    private List<Observer<Event>> observers;

    public Service(UserDbRepo userRepository, FriendshipDbRepo friendshipRepository, FriendRequestDbRepo friendRequestRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendRequestRepository = friendRequestRepository;
        observers = new ArrayList<>();
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/socialnetwork5",
                    "postgres" , "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // User methods
    public User addUser(User user){return userRepository.save(user);}

    public User deleteUser(Long id){return userRepository.delete(id);}

    public User updateUser(User user){return userRepository.update(user);}

    public User findOneUser(Long id){return userRepository.findOne(id);}

    public int repoSize(UserDbRepo userRepository){return userRepository.repoSize(userRepository.findAll());}

    public List<User> findAllUser(){return userRepository.findAll();}

    public User findUserByEmail(String email){return userRepository.findUserByEmail(email);}

    public Long getNextId(){return userRepository.getNextId();}


// Friendship methods
    public Friendship addFriendship(Friendship friendship){return friendshipRepository.save(friendship);}

    public void deleteFriendshipByIdUser1IdUser2(Long id_user1, Long id_user2) {
        friendshipRepository.deleteFriendshipByIdUser1IdUser2(id_user1, id_user2);
    }

    public Friendship deleteFriendship(Long id){
        return friendshipRepository.delete(id);
    }


    public List<User> getUserFriendships(Long userId) {
        List<User> userFriendsList = new ArrayList<>();
       List<Friendship> friends = friendshipRepository.findAllForUser(userId);
       for (Friendship friendship:friends){
           if (friendship.getId_user1().equals(userId)){
               User user = userRepository.findOne(friendship.getId_user2());
               userFriendsList.add(user);
           }
           else {
               User user = userRepository.findOne(friendship.getId_user1());
               userFriendsList.add(user);
           }
       }
       return userFriendsList;
    }

    public List<Friendship> findAllFriendships() {
        return friendshipRepository.findAll();
    }


    //returneaza o lista de useri care nu sunt prienteni cu un user dat
    public List<User> findAllUsersNoFriends(long userId){
        List<User> userNoFriendList = new ArrayList<>();
        List<User> users = userRepository.findAll();
        List<Friendship> friendships = friendshipRepository.findAllForUser(userId);
        for (User user: users){
            int i = 0;
            boolean isFriend = false;
            if (user.getId().equals(userId)) isFriend = true;
            for (Friendship friendship:friendships){
                if (friendship.getId_user1().equals(user.getId()) && friendship.getId_user2().equals(userId)
                || friendship.getId_user2().equals(user.getId()) && friendship.getId_user1().equals(userId)) isFriend = true;
            }
            if (!isFriend){
                userNoFriendList.add(user);
            }
        }

        return userNoFriendList;
    }


    //Friend request methods
    public FriendRequest addFriendRequest(FriendRequest friendRequest){return friendRequestRepository.save(friendRequest);}

    public FriendRequest deleteFriendRequest(Long id){return friendRequestRepository.delete(id);}

    public FriendRequest findOneFriendRequest(Long id){return friendRequestRepository.findOne(id);}

    public FriendRequest findOneFriendRequestBySenderReceiver(Long id_sender, Long id_receiver){
        return friendRequestRepository.findOneBySenderReceiver(id_sender, id_receiver);}

    public void deleteRequestBySenderReceiver(Long id_sender, Long id_receiver){
        friendRequestRepository.deleteRequestBySenderReceiver(id_sender, id_receiver);
    }

    public List<FriendRequest> getRequests(Long userId) {
        return friendRequestRepository.findAllForUser(userId);
    }

    public List<Friendship> getFriendships(Long userId){
        return friendshipRepository.findAllFriendsForUser(userId);
    }


    public Long getFriendRequestId(Long id_sender, Long id_receiver) throws SQLException {
        Long id = null;
        PreparedStatement statement = connection.prepareStatement("SELECT id_friendRequest FROM FriendRequest WHERE id_sender = ? AND id_receiver = ?");
        statement.setLong(1, id_sender);
        statement.setLong(2, id_receiver);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            id = result.getLong("id_friendRequest");}
        return id;
    }
    public boolean hasFriendRequestFromUser(Long idLoggedUser, Long idSender){
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        for (FriendRequest friendRequest: friendRequests){
            if (friendRequest.getId_receiver().equals(idLoggedUser) && friendRequest.getId_sender().equals(idSender))
                return true;
        }
        return false;
    }

    public boolean alreadySentFriendRequestToUser(Long idLoggedUser, Long idReciver){
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        for (FriendRequest friendRequest: friendRequests){
            if (friendRequest.getId_receiver().equals(idReciver) && friendRequest.getId_sender().equals(idLoggedUser))
                return true;
        }
        return false;
    }

    // returneaza o lista de friend requesturi primite de un user
    public List<FriendRequest> getUserFriendRequests(Long idUser) {
        List<FriendRequest> userFriendRequestsList = new ArrayList<>(); //lista de friend request ale userului dat
        List<FriendRequest> allFriendRequestsList = friendRequestRepository.findAll();
        for (FriendRequest friendRequest : allFriendRequestsList){
            if (friendRequest.getId_receiver().equals(idUser))
                userFriendRequestsList.add(friendRequest);
        }
        return userFriendRequestsList;
    }



    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void notifyObserver(Event t) {
        observers.forEach(o->o.update(t));
    }

}
