package org.example.entities;
import org.example.repositories.CommentRepo;
import org.example.textprocessors.AnsiColors;

import java.util.ArrayList;
import java.util.HashMap;

public class Post implements Likeable {
    private static final CommentRepo commentRepo = CommentRepo.getInstance();
    public static ArrayList<Post> posts = new ArrayList<>();
    private int postID;
    String username;
    private String title;
    private String body;
    private int voteCount;
    public HashMap<Integer, Integer> votingUserID; //K = userID , V = -1/+1 -> downvote/upvote
    public ArrayList<Comment> commentList;

    public Post(String title, String body, String username) {
        this.title = title;
        this.body = body;
        this.voteCount = 0;
        this.username = username;
        this.commentList = new ArrayList<>();
        this.votingUserID = new HashMap<>();
    }

    @Override
    public void upvote() {
        voteCount++;
    }
    @Override
    public void downvote() {
        voteCount--;
    }
    @Override
    public int getVotes() {
        return voteCount;
    }

    public int getPostID() {
        return postID;
    }
    public String getUsername() {
        return username;
    }
    public int getCommentsCounter() { return commentList.size(); }
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public void setPostId(int dbPostID) {
        this.postID = dbPostID;
    }

    public void addComment(User parentUser, String commentText) {
        Comment comment = new Comment(this, parentUser, commentText);
        commentList.add(comment);
        commentRepo.save(comment);
    }

    // move in ViewPost
    public void printPostComments(int indentLevel) {
        String indent = "    ".repeat(indentLevel); // 4 spaces per level

        for (Comment comment : commentList) {
            System.out.println(indent + AnsiColors.toOrange("ID: " + comment.getCommentID() + " | USER: " + comment.getParentUser().getUsername()));
            System.out.println(AnsiColors.addReward(comment.getCommentText(), comment.getVotes()));
            System.out.print(indent + AnsiColors.toRed("UP ") + comment.getVoteCount() + AnsiColors.toBlue(" DOWN "));
            System.out.println("| " + comment.replyList.size() + " replies");
            System.out.println(indent + AnsiColors.LINE_SEPARATOR);

            for (CommentReply reply : comment.replyList) {
                printReply(reply, indentLevel + 1);
            }
        }
    }

    // move in ViewComments
    public void printReply(CommentReply reply, int indentLevel) {
        String indent = "    ".repeat(indentLevel);

        System.out.println(indent + AnsiColors.toOrange("ID " + reply.getCommentReplyID() + " | USER: " + reply.getParentUser().getUsername()));
        System.out.println(indent + AnsiColors.addReward(reply.getCommentReplyText(), reply.getVotes()));
        System.out.print(indent + AnsiColors.toRed("UP ") + reply.getVotes() + AnsiColors.toBlue(" DOWN "));
        System.out.println("| " + reply.commentReplies.size() + " replies");
        System.out.println(indent + AnsiColors.LINE_SEPARATOR);

        // daca o sa avem nested replies
        for (CommentReply nested : reply.getCommentReplies()) {
            printReply(nested, indentLevel + 1);
        }
    }
}

