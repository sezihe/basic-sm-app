package com.danielezihe.hibernate.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 25/09/2021
 */
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user_id;

    @Column(name = "post")
    private String post;

    @ElementCollection
    @Column(name = "comment_ids")
    private Set<Integer> comments_ids;

    public Post() {
    }

    public Post(int id, User user_id, String post, Set<Integer> comments_ids) {
        this.id = id;
        this.user_id = user_id;
        this.post = post;
        this.comments_ids = comments_ids;
    }

    public int getId() {
        return id;
    }

    public String getPost() {
        return post;
    }

    public Set<Integer> getComments() {
        return comments_ids;
    }

    public User getPostOwner() {
        return user_id;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", post='" + post + '\'' +
                ", comments_ids=" + comments_ids +
                '}';
    }
}
