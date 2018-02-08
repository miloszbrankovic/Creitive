package com.example.milos.creitive.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.milos.creitive.CreitiveAPI;
import com.example.milos.creitive.R;
import com.example.milos.creitive.ServerConfiguration;
import com.example.milos.creitive.dialogs.SimpleDialogBox;
import com.example.milos.creitive.receivers.InternetBroadcastReceiver;
import com.example.milos.creitive.utils.SharedPreferenceUtils;
import com.example.milos.creitive.adapters.MyBlogListViewAdapter;
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

    private RecyclerView mPostRecyclerView;
    private MyBlogListViewAdapter myBlogListViewAdapter;

    private String dialogBoxMessageText = "To load content you need to enable internet!";
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_list_activity);

        checkInternetConnection();
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
        Call<List<Blog>> call = creitiveAPI.getBlogs(headerMap);
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

                        blogsInListView(blogLists);
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

    /*
    method for populating list view
     */
    private void blogsInListView(ArrayList<Blog> blogLists) {

        mPostRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(BlogListActivity.this));
        mPostRecyclerView.setHasFixedSize(true);

        myBlogListViewAdapter = new MyBlogListViewAdapter(blogLists, BlogListActivity.this, getApplicationContext());
        mPostRecyclerView.setAdapter(myBlogListViewAdapter);
    }


    private void checkInternetConnection(){
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (InternetBroadcastReceiver.checkInternetConnection(context) == true){
                    loadBlogPosts();
                }else {
                    SimpleDialogBox.dialogBoxMeWarning(BlogListActivity.this, dialogBoxMessageText);
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }


}
