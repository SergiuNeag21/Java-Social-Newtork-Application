package com.example.socialnetworkjava2.domain;

import java.util.Date;
import java.util.Objects;

public class FriendRequest extends Entity<Long>{
    private Long id_sender;
    private Long id_receiver;
    private Date date;

    public FriendRequest() {

    }

    public FriendRequest(Long senderId, Long receiverId, java.sql.Date date) {
        this.id_sender = senderId;
        this.id_receiver = receiverId;
        this.date = date;
    }

    public Long getId_sender() {
        return id_sender;
    }

    public void setId_sender(Long id_sender) {
        this.id_sender = id_sender;
    }

    public Long getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(Long id_receiver) {
        this.id_receiver = id_receiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(id_sender, that.id_sender) && Objects.equals(id_receiver, that.id_receiver) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_sender, id_receiver, date);
    }
}
