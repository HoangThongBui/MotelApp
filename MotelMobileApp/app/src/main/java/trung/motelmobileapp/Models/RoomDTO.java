package trung.motelmobileapp.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomDTO implements Serializable {
    private String id;
    private String address;
    private String city;
    private String district;
    private String ward;
    private int price;
    private int area;
    private String description;
    private ArrayList<String> images;

    public RoomDTO(String address, String city, String district, String ward, int price, int area, String description, ArrayList<String> images ){
        this.address = address;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.price = price;
        this.area = area;
        this.description = description;
        this.images = images;
    }

    public RoomDTO(String address, String city, String district, String ward, ArrayList<String> images) {
        this.address = address;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.images = images;
    }

    public RoomDTO(String id, String address, String city, String district, String ward, int price, int area, String description, ArrayList<String> images) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.price = price;
        this.area = area;
        this.description = description;
        this.images = images;
    }

    public String getFullAddress(){
        return this.address + ", Phuong " + this.ward + ", Quan " + this.getDistrict() + ", " + this.city;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", description='" + description + '\'' +
                '}';
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
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

}
