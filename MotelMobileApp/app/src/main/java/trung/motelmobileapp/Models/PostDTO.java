package trung.motelmobileapp.Models;

import java.io.Serializable;

public class PostDTO implements Serializable{
    private String id;
    private String title;
    private UserDTO user;
    private RoomDTO room;
    private String request_date;

    public PostDTO(String id, String title, UserDTO user, RoomDTO room, String request_date) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.room = room;
        this.request_date = request_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", user=" + user +
                ", room=" + room +
                ", request_date='" + request_date + '\'' +
                '}';
    }
}
