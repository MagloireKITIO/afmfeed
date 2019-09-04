package tv.afriquemedia.afmfeed.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {
    @SerializedName("status")
    @Expose
    private String statuts;


    @SerializedName("totalResult")
    @Expose
    private int totalResult;

    @SerializedName("articles")
    @Expose
    private List<Article> articles;

    public String getStatuts() {
        return statuts;
    }

    public void setStatuts(String statuts) {
        this.statuts = statuts;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
