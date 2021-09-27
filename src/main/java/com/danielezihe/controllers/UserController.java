package com.danielezihe.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.danielezihe.hibernate.entity.QUser;
import com.danielezihe.hibernate.entity.User;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 25/09/2021
 */
public record UserController(String name, String email, String password) {

    public User save() {
        String hashedPassword = hashPassword(password);
        User user = new User(0, name, email, hashedPassword);

        HibernateUtil.addToDB(user);

        return user;
    }

    public static <T> T login(String email, String password) {
        QUser qUser = QUser.user;
        JPAQuery<User> query = new JPAQuery<>(HibernateUtil.getSessionFactory().openSession());

        User user = query.from(qUser).where(qUser.email.eq(email)).fetchOne();

        if (user == null)
            return (T) "INVALID USER";

        if (!verifyPassword(password, user.getPassword())) {
            return (T) "INCORRECT DETAILS";
        } else {
            return (T) user;
        }
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(10, password.toCharArray());
    }

    private static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
    }
}
