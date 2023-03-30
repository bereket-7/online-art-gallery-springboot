package com.project.oag.entity;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DeviceMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String deviceDetails;
    private String location;
    private Date lastLoggedIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(Date lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceMetadata that = (DeviceMetadata) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getDeviceDetails(), that.getDeviceDetails()) &&
                Objects.equals(getLocation(), that.getLocation()) &&
                Objects.equals(getLastLoggedIn(), that.getLastLoggedIn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getDeviceDetails(), getLocation(), getLastLoggedIn());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("DeviceMetadata{");
        builder.append("id=").append(id);
        builder.append(", userId=").append(userId);
        builder.append(", deviceDetails='").append(deviceDetails).append('\'');
        builder.append(", location='").append(location).append('\'');
        builder.append(", lastLoggedIn=").append(lastLoggedIn);
        builder.append('}');
        return builder.toString();
    }
}