package com.kireeti.gkpract.models;


import com.google.gson.annotations.SerializedName;

public class ImageFile
{
    private String fileName;
    private String nameToServer;
    private String filePath;
    private String comments;

    @SerializedName("Id")
    private String Id;

    @SerializedName("PhotoRefId")
    private String PhotoRefId;

    @SerializedName("PhotoType")
    private String PhotoType;

    @SerializedName("Path")
    private String Path;

    @SerializedName("PageType")
    private String PageType;

    @SerializedName("Comments")
    private String Comments;

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getNameToServer() {
        return nameToServer;
    }

    public void setNameToServer(String nameToServer) {
        this.nameToServer = nameToServer;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPhotoRefId() {
        return PhotoRefId;
    }

    public void setPhotoRefId(String photoRefId) {
        PhotoRefId = photoRefId;
    }

    public String getPhotoType() {
        return PhotoType;
    }

    public void setPhotoType(String photoType) {
        PhotoType = photoType;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getPageType() {
        return PageType;
    }

    public void setPageType(String pageType) {
        PageType = pageType;
    }
}
