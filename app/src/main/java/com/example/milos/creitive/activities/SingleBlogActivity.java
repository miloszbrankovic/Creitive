package com.example.milos.creitive.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milos.creitive.CreitiveAPI;
import com.example.milos.creitive.R;
import com.example.milos.creitive.ServerConfiguration;

import com.example.milos.creitive.utils.SharedPreferenceUtils;
import com.example.milos.creitive.models.Content;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by milos on 06/02/2018.
 */

public class SingleBlogActivity extends AppCompatActivity {

    public static final String TAG = SingleBlogActivity.class.getSimpleName();

    long url_id;
    String htmlContent;

    TextView mTextViewPublishedDate;
    TextView mTextViewTitle;
    TextView mTextViewAuthor;
    TextView mTextViewBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_blog_activity);

        mTextViewPublishedDate = (TextView) findViewById(R.id.textViewPublishedDate);
        mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        mTextViewAuthor = (TextView) findViewById(R.id.textViewAuthor);
        mTextViewBody = (TextView) findViewById(R.id.textViewSingleBody);

        mTextViewPublishedDate.setText("");
        mTextViewTitle.setText("");
        mTextViewAuthor.setText("");
        mTextViewBody.setText("");

        url_id = SharedPreferenceUtils.getLongValue(SingleBlogActivity.this, "url_id", -1);
        //Log.e(TAG, "id number is:  " + url_id);
        if (url_id != -1) loadBlogContent(url_id);
    }

    private void loadBlogContent(long id) {
        Log.e("TAG- ---- ", "loadBlogContent() -> you clicked on item with id: " + id);
        //Toast.makeText(SingleBlogActivity.this, "loadBlogContent() -> you clicked on item with id: " + id, Toast.LENGTH_LONG).show();

        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Host", ServerConfiguration.HOST);
        headerMap.put("Accept", ServerConfiguration.ACCEPT);
        headerMap.put("X-Authorize", SharedPreferenceUtils.getStringValue(SingleBlogActivity.this, "testVariable", "no token"));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerConfiguration.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CreitiveAPI creitiveAPI = retrofit.create(CreitiveAPI.class);

        String url = ServerConfiguration.SERVER_URL + ServerConfiguration.BLOGS + id;
        //Log.e(TAG, "URL --- " + url);
        Call<Content> call = creitiveAPI.getBlog(headerMap, url);
        call.enqueue(new Callback<Content>() {
            @Override
            public void onResponse(Call<Content> call, Response<Content> response) {
                Content content = response.body();
                Log.e(TAG, "onResponse: content.getContent() ---- " + content.getContent());
                htmlContent = content.getContent().toString();
                setTextInFields();
            }

            @Override
            public void onFailure(Call<Content> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.toString());
            }
        });
    }

    private void setTextInFields() {
        String classPaternTitle = "h1.singleArticleBlog-title";
        String classPaternAuthor = "p.singleArticleBlog-author";
        String classPaternBody = "div.singleArticleBlog-description";
        String classPaternPublishedDate = "time.singleArticleBlog-publishedDate";

        Document doc = Jsoup.parse(htmlContent);

        Elements elementsPublishedDate = doc.select(classPaternPublishedDate);
        Elements elementsTitle = doc.select(classPaternTitle);
        Elements elementsAuthor = doc.select(classPaternAuthor);
        //Elements elementsBody = doc.select(classPaternBody);
        String elementsBody = String.valueOf(doc.select(classPaternBody));

        mTextViewPublishedDate.setText(elementsPublishedDate.text());
        mTextViewTitle.setText(elementsTitle.text());
        mTextViewAuthor.setText(elementsAuthor.text());
        //mTextViewBody.setText(elementsBody.text());
        mTextViewBody.setText(escapingCharacters(elementsBody));
    }

    public static String escapingCharacters(String html) {
        if(html==null) return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        //document.select("br").append("\n");
        document.select("p").prepend("\n");
        String s = document.html();
        s = s.replace("&nbsp;", " ");
        return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
}
