package tv.afriquemedia.afmfeed.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Article {

    public Source getSource() {
        return Source;
    }

    public void setSource(Source source) {
        Source = source;
    }

    @SerializedName("source")
    @Expose
    private Source Source;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("urlToImage")
    @Expose
    private String UrlToImage;

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return UrlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        UrlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
