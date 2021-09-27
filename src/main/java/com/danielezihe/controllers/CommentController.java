package com.danielezihe.controllers;

import com.danielezihe.hibernate.entity.Comment;
import com.danielezihe.hibernate.entity.Post;
import com.danielezihe.hibernate.entity.QComment;
import com.danielezihe.hibernate.entity.User;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 25/09/2021
 */
public record CommentController(Post post_id,
                                User user_id, String comment) {

    public Comment save() {
        Comment comment = new Comment(0, post_id, user_id, comment());

        HibernateUtil.addToDB(comment);

        return comment;
    }

    public static List<Comment> getAllCommentsFromASetOfIDs(Set<Integer> comment_ids) {
        QComment qComment = QComment.comment1;
        JPAQueryFactory query = new JPAQueryFactory(HibernateUtil.getSessionFactory().openSession());
        List<Comment> comments = new LinkedList<>();

        for (int comment_id : comment_ids) {
            Comment comment = (Comment) query.from(qComment).where(qComment.id.eq(comment_id)).fetchOne();

            assert comment != null;
            comments.add(comment);
        }

        return comments;
    }
}
