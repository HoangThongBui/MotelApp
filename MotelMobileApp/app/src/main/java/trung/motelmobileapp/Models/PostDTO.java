package trung.motelmobileapp.Models;

import java.io.Serializable;

public class PostDTO implements Serializable{
    private String id;
    private String title;
    private UserDTO user;
    private String address;
    private String city;
    private String district;
    private String ward;
    private int price;
    private String detail;
    private String request_date;

    public PostDTO(String id, String title, UserDTO user, String address, String city, String district, String ward, int price, String detail, String request_date) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.address = address;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.price = price;
        this.detail = detail;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
                ", user=" + user.getName() +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", price=" + price +
                ", detail='" + detail + '\'' +
                ", request_date='" + request_date + '\'' +
                '}';
    }
}
