package com.example.milos.creitive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.milos.creitive.models.Blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogListActivity extends AppCompatActivity {

    private static final String TAG = BlogListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_list_activity);

        loadBlogPosts();
    }

    public void loadBlogPosts(){
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Host", ServerConfiguration.HOST);
        headerMap.put("Accept", ServerConfiguration.ACCEPT);
        headerMap.put("X-Authorize", SharedPreferenceUtils.getStringValue(BlogListActivity.this, "testVariable", "no token"));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerConfiguration.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CreitiveAPI creitiveAPI = retrofit.create(CreitiveAPI.class);
        Call<List<Blog>> call = creitiveAPI.getBlog(headerMap);
        final ArrayList<Blog> blogLists = new ArrayList<Blog>();
        call.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                Log.e(TAG, "onResponse" + response.body().toString());

                if (response.isSuccessful()){

                    blogLists.clear();
                    blogLists.addAll(response.body());

                    if (!blogLists.isEmpty()){
                        Log.e(TAG, "onResponse: blogLists != empty");

                        for (int i = 0; i <blogLists.size(); i++){
                            Log.e(TAG, "onResponse: id: " + blogLists.get(i).getId()
                                    + " \ntitle: " + blogLists.get(i).getTitle()
                                    + " \nimage_url: " + blogLists.get(i).getImage_url()
                                    + " \ndescription: " + blogLists.get(i).getDescription()
                                    + "\n----------------------------------------------------------" );
                        }
                    }

                } else {
                    Log.e(TAG, "onResponse: BAD_REQUEST ");
                }

            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                Log.e(TAG, "onFailure" + t.toString());
            }
        });
    }
}
