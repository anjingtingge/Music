package com.hmct.refrigeratorpadpromusic;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hmct.refrigeratorpadpromusic.activity.MusicDetailActivity;
import com.hmct.refrigeratorpadpromusic.activity.MusicPlayer;
import com.hmct.refrigeratorpadpromusic.recyview.MusicCommonAdapter;
import com.hmct.refrigeratorpadpromusic.recyview.MusicViewHolder;
import com.hmct.refrigeratorpadpromusic.sql.MusicMySQLiteOpenHelper;
import com.hmct.refrigeratorpadpromusic.sql.MusicPersonModel;
import com.hmct.refrigeratorpadpromusic.units.MusicEventMessage;
import com.hmct.refrigeratorpadpromusic.units.MusicFloatWindowService;
import com.hmct.refrigeratorpadpromusic.units.MusicServiceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicMainActivity extends AppCompatActivity  {

    private List<MusicPersonModel> mList;
    private MusicMySQLiteOpenHelper dbHelperlist;
    private SQLiteDatabase sqliteDatabase,sqliteDatabaselist;
    private ContentValues valueslist;

    private RecyclerView mRecyclerView;
    public MusicCommonAdapter musicCommonAdapter;

    private PopupWindow popWin = null; // 弹出窗口
    private View popView = null; // 保存弹出窗口布局

    private RecyclerView popmRecycl;
    public MusicCommonAdapter popCommonAdapter;
    private List<String> mMenus;
    private String[]  language,school,theme,mood,scenes;
    private TextView text_language,text_school,text_theme,text_mood,text_scenes;
    private TextView[] textmenus;
    private TextView text_childmenu;
    private View view_divide;
    private EditText searchview;
    private Intent window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity_main);
        initId();
        initData();
        ObtainData();
        initRecycler();
        initPopview();
        initPopRecycler();


        if (!MusicServiceUtils.isServiceRunning(MusicMainActivity.this,"MusicFloatWindowService")){
            window = new Intent(MusicMainActivity.this,MusicFloatWindowService.class);
            startService(window);
            Log.e("服务没开启","dddddd");
        }

    }
    private void initId(){
        text_childmenu = findViewById(R.id.chilmenu);
        view_divide = findViewById(R.id.view_divide);
        searchview = findViewById(R.id.searchview);
    }
    private void initData(){

        language= new String[]{"国语","英语","韩语","粤语","日语","小语种","闽南语","法语","拉丁语"};
        school= new String[]{ "流行","轻音乐", "摇滚", "民谣","R&B","嘻哈", "电子", "古典","乡村", "蓝调","爵士", "新世纪","拉丁", "后摇","中国传统", "世界音乐"};
        theme= new String[]{"儿歌","ACG","经典","网络歌曲","影视","中国风","古风","情歌","城市","现场音乐","背景音乐","佛教音乐","UP主","乐器","DJ"};
        mood= new String[]{"伤感","安静","快乐","治愈","励志","甜蜜","寂寞","宣泄","思念"};
        scenes= new String[]{"睡前","夜店","学习","运动","开车","约会","工作","旅行","派对","婚礼","咖啡馆","跳舞","校园"};
    }
    private void initPopRecycler(){
        popmRecycl = (RecyclerView)popView.findViewById(R.id.rcvmenu);
        mMenus = new ArrayList<>();
        mMenus.addAll(Arrays.asList(language));

        popCommonAdapter =new MusicCommonAdapter<String>(this,R.layout.music_item_menu, mMenus,2)
        {
            @Override
            public void convert(MusicViewHolder holder, String s)
            {
                TextView tv = holder.getView(R.id.tex_menu);
                tv.setText(s);


            }
        };

        GridLayoutManager layoutManager = new GridLayoutManager(this,6);


        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        popmRecycl.setLayoutManager(layoutManager);
        popmRecycl.setAdapter(popCommonAdapter);

        popCommonAdapter.setOnItemClickListener(new MusicCommonAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position)
            {


                popCommonAdapter.setThisPosition(position);
                popCommonAdapter.notifyDataSetChanged();

                String aa=mMenus.get(position);
                Toast.makeText(MusicMainActivity.this, aa, Toast.LENGTH_SHORT).show();

                mList.clear();


                mList.addAll(dbHelperlist.rawQueryPersonData(aa));
                musicCommonAdapter.notifyDataSetChanged();

                text_childmenu.setVisibility(View.VISIBLE);
                view_divide.setVisibility(View.VISIBLE);
                text_childmenu.setText(aa);


                try
                {
                    Thread.currentThread().sleep(500);//毫秒
                }
                catch(Exception e){}

                popWin.dismiss();

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position)
            {
                return false;
            }
        });

    }
    private void initPopview(){




        popView=LayoutInflater.from(MusicMainActivity.this).inflate(R.layout.music_activity_popwin, null);

        popWin = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true); // 实例化PopupWindow
        popWin.setFocusable(true);

        popWin = new PopupWindow(popView, 1080, 960);

        text_language = popView.findViewById(R.id.text_language);
        text_school = popView.findViewById(R.id.text_school);
        text_theme = popView.findViewById(R.id.text_theme);
        text_mood = popView.findViewById(R.id.text_mood);
        text_scenes = popView.findViewById(R.id.text_scenes);
       textmenus = new TextView[]{text_language, text_school, text_theme, text_mood, text_scenes};


    }
    private void initRecycler(){

        mRecyclerView = (RecyclerView)findViewById(R.id.rcv1);

        musicCommonAdapter =new MusicCommonAdapter<MusicPersonModel>(this,R.layout.music_item_step, mList,0)
        {
            @Override
            public void convert(MusicViewHolder holder, MusicPersonModel s)
            {
                TextView tv = holder.getView(R.id.tex_title);
                ImageView imageView = holder.getView(R.id.img_title);
                tv.setText(s.getName());

                Glide.with(MusicMainActivity.this).load(s.getImgurl()).into(imageView);
            }
        };

        GridLayoutManager layoutManager = new GridLayoutManager(this,4);


        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(musicCommonAdapter);

        musicCommonAdapter.setOnItemClickListener(new MusicCommonAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position)
            {
                String aa=mList.get(position).getName();
                Toast.makeText(MusicMainActivity.this, aa, Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(MusicMainActivity.this, MusicDetailActivity.class);
                intent.putExtra("VIDEONAME",aa);
                startActivity(intent);


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position)
            {
                return false;
            }
        });

    }

    private void ObtainData(){

        dbHelperlist = new MusicMySQLiteOpenHelper(this,"music");
        sqliteDatabaselist = dbHelperlist.getWritableDatabase();

        mList = new ArrayList<>();
        mList.clear();

        mList.addAll(dbHelperlist.queryAllPersonData("songlist",null));






    }


    public void MainOnClick(View v){
        switch (v.getId()){

            case R.id.search_back:

                EventBus.getDefault().post(new MusicEventMessage<String>(6,""));
                finish();
                break;
            case R.id.lly_kind:
                popWin.setOutsideTouchable(true);//设置点击外面可以消失~注意则必须要设置该popupWindow背景才有效
                popWin.setBackgroundDrawable(new BitmapDrawable());
                popWin.showAtLocation(v, Gravity.CENTER, 0, 0);
                textmenus[0].setSelected(true);
                break;
            case R.id.text_language:
                mMenus.clear();
                mMenus.addAll(Arrays.asList(language));
                ChangeColor(0);
                RefreshAterMenu();
                break;
            case R.id.text_school:
                mMenus.clear();
                mMenus.addAll(Arrays.asList(school));
                ChangeColor(1);
                RefreshAterMenu();
                break;
            case R.id.text_theme:
                mMenus.clear();
                mMenus.addAll(Arrays.asList(theme));
                ChangeColor(2);
                RefreshAterMenu();
                break;

            case R.id.text_mood:
                mMenus.clear();
                mMenus.addAll(Arrays.asList(mood));
                ChangeColor(3);
                RefreshAterMenu();
                break;

            case R.id.text_scenes:
                mMenus.clear();
                mMenus.addAll(Arrays.asList(scenes));
                ChangeColor(4);
                RefreshAterMenu();
                break;

            case R.id.img_search:
                String songname =searchview.getText().toString();
                if (songname.length()!=0){
                EventBus.getDefault().post(new MusicEventMessage<String>(6,""));
                Intent intent = new Intent(MusicMainActivity.this, MusicPlayer.class);
                intent.putExtra("SONGLISTNAME",songname);
                startActivity(intent);
                finish();
                }else {
                    Toast.makeText(MusicMainActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void RefreshAterMenu(){
        popCommonAdapter.mDatas=mMenus;
        popCommonAdapter.setThisPosition(0);
        popCommonAdapter.notifyDataSetChanged();

    }

    private void ChangeColor(int nub){

        for (int i=0;i<textmenus.length;i++){

            if (i==nub){
                textmenus[i].setSelected(true);
            }else {
                textmenus[i].setSelected(false);
            }
        }
    }
}
