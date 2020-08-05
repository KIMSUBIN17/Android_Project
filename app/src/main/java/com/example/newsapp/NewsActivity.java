package com.example.newsapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] mDataset = {"1","2"};
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        queue = Volley.newRequestQueue(this);
        getNews();
        //1. 화면이 로딩 -> 뉴스 정보 받아온다
        //2. 받아온 정보 -> 리사이클러뷰의 어댑터로 넘겨준다.
        //3. 어댑터는 받아온 정보를 셋팅, 화면에 표시한다.
    }

    public void getNews(){

        // Instantiate the RequestQueue.
        //volley에게 이 주소로 접속하라고 알려줌
        String url ="http://newsapi.org/v2/top-headlines?country=kr&apiKey=b37e979437f74dd093bdce196385fdc8";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("NEWS", response);

                        try {

                            JSONObject jsonObj = new JSONObject(response);

                            JSONArray arrayArticles = jsonObj.getJSONArray("articles");

                            //response ->> NewsData Class 분류
                            //(string 원문 잘 분류, 분리하여 NewsData 클래스에 담기)
                            List<NewsData> news = new ArrayList<>();

                            for(int i = 0, j = arrayArticles.length(); i<j; i++) {
                                JSONObject obj = arrayArticles.getJSONObject(i);  //articles안에 한개의 내용 들어옴

                                Log.d("NEWS", obj.toString());  //데이터가 건별로 들어오는지 확인

                                NewsData newsData = new NewsData();
                                newsData.setTitle(obj.getString("title"));
                                newsData.setUrlToImage(obj.getString("urlToImage"));
                                newsData.setContent(obj.getString("content"));
                                news.add(newsData);
                            }



                            // specify an adapter (see also next example)
                            // 정상적으로 정보를 받아온 경우에 어댑터로 정보를 넘기도록 수정한다.
                            mAdapter = new MyAdapter(news,NewsActivity.this);
                            recyclerView.setAdapter(mAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
