package com.danielezihe.controllers;

import com.danielezihe.hibernate.entity.Comment;
import com.danielezihe.hibernate.entity.Post;
import com.danielezihe.hibernate.entity.QPost;
import com.danielezihe.hibernate.entity.User;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 25/09/2021
 */
public class PostController {
    private final User user_id;
    private final String post;
    private final Set<Integer> comments_ids;
    static QPost qPost = QPost.post1;
    static JPAQueryFactory queryFactory = new JPAQueryFactory(HibernateUtil.getSessionFactory().openSession());

    public PostController(User user_id, String post) {
        this.user_id = user_id;
        this.post = post;
        comments_ids = new HashSet<>();
    }

    public Post save() {
        Post post = new Post(0, user_id, getPost(), comments_ids);

        HibernateUtil.addToDB(post);

        return post;
    }

    public static void addComment(Post post, Comment comment) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            post.getComments().add(comment.getId());
            session.merge(post);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static Post findPostById(int id) {
        return (Post) queryFactory.from(qPost).where(qPost.id.eq(id)).fetchOne();
    }

    public static List<Post> getAllPosts() {
        return (List<Post>) queryFactory.from(qPost).orderBy(qPost.id.asc()).fetch();
    }

    public String getPost() {
        return post;
    }
}
