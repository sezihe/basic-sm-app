package com.danielezihe.hibernate.entity;

import javax.persistence.*;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 25/09/2021
 */
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post_id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user_id;

    @Column(name = "comment")
    private String comment;

    public Comment() {
    }

    public Comment(int id, Post post_id, User user_id, String comment) {
        this.id = id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public User getCommentOwner() {
        return user_id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post_id=" + post_id +
                ", user_id=" + user_id +
                ", comment='" + comment + '\'' +
                '}';
    }
}
