package com.danielezihe.unitTests;

import com.danielezihe.controllers.PostController;
import com.danielezihe.controllers.UserController;
import com.danielezihe.hibernate.entity.Post;
import com.danielezihe.hibernate.entity.QPost;
import com.danielezihe.hibernate.entity.QUser;
import com.danielezihe.hibernate.entity.User;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQuery;
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
public class PostControllerTest {
    JPAQuery<Post> postQuery;
    QPost post;

    public static final Logger logger = LogManager.getLogger(PostControllerTest.class);

    static {
        // Log4j
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");
    }

    @BeforeEach
    void setUp() {
        postQuery = new JPAQuery<>(HibernateUtil.getSessionFactory().openSession());
        post = QPost.post1;
    }

    @Test
    @DisplayName("checksIfAPostIsActuallySaved")
    void checksIfAPostIsActuallySaved() {
        User aUser = createAUser();

        PostController newPost = new PostController(aUser, "Hello Everyone");
        newPost.save();

        Post justAddedPost = postQuery.from(post).where(post.post.eq("Hello Everyone")).fetchOne();
        logger.info("POST: " + justAddedPost);

        PostController.getAllPosts();

        assert justAddedPost != null;
        Assertions.assertEquals(newPost.getPost(), justAddedPost.getPost());
    }

    @Test
    @DisplayName("testsFindPostByIDMethod")
    void testsFindPostByIdMethod() {
        User aUser = createAUser();

        PostController newPost = new PostController(aUser, "Hello Everyone");
        Post post = newPost.save();

        Post dbPost = PostController.findPostById(post.getId());

        Assertions.assertEquals(post.getId(), dbPost.getId());
    }

    @Test
    @DisplayName("testsGetAllPostsMethod")
    void testsGetAllPostsMethod() {
        User aUser = createAUser();

        new PostController(aUser, "Hello Everyone").save();

        new PostController(aUser, "Hi Everyone").save();

        new PostController(aUser, "Hey Everyone").save();

        List<Post> posts = PostController.getAllPosts();

        Assertions.assertEquals(3, posts.size());
    }

    // UTILITIES
    private User createAUser() {
        UserController newUser = new UserController("Daniel", "esouzo61@gmail.com", "This!sAStrongPassword!");

        return newUser.save();
    }
}
