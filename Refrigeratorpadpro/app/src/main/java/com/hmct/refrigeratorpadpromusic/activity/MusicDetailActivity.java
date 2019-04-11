package com.hmct.refrigeratorpadpromusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.hmct.refrigeratorpadpromusic.R;
import com.hmct.refrigeratorpadpromusic.recyview.MusicCommonAdapter;
import com.hmct.refrigeratorpadpromusic.recyview.MusicViewHolder;
import com.hmct.refrigeratorpadpromusic.sql.MusicMySQLiteOpenHelper;
import com.hmct.refrigeratorpadpromusic.sql.MusicPersonModel;
import com.hmct.refrigeratorpadpromusic.units.MusicEventMessage;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MusicDetailActivity extends Activity {

    private WebView mWebView;
    private String videoname;
    private List<MusicPersonModel> mList,mDetail;
    private MusicMySQLiteOpenHelper dbHelperlist;
    private RecyclerView mRecyclerView;
    public MusicCommonAdapter musicCommonAdapter;
    private TextView tex_title,tex_pic,tex_director,tex_star,tex_des;
    private ImageView img_pic;

    private Intent window,player;

    private String musicurl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity_musicdetail);

        initId();
        initData();
        initRecycler();
//        initWebview();
        EventBus.getDefault().register(this);

    }
    private void initId(){
        tex_title = findViewById(R.id.tex_title);
        img_pic = findViewById(R.id.img_pic);
        tex_des = findViewById(R.id.tex_des);

    }
    private void initData(){

        dbHelperlist = new MusicMySQLiteOpenHelper(this,"music");
        Intent intent = getIntent();
        videoname = intent.getStringExtra("VIDEONAME");
        Log.e("videoname",videoname);

        mList = new ArrayList<>();
        mList.clear();
        mList.addAll(dbHelperlist.queryAllPersonData("songlist",videoname));

        mDetail = new ArrayList<>();
        mDetail.clear();
        mDetail.addAll(dbHelperlist.queryAllPersonData("listdetail",videoname));

        musicurl = mList.get(0).getImgurl();
        tex_title.setText(mList.get(0).getName());
        tex_des.setText(mList.get(0).getDes());
        Glide.with(MusicDetailActivity.this).load(musicurl).into(img_pic);


    }


    private void initRecycler(){

        mRecyclerView = (RecyclerView)findViewById(R.id.rcv1);
        musicCommonAdapter =new MusicCommonAdapter<MusicPersonModel>(this,R.layout.music_item_count, mDetail,1)
        {
            @Override
            public void convert(MusicViewHolder holder, MusicPersonModel s)
            {
                TextView count = holder.getView(R.id.tex_count);

                TextView tex_title = holder.getView(R.id.tex_title);
                TextView tex_singer = holder.getView(R.id.tex_singer);
                TextView tex_songtime = holder.getView(R.id.tex_songtime);



                count.setText(s.getNumber());
                tex_title.setText(s.getSongtitle());
                tex_singer.setText(s.getSinger());
                tex_songtime.setText(s.getSongtime());


            }
        };


        GridLayoutManager layoutManager = new GridLayoutManager(this,10);

        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(musicCommonAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
               this, DividerItemDecoration.HORIZONTAL));

        musicCommonAdapter.setOnItemClickListener(new MusicCommonAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position)
            {


                EventBus.getDefault().post(new MusicEventMessage<String>(4,mDetail.get(position).getSongid()));

                musicCommonAdapter.setThisPosition(position);
                musicCommonAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new MusicEventMessage<String>(3,musicurl));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position)
            {
                return false;
            }
        });

    }

    public void DetailOnClick(View v){
        switch (v.getId()){

            case R.id.detail_back:

                finish();
                break;


        }
    }




    /**
     * 接收消息
     * @param msg
     */
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventComing(MusicEventMessage<String> msg){

        if(msg.getType() == 5){

            mRecyclerView.performClick();

            int aaa = Integer.parseInt(msg.getData());
            musicCommonAdapter.setThisPosition(aaa);
            musicCommonAdapter.notifyDataSetChanged();

            Log.e("music_stop","wosh我收到");
    }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
