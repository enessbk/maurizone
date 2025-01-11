package com.example.maurizone.models;

public class UserProfile {
    private String userId;
    private String name;
    private String email;
    private String language;
    private String profilePic = "https://firebasestorage.googleapis.com/v0/b/maurizone-59646.appspot.com/o/image%20profile.jpg?alt=media&token=81cb40cc-fce3-426c-89e4-93ef074e3766";

    // Default constructor required for calls to DataSnapshot.getValue(UserProfile.class)
    public UserProfile() {
    }
    public UserProfile(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public UserProfile(String userId, String name, String email, String language, String profilePic) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.language = language;
        this.profilePic = profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
