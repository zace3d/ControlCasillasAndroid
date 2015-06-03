package mx.citydevs.denunciaelectoral.beans;

import java.io.Serializable;

/**
 * Created by zace3d on 5/31/15.
 */
public class Complaint implements Serializable {
    private String name;
    private String last_name;
    private int complaint_type;
    private String content;
    private String latitude;
    private String longitude;
    private String phone;
    private String uuid;
    private String ip;
    private String picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public int getComplaintType() {
        return complaint_type;
    }

    public void setComplaintType(int complaint_type) {
        this.complaint_type = complaint_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}