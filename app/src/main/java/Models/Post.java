package Models;

public class Post {
    String creatorId;
    String creatorName;
    String creationDate;
    String category;
    String mediaUrl;
    String description;

    public Post(){}

    public Post(String creatorId, String creatorName, String creationDate, String category) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.creationDate = creationDate;
        this.category = category;
    }


    public Post(String creatorId, String creatorName, String creationDate, String category, String media, String description) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.creationDate = creationDate;
        this.category = category;
        this.mediaUrl = media;
        this.description = description;
    }

    public Post(String creatorId, String creatorName, String creationDate, String category, String media) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.creationDate = creationDate;
        this.category = category;
        this.mediaUrl = media;
    }


    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMedia() {
        return mediaUrl;
    }

    public void setMedia(String media) {
        this.mediaUrl = media;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
