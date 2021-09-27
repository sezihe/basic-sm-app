package com.danielezihe.unitTests;

import com.danielezihe.controllers.CommentController;
import com.danielezihe.controllers.PostController;
import com.danielezihe.controllers.UserController;
import com.danielezihe.hibernate.entity.*;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 25/09/2021
 */
public class CommentControllerTest {
    JPAQueryFactory queryFactory;
    QUser user;
    QPost post;
    QComment comment;

    public static final Logger logger = LogManager.getLogger(CommentControllerTest.class);

    static {
        // Log4j
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");
    }

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(HibernateUtil.getSessionFactory().openSession());
        user = QUser.user;
        post = QPost.post1;
        comment = QComment.comment1;
    }

    @Test
    @DisplayName("checksIfACommentIsSaved")
    void checksIfACommentIsSaved() {
        User newUser = createAUser("Daniel", "esouzo61@gmail.com");

        Post johnsPost = createAPost();

        CommentController newComment = new CommentController(johnsPost, newUser, "Ha, Hello!");
        newComment.save();

        Comment newlyAddedComment = (Comment) queryFactory.from(comment).where(comment.comment.eq("Ha, Hello!")).fetchOne();

        assert newlyAddedComment != null;

        PostController.addComment(johnsPost, newlyAddedComment);
        logger.info("COMMENT: " + newlyAddedComment);

        Assertions.assertEquals(newComment.comment(), newlyAddedComment.getComment());
    }

    @Test
    @DisplayName("testsGetAllCommentsMethod")
    void testsGetAllCommentsMethod() {
        User newUser = createAUser("Daniel", "esouzo61@gmail.com");

        Post johnsPost = createAPost();

        CommentController newComment = new CommentController(johnsPost, newUser, "Ha, Hello!");
        Comment comment = newComment.save();
        PostController.addComment(johnsPost, comment);

        CommentController newComment1 = new CommentController(johnsPost, newUser, "HEY!");
        Comment comment1 = newComment1.save();
        PostController.addComment(johnsPost, comment1);

        List<Comment> comments = CommentController.getAllCommentsFromASetOfIDs(johnsPost.getComments());

        Assertions.assertEquals(2, comments.size());
    }


    // UTILITIES
    private User createAUser(String name, String email) {
        UserController newUser = new UserController(name, email, "This!sAStrongPassword!");

        return newUser.save();
    }

    public Post createAPost() {
        User aUser = createAUser("John", "s@gmail.com");

        PostController newPost = new PostController(aUser, "Hello Everyone");

        return newPost.save();
    }
}
