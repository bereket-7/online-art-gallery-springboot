package com.project.oag.app.repository;

/**
 * Projection for {@link com.project.oag.app.entity.User}
 */
public interface UserInfoByRole {
    String getFirstName();

    String getLastName();

    String getPhone();

    String getSex();

    String getImage();
}