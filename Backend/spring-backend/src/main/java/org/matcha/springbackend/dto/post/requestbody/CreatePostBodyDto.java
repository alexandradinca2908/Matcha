package org.matcha.springbackend.dto.post.requestbody;

public record CreatePostBodyDto(
        String title,
        String content,
        String author,
        String subreddit
) {
}
