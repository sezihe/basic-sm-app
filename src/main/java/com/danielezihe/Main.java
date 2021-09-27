package com.danielezihe;

import com.danielezihe.controllers.CommentController;
import com.danielezihe.controllers.PostController;
import com.danielezihe.controllers.UserController;
import com.danielezihe.hibernate.entity.Comment;
import com.danielezihe.hibernate.entity.Post;
import com.danielezihe.hibernate.entity.User;

import java.util.*;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 25/09/2021
 */
public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        println("Welcome!");
        goToMain();
    }

    static void handleRegister() {
        println("Kindly Enter your name, email address and password all seperated with commas Eg: Daniel,sezihe@gmail.com,Th!s!sAStrongPassword!");
        String[] data = scanner.nextLine().split(",");

        UserController newUser = new UserController(data[0], data[1], data[2]);
        newUser.save();

        println("User Registered Successfully. Please login\n");
    }

    static void handleLogin() {
        println("Kindly Enter your email & password seperated by a comma. Eg: sezihe@gmail.com,Th!s!sAStrongPassword!");
        String[] data = scanner.nextLine().split(",");

        var response = UserController.login(data[0], data[1]);

        if(response instanceof User) {
            goHome((User) response);
        } else {
            System.out.println(response + "\n");
        }
    }

    static void handleCreateNewPost(User user) {
        println("Enter your new post below: ");
        String post = scanner.nextLine();

        PostController newPost = new PostController(user, post);
        newPost.save();

        println("POST ADDED SUCCESSFULLY\n");
    }

    static void handleShowAllPosts(User user) {
        StringBuilder postStringBuilder = new StringBuilder();
        List<Post> posts = PostController.getAllPosts();

        if(posts.isEmpty()) {
            println("--------------------POSTS--------------------");
            println("                  No Posts.                  ");
            println("---------------------------------------------");
        } else {
            for(Post post : posts) {
                String postStr = "\n"+ post.getId() + ". " + post.getPostOwner().getName() + ": " +
                        post.getPost();
                appendLine(postStringBuilder, postStr);
                appendLine(postStringBuilder, "Comments:");

                Set<Integer> comment_ids = post.getComments();
                if(comment_ids.isEmpty()) {
                    appendTab(postStringBuilder, "No Comments yet.");
                } else {
                    List<Comment> comments = CommentController.getAllCommentsFromASetOfIDs(comment_ids);

                    for(Comment comment : comments) {
                        if(comment != null)
                            appendTab(postStringBuilder, "- " + comment.getCommentOwner().getName() + ": " + comment.getComment());
                    }
                }
            }

            println("--------------------POSTS--------------------");

            println(postStringBuilder.toString());

            println("---------------------------------------------");

            printCommentPrompt(user);
        }
    }

    static void handleMakeAComment(User user, String commentData) {
        String[] data = commentData.split("::");

        // find Post By ID
        Post post = PostController.findPostById(Integer.parseInt(data[0]));

        CommentController newComment = new CommentController(post, user, data[1]);
        Comment newlyAddedComment = newComment.save();

        // link comment id to the post
        PostController.addComment(post, newlyAddedComment);
    }

    static void handleLogout() {
        goToMain();
    }

    static void goHome(User user) {
        boolean isRunning = true;
        while (isRunning) {
            printHomePrompt(user.getName());
            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1 -> handleCreateNewPost(user);
                case 2 -> handleShowAllPosts(user);
                case 3 -> {
                    isRunning = false;
                    handleLogout();
                }
                default -> println("Please enter a valid option");
            }
        }
    }

    static void goToMain() {
        while (true) {
            printFirstPrompt();
            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1 -> handleRegister();
                case 2 -> handleLogin();
                case 3 -> System.exit(0);
                default -> println("Please enter a valid option");
            }
        }
    }


    // UTILITIES
    static void printFirstPrompt() {
        println("""
                -MAIN MENU-
                Press 1 to Register
                Press 2 to Login
                Press 3 to Quit.""");
    }

    static void printHomePrompt(String userName) {
        println("\n-HOME-\n" +
                "Hello, " + userName + "!\n" +
                "Press 1 to create a new Post\n" +
                "Press 2 to view all Posts\n" +
                "Press 3 to logout.");
    }

    static void printCommentPrompt(User user) {
        println("To comment, kindly input the post number(written before the post) and your comment both seperated by (::). Eg: 1::Ha, glad you're loving it!");
        println("Or kindly press 0 to go back home");

        String input = scanner.nextLine();
        switch (input) {
            case "0" -> goHome(user);
            default -> handleMakeAComment(user, input);
        }
    }

    static <T> void println(T value) {
        System.out.println(value);
    }

    static StringBuilder appendLine(StringBuilder builder, String line) {
        return builder.append(line).append("\n");
    }

    static StringBuilder appendTab(StringBuilder builder, String line) {
        return builder.append("\t").append(line).append("\n");
    }
}
