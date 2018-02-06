package com.example.milos.creitive;

import com.example.milos.creitive.models.Blog;
import com.example.milos.creitive.models.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Created by milos on 03/02/2018.
 */

public interface CreitiveAPI {


    @POST(ServerConfiguration.LOGIN)
    Call<Token> login(
            @HeaderMap Map<String, String> headers,
            @Body HashMap<String, String> requestBody
    );

    @GET(ServerConfiguration.BLOGS)
    Call<List<Blog>> getBlogs(
            @HeaderMap Map<String, String> headers
    );

}
