package com.example.counseling.model;

public class SystemInfo {
    private Long id;
    private String title;
    private String subtitle;
    private String content;
    private String picture1;
    private String picture2;
    private String picture3;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getPicture1() { return picture1; }
    public void setPicture1(String picture1) { this.picture1 = picture1; }
    public String getPicture2() { return picture2; }
    public void setPicture2(String picture2) { this.picture2 = picture2; }
    public String getPicture3() { return picture3; }
    public void setPicture3(String picture3) { this.picture3 = picture3; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
