package org.example.userinterface;

import org.example.models.Post;
import org.example.models.User;
import org.example.api.PostApiClient;
import org.example.textprocessors.AnsiColors;
import org.example.textprocessors.ProfanityFilter;
import org.example.textprocessors.TextSymbols;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UIPost {
    private static UIPost instance;
    private static final ProfanityFilter profanityFilter = ProfanityFilter.getInstance();
    public static final String POST_COUNT_HEADER_FORMAT = "\t\t=== Showing a total of %d posts ===";
    public static final int MAX_BODY_PREVIEW = 20;
    public static final int MIN_TITLE_LENGTH = 10;
    public static final int MAX_TITLE_LENGTH = 40;
    private static final int POST_WIDTH = 50;
    public static final int MIN_REWARD_VOTES = 3;
    private static final String PROMPT_TITLE = AnsiColors.toGreen("Please enter title: ");
    private static final String PROMPT_DESCRIPTION = AnsiColors.toGreen("Please enter description: ");
    private static final String ERROR_TITLE_TOO_LONG =
            AnsiColors.toRed("Title must be at most %d characters long.");
    private static final String ERROR_TITLE_TOO_SHORT =
            AnsiColors.toRed("Title must be at least %d characters long.");
    private static final String ERROR_DESCRIPTION_EMPTY = AnsiColors.toRed("Description can not be empty!");
    private static final String POST_ADDED_SUCCESSFULLY = AnsiColors.toGreen("Post added successfully!");
    private static final String PLEASE_ENTER_POST_ID = AnsiColors.toGreen("Please enter a PostID: ");
    private static final String INVALID_INPUT = AnsiColors.toYellow("Invalid input. Please enter a valid number.");

    UIComment uiComment = UIComment.getInstance();

    public UIPost() {}

    public static UIPost getInstance() {
        if (instance == null) {
            instance = new UIPost();
        }
        return instance;
    }

    public static Map<String, String> getPostDetailsFromUser() {
        Scanner sc = new Scanner(System.in);
        Map<String, String> postData = new HashMap<>();

        System.out.println(PROMPT_TITLE);
        String title = sc.nextLine();
        try{
            title = profanityFilter.filter(title);
        } catch (FileNotFoundException e) {
            System.out.println("Config file could not be found.");
        }

        while (title.length() < UIPost.MIN_TITLE_LENGTH || title.length() > UIPost.MAX_TITLE_LENGTH) {
            System.out.printf((ERROR_TITLE_TOO_SHORT) + "%n", UIPost.MIN_TITLE_LENGTH);
            System.out.printf((ERROR_TITLE_TOO_LONG) + "%n", UIPost.MAX_TITLE_LENGTH);
            title = sc.nextLine();
        }
        postData.put("title", title);

        System.out.println(PROMPT_DESCRIPTION);
        String body = sc.nextLine();
        try{
            body = profanityFilter.filter(body);
        } catch (FileNotFoundException e) {
            System.out.println("Config file could not be found.");
        }
        while (body.isEmpty()) {
            System.out.println(ERROR_DESCRIPTION_EMPTY);
            body = sc.nextLine();
        }
        postData.put("body", body);

        return postData;
    }

    public void showFeed(User user) {
        String headerText = String.format(UIPost.POST_COUNT_HEADER_FORMAT, PostApiClient.posts.size());
        System.out.println(AnsiColors.toGreen(headerText));
        int postCount = PostApiClient.posts.size();
        for (int i = postCount - 1; i >= 0; i--) {
            Post iter = PostApiClient.posts.get(i);
            this.showPost(false, iter, user);
        }
    }

    public void showPost(boolean isExpanded, Post post, User user) {
        String username;
        if (user == null) {
            username = "";
        } else {
            username = user.getUsername();
        }
        printTopBorder();
        printHeader(post.getPostID(), post.getAuthorName());
        printSeparator();
        printTitleLine(post.getTitle(), post.getVotes());
        printSeparator();
        printContentLine(isExpanded, post.getContent());
        printEmptyContentLine();
        printSeparator();
        printFooter(post.getVotes(), post.getCommentsCounter(), post.getVotingUserID(), username);
        printBottomBorder(isExpanded);
        if (isExpanded) {
            uiComment.showAllCommentsAndReplies(post, user);
        }
    }

    private void printTopBorder() {
        System.out.print("╔");
        for (int i = 0; i < POST_WIDTH - 2; i++) {
            System.out.print("═");
        }
        System.out.println("╗");
    }

    private void printBottomBorder(boolean isExpanded) {
        System.out.print("╚");
        for (int i = 0; i < POST_WIDTH - 2; i++) {
            System.out.print("═");
        }

        if (isExpanded) {
            System.out.println("╝");
        } else {
            System.out.println("╝" + "\n");
        }
    }

    private void printSeparator() {
        System.out.print("╠");
        for (int i = 0; i < POST_WIDTH - 2; i++) {
            System.out.print("─");
        }
        System.out.println("╣");
    }

    private void printHeader(int postId, String user) {
        String postLabel = "ID: " + postId;
        String userLabel = "USER: " + user;
        int totalLength = postLabel.length() + userLabel.length();
        int padding = POST_WIDTH - 4 - totalLength;

        System.out.print("║ " + AnsiColors.toGreen(postLabel));
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        System.out.println(AnsiColors.toGreen(userLabel)+ " ║");
    }

    private void printEmptyContentLine() {
        System.out.print("║ ");
        for (int i = 0; i < POST_WIDTH - 3; i++) {
            System.out.print(" ");
        }
        System.out.println("║");
    }

    private void printTitleLine(String title, int score) {
        String finalTitle = TextSymbols.addReward(title, score);
        System.out.print("║ " + finalTitle);
        int padding = POST_WIDTH - 4 - finalTitle.replaceAll("\u001B\\[[;\\d]*m", "").length();
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        System.out.println(" ║");
    }

    private void printContentLine(boolean isExpanded, String content) {
        if (!isExpanded) {
            int contentLength;
            if (content.length() > UIPost.MAX_BODY_PREVIEW) {
                String substring = content.substring(0, UIPost.MAX_BODY_PREVIEW) + "...";
                contentLength = substring.length();
                System.out.print("║ " + substring);
            } else {
                contentLength = content.length();
                System.out.print("║ " + content);
            }
            int padding = POST_WIDTH - 4 - contentLength;
            for (int i = 0; i < padding; i++) {
                System.out.print(" ");
            }
            System.out.println(" ║");
        } else {
            if (content.length() < POST_WIDTH - 4) {
                System.out.print("║ " + content);
                int padding = POST_WIDTH - 4 - content.length();
                for (int i = 0; i < padding; i++) {
                    System.out.print(" ");
                }
                System.out.println(" ║");
            } else {
                int maxLineLength = POST_WIDTH - 4;
                for (int i = 0; i < content.length(); i += maxLineLength) {
                    System.out.print("║ ");
                    int endIndex = Math.min(i + maxLineLength, content.length());
                    String line = content.substring(i, endIndex);
                    System.out.print(line);

                    if (endIndex == content.length()) {
                        int padding = maxLineLength - line.length();
                        for (int j = 0; j < padding; j++) {
                            System.out.print(" ");
                        }
                    }
                    System.out.println(" ║");
                }
            }
        }
    }

    private void printFooter(int score, int comments, HashMap<String, Integer> votingUserID, String username) {
        String votes;
        if (votingUserID.containsKey(username)) {
            if (votingUserID.get(username) == 1) {
                votes = AnsiColors.toRed("UP " + score) + " DOWN";
            } else {
                votes = "UP " + AnsiColors.toBlue(score + " DOWN");
            }
        } else {
            votes = "UP " + score + " DOWN";
        }
        String commentsStr = comments + " comment(s)";
        int totalLength = votes.replaceAll("\u001B\\[[;\\d]*m", "").length()
                + commentsStr.length();
        int padding = POST_WIDTH - 4 - totalLength;

        System.out.print("║ " + votes);
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        System.out.println(commentsStr + " ║");
    }

    public void postAddedSuccessfully() {
        System.out.println(POST_ADDED_SUCCESSFULLY);
    }

    public static void pleaseEnterPostId() {
        System.out.println(PLEASE_ENTER_POST_ID);
    }

    public static void invalidInput() {
        System.out.println(INVALID_INPUT);
    }
}
