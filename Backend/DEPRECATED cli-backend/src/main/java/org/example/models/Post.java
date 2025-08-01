package org.example.models;
import org.example.repositories.CommentRepo;
import org.example.repositories.ImageRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class Post extends Likeable {
    private static final CommentRepo commentRepo = CommentRepo.getInstance();
    private static final ImageRepo imageRepo = ImageRepo.getInstance();
    private static int postCounter = 1;
    private int postID;
    private String username;
    private String title;
    private String body;
    private String imageURL;
    private int voteCount;
    private ArrayList<Comment> commentList;
    private HashMap<String, Integer> votingUserID; //K = userID , V = -1/+1 -> downvote/upvote


    public Post(String title, String body, String username) {
        this.title = title;
        this.body = body;
        this.imageURL = "No image";
        this.voteCount = 0;
        this.username = username;
        this.postID = postCounter++;
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
    public void setUsername(String username) {
        this.username = username;
    }
    public int getCommentsCounter() { return commentList.size(); }
    public ArrayList<Comment> getCommentList() { return commentList; }
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public HashMap<String, Integer> getVotingUserID() {
        return votingUserID;
    }
    public void setPostId(int dbPostID) {
        this.postID = dbPostID;
    }
}

