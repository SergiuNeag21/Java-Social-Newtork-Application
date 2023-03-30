package com.example.socialnetworkjava2.domain;

import java.util.Date;

public class Friendship extends Entity<Long>{
    private Long id_user1;
    private Long id_user2;
    private Date date;

    public Friendship(Long user1, Long user2, Date date) {
        this.id_user1 = user1;
        this.id_user2 = user2;
        this.date = date;
    }

    public Friendship() {
    }


    public Long getId_user1() {
        return id_user1;
    }

    public void setId_user1(Long id_user1) {
        this.id_user1 = id_user1;
    }

    public Long getId_user2() {
        return id_user2;
    }

    public void setId_user2(Long id_user2) {
        this.id_user2 = id_user2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
