package com.hmct.refrigeratorpadpromusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hmct.refrigeratorpadpromusic.MusicMainActivity;
import com.hmct.refrigeratorpadpromusic.R;
import com.hmct.refrigeratorpadpromusic.units.MusicEventMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

public class MusicPlayer extends Activity {
    public WebView webView;
    private String songname,songlistname;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity_player);

        Intent intent = getIntent();
        songname = intent.getStringExtra("SONGNAME");
        songlistname  = intent.getStringExtra("SONGLISTNAME");
//        Log.e("songname",songname);


        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//打开js支持0
        webSettings.setMediaPlaybackRequiresUserGesture(false);

//自适应手机屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JsInteration(), "JsInteration");

        if (songlistname!=null){
            webView.loadUrl("file:///android_asset/ul.html");
        }
        if (songname!=null){
            webView.loadUrl("http://192.168.1.106:8080/?songid=0043dDKn0PRCUY&searchname="+songname);
        }




    }

    public void PlayerOnClick(View v){
        switch (v.getId()){



            case R.id.player_back:
                webView.removeAllViews();
                webView.destroy();
                Intent intent = new Intent(MusicPlayer.this, MusicMainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    public class JsInteration {

        @JavascriptInterface//一定要写，不然H5调不到这个方法
        public String back() {
            return songlistname;
        }
    }

}
