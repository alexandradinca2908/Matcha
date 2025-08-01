package org.matcha.springbackend.mapper;

import org.matcha.springbackend.dto.post.PostDto;
import org.matcha.springbackend.entities.AccountEntity;
import org.matcha.springbackend.entities.PostEntity;
import org.matcha.springbackend.entities.SubredditEntity;
import org.matcha.springbackend.model.Account;
import org.matcha.springbackend.model.Post;
import org.matcha.springbackend.model.Subreddit;
import org.matcha.springbackend.model.Vote;
import org.matcha.springbackend.repository.AccountRepository;
import org.matcha.springbackend.repository.SubredditRepository;
import org.matcha.springbackend.service.VoteService;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final AccountRepository accountRepository;
    private final SubredditRepository subredditRepository;
    private final VoteService voteService;
    private final AccountMapper accountMapper;

    public PostMapper(AccountRepository accountRepository, SubredditRepository subredditRepository, VoteService voteService, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.subredditRepository = subredditRepository;
        this.voteService = voteService;
        this.accountMapper = accountMapper;
    }

    public PostEntity modelToEntity(Post post) {
        if (post == null) return null;

        PostEntity entity = new PostEntity();

        if (post.getPostID() != null) {
            entity.setPostID(post.getPostID());
        } else {
            entity.setPostID(null);
        }

        entity.setTitle(post.getTitle());
        entity.setContent(post.getContent());
        entity.setPhotoPath(post.getPhotoPath());
        entity.setDeleted(post.isDeleted());
        entity.setCreatedAt(post.getCreatedAt());
        entity.setUpdatedAt(post.getUpdatedAt());
        entity.setUpvotes(post.getUpvotes());
        entity.setDownvotes(post.getDownvotes());

        Integer score = 0;
        if (post.getUpvotes() != null && post.getDownvotes() != null) {
            score = post.getUpvotes() - post.getDownvotes();
        }
        entity.setScore(score);

        Integer commentCount = 0;
        if (post.getComments() != null) {
            commentCount = post.getComments().size();
        }
        entity.setCommentCount(commentCount);

        // Map Account
        if (post.getAccount() != null && post.getAccount().getAccountId() != null) {
            AccountEntity accountEntity = accountRepository.findById(post.getAccount().getAccountId()).orElse(null);
            entity.setAccount(accountEntity);
        }

        // Map Subreddit
        if (post.getSubreddit() != null && post.getSubreddit().getId() != null) {
            SubredditEntity subredditEntity = subredditRepository.findById(post.getSubreddit().getId()).orElse(null);
            entity.setSubreddit(subredditEntity);
        }

        return entity;
    }

    public PostDto modelToDto(Post model) {
        String id = model.getPostID().toString();
        String title = model.getTitle();
        String content = model.getContent();
        String author = model.getAccount().getUsername();
        String subreddit = model.getSubreddit().getDisplayName();
        // Treat null upvotes/downvotes as 0
        Integer upvotes = model.getUpvotes() != null ? model.getUpvotes() : 0;
        Integer downvotes = model.getDownvotes() != null ? model.getDownvotes() : 0;
        Integer score = upvotes - downvotes;
        Integer commentCount = (model.getComments() != null) ? model.getComments().size() : 0;

        // Only try to get userVote if account and postID are not null and account has an ID
        String userVote = null;
        if (model.getAccount() != null && model.getAccount().getAccountId() != null && model.getPostID() != null) {
            try {
                Vote vote = voteService.getVoteByAccountAndVotable(accountMapper.modelToEntity(model.getAccount()), model.getPostID());
                userVote = vote != null && vote.getVoteType() != null ? vote.getVoteType().toString() : null;
            } catch (Exception e) {
                userVote = null;
            }
        }

        String createdAt = model.getCreatedAt().toString();
        String updatedAt = model.getUpdatedAt().toString();

        return new PostDto(id, title, content, author, subreddit, upvotes, downvotes,
                score, commentCount, userVote, createdAt, updatedAt);
    }

    public Post entityToModel(PostEntity entity) {
        if (entity == null) return null;

        // Map Account
        Account account = new Account();
        if (entity.getAccount() != null) {
            account.setAccountId(entity.getAccount().getAccountId());
            account.setUsername(entity.getAccount().getUsername());
            account.setEmail(entity.getAccount().getEmail());
            account.setPhotoPath(entity.getAccount().getPhotoPath());
        }

        // Map Subreddit
        Subreddit subreddit = new Subreddit();
        if (entity.getSubreddit() != null) {
            subreddit.setSubredditId(entity.getSubreddit().getSubredditId());
            subreddit.setDisplayName(entity.getSubreddit().getName());
            subreddit.setDescription(entity.getSubreddit().getDescription());
        }

        // Map Post
        Post post = new Post();
        post.setPostID(entity.getPostID());
        post.setAccount(account);
        post.setSubreddit(subreddit);
        post.setTitle(entity.getTitle());
        post.setContent(entity.getContent());
        post.setPhotoPath(entity.getPhotoPath());
        post.setDeleted(entity.isDeleted());
        post.setCreatedAt(entity.getCreatedAt());
        post.setUpdatedAt(entity.getUpdatedAt());
        // Avoid NullPointerException for comments
        post.setComments(new java.util.ArrayList<>());
        return post;
    }

}
