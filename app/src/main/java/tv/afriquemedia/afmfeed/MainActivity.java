package tv.afriquemedia.afmfeed;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.afriquemedia.afmfeed.api.ApiClient;
import tv.afriquemedia.afmfeed.api.ApiInterface;
import tv.afriquemedia.afmfeed.models.Article;
import tv.afriquemedia.afmfeed.models.News;
import tv.afriquemedia.afmfeed.models.Utils;

public class  MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "3141b2e2ea844ff18b4d2c4a04b37462";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private  String TAG = MainActivity.class.getSimpleName();
    public Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorMessage, errorTitle;
    private Button btnRetry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);



     onLoadingSwipeRefresh("");

        errorLayout = findViewById(R.id.errorLayout);
        errorImage = findViewById(R.id.errorImage);
        errorMessage = findViewById(R.id.errorMessage);
        errorTitle = findViewById(R.id.errorTitle);
        btnRetry = findViewById(R.id.btnRetry);


    }

    public void LoadJson(final String keyword){

        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getApiClient()
                .create(ApiInterface.class);

        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call ;

        if(keyword.length()>0){
            call = apiInterface.getNewsSearch(keyword,language,"publishedAt", API_KEY);

        }else {

            call= apiInterface.getNews(country, API_KEY);
        }



        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {

                if (response.isSuccessful()&& response.body()
                        .getArticles() !=null){

                    if (!articles.isEmpty()){
                        articles.clear();
                    }
                    recyclerView.setHasFixedSize(true);
                    articles = response.body().getArticles();
                    Log.i(TAG,"th height is "+articles.size());
                    if(!articles.isEmpty()){
                        adapter = new Adapter(articles, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        initListener();
                        swipeRefreshLayout.setRefreshing(false);
                    }


                } else {
                    swipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()){
                        case 404:
                            errorCode="404 page inconnue";
                            break;

                        case 500:
                            errorCode = "500 erreur de serveur ";
                            break;
                            default: errorCode = "erreur inconnue ";
                            break;
                    }

                    showErrorMessage(
                            R.drawable.no_result,
                            "Aucun Resultat",
                            "Actualiser!\n" +
                                    errorCode );
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.no_result,
                        "Oops!!",
                        "Verifier votre connexion internet!\n" +
                               t.toString() );

            }
        });
    }
    private void initListener(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
                Article article = articles.get(position);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("source",article.getSource().getName());
                intent.putExtra("author",article.getAuthor());

                Pair<View, String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));

                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                MainActivity.this,
                                        pair);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    startActivity(intent, optionsCompat.toBundle());
                }else {
                    startActivity(intent);
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =(SearchView) menu.findItem(R.id.action_search).getActionView();

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Recherche...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>2){
                   onLoadingSwipeRefresh(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false,false);


        return true;
    }

    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );
    }
private void showErrorMessage (int imageView, String title, String message){

        if(errorLayout.getVisibility()==View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");

            }
        });


}

}
