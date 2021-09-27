package com.danielezihe.unitTests;

import com.danielezihe.controllers.UserController;
import com.danielezihe.hibernate.entity.QUser;
import com.danielezihe.hibernate.entity.User;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.*;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 25/09/2021
 */
public class UserControllerTest {
    JPAQuery<User> query;
    QUser user;

    public static final Logger logger = LogManager.getLogger(UserControllerTest.class);

    static {
        // Log4j
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");
    }

    @BeforeEach
    void setUp() {
        query = new JPAQuery<>(HibernateUtil.getSessionFactory().openSession());
        user = QUser.user;
    }

    @Test
    @DisplayName("checksIfAUserIsRegisteredSuccessfully")
    void checksIfAUserIsRegisteredSuccessfully() {
        String userEmail = "sezihe@gmail.com";
        UserController newUser = new UserController("Daniel", userEmail, "This!sAStrongPassword!");
        newUser.save();

        // find new user with QueryDSL
        User justSavedUser = query.from(user).where(user.email.eq(userEmail)).fetchOne();
        logger.info("USER: " + justSavedUser);

        assert justSavedUser != null;
        Assertions.assertEquals(userEmail, justSavedUser.getEmail());
    }

    @Test
    @DisplayName("checksIfUserPasswordIsGettingHashed")
    void checksIfUserPasswordIsGettingHashed() {
        String userEmail = "sezihe@gmail.com";
        String password = "This!sAStrongPassword!";
        UserController newUser = new UserController("Daniel", userEmail, password);
        newUser.save();

        // find new user with QueryDSL
        User justSavedUser = query.from(user).where(user.email.eq(userEmail)).fetchOne();
        logger.info("USER: " + justSavedUser);

        assert justSavedUser != null;
        Assertions.assertEquals(userEmail, justSavedUser.getEmail());

        // assert that the password was not left the same
        Assertions.assertNotEquals(password, justSavedUser.getPassword());
    }
}
