package trung.motelmobileapp.Models;

import java.io.Serializable;

public class CommentDTO implements Serializable{
    private String id;
    private String postId;
    private UserDTO user;
    private String detail;
    private String commentTime;

    public CommentDTO(String id, String postId, UserDTO user, String detail, String commentTime) {
        this.id = id;
        this.postId = postId;
        this.user = user;
        this.detail = detail;
        this.commentTime = commentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id='" + id + '\'' +
                ", postId='" + postId + '\'' +
                ", user=" + user +
                ", detail='" + detail + '\'' +
                ", commentTime='" + commentTime + '\'' +
                '}';
    }
}
