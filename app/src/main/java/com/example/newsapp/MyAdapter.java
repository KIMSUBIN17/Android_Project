package com.example.newsapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<NewsData> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //ViewHolder : 메모리 관리와 성능 향상 위해 안드로이드에서 제공하는 클래스

        public TextView TextView_title;
        public TextView TextView_content;
        public SimpleDraweeView ImageView_title;
        public MyViewHolder(View v) {
            super(v);
            TextView_title = v.findViewById(R.id.TextView_title);
            TextView_content = v.findViewById(R.id.TextView_content);
            ImageView_title = v.findViewById(R.id.ImageView_title);
                    //activity에서는 바로 찾아갈땐 그 자체가 부모의 뷰이기때문에 바로 findview가 가능
                    //but, 부모 xml뷰가 누군지 알 수 없으므로 명확하게 row_news.xml이 부모라고 지정한 것
            //뷰홀더에 자식들 컴포넌트를 정의한 이유는? 한줄 한줄 보여주는 내용을 홀더를 통해 넘겨서 v에서 자식요소 사용)
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 각 줄마다 보여줄 값을 들고 있는 원본 데이터 mDataset 변수(초기 데이터가 string자료형, 다른 자료형으로도 변경가능_
    //값을 꺼내서 텍스트뷰. 이미지뷰에 각각 표시하게됨
    //액티비기ㅏ 원본 데이터를 어댑터로 넘겨주고 > 어댑터는 그걸로 작업
    public MyAdapter(List<NewsData> myDataset, Context context) {
        mDataset = myDataset;
        Fresco.initialize(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news, parent, false);
            //infalte:특정한 컴포넌트(여기서는 리사이클러뷰)의 특정 항목의 레이아웃을 바꾸는 inflate
        //row_news로 부모뷰 세팅하고 MyViewHolder로 넘겨서 반복하게만듦(자식 매칭)

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // ViewHolder가 반복되면서 onBind~함수에서 값을 셋팅
        // position에 해당하는 데이터를 가져오는 것
        // List에 순서대로 add하여 넣었기 때문에 get으로 하면 해당 순번의 뉴스 데이터를 꺼낼 수 있음
        NewsData news = mDataset.get(position);

        holder.TextView_title.setText(news.getTitle());

        String content = news.getContent();
        if(content != null && content.length() > 0) {
            holder.TextView_content.setText(content);
        }
        else{
            holder.TextView_content.setText("-");
        }

        //이미지 주소 통해 이미지 가져옴(fresco 사용)
        Uri uri = Uri.parse(news.getUrlToImage());

        holder.ImageView_title.setImageURI(uri);


    }

    // Return the size of your dataset (invoked by the layout manager)
    //ViewHolder가 원본 데이터의 크기(번지수 길이)만큼 반복됨
    @Override
    public int getItemCount() {

        return mDataset == null ? 0 : mDataset.size();
    }
}


//fresco 사용법
//1. init
//2. 전용뷰 xml
//3. 이미지 로딩딩