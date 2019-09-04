package tv.afriquemedia.afmfeed.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tv.afriquemedia.afmfeed.models.News;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(

          @Query("country") String country,
          @Query("apikey")String apiKey

          );
    @GET ("everything")
    Call<News> getNewsSearch(

            @Query("q")String keyword,

            @Query("language")String language,

            @Query("sortBy") String sortBy,

            @Query("apikey") String apikey

    );


}
