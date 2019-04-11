package com.hmct.refrigeratorpadpromusic.units;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hmct.refrigeratorpadpromusic.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MusicFloatWindowService extends Service {

    private static final String TAG = "MusicFloatWindowService";

    ConstraintLayout toucherLayout;
    WindowManager.LayoutParams params;
    WindowManager windowManager;
    ImageView img_bg,image_play;
    String imageButton1_url;
    String musicid;
    public boolean isPlaying=false;
    public WebView webView;
    public String positon = "0";

    private ObjectAnimator mCircleAnimator;



    int statusBarHeight = -1;


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG,"MusicFloatWindowService Created");

        EventBus.getDefault().register(this);



    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        createToucher();

        return super.onStartCommand(intent, flags, startId);
    }



    @SuppressLint("ClickableViewAccessibility")
    private void createToucher()
    {
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
        {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        params.x = 0;
        params.y = 40;

        //设置悬浮窗口长宽数据.
        params.width = 250;
        params.height = 100;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (ConstraintLayout) inflater.inflate(R.layout.music_item_float,null);
        //添加toucherlayout
        windowManager.addView(toucherLayout,params);


        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
//        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
//        if (resourceId > 0)
//        {
//            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//        }
//        Log.i(TAG,"状态栏高度为:" + statusBarHeight);

        //浮动窗口按钮.
        img_bg = toucherLayout.findViewById(R.id.img_bg);
        image_play =  toucherLayout.findViewById(R.id.imageButton_play);
//        Glide.with(this).load(imageButton1_url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(img_bg);
        Glide.with(this).load(R.drawable.music_hali).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(img_bg);


        mCircleAnimator = ObjectAnimator.ofFloat(img_bg, "rotation", 0.0f, 360.0f);
        mCircleAnimator.setDuration(12000);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.setRepeatCount(-1);
        mCircleAnimator.setRepeatMode(ObjectAnimator.RESTART);





        webView = toucherLayout.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//打开js支持0
        webSettings.setMediaPlaybackRequiresUserGesture(false);

//自适应手机屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());



        image_play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isPlaying){
                    webView.loadUrl("javascript:stop()");
                    image_play.setSelected(false);
                    mCircleAnimator.pause();
                    isPlaying = false;

                    EventBus.getDefault().post(new MusicEventMessage<String>(5,positon));


                }else {

                    webView.loadUrl("javascript:play()");
                    image_play.setSelected(true);
                    mCircleAnimator.resume();
                    EventBus.getDefault().post(new MusicEventMessage<String>(5,positon));

                    isPlaying = true;

                }

            }
        });



    }




    /**
     * 接收消息
     * @param msg
     */
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventComing(MusicEventMessage<String> msg){

        if(msg.getType() == 1){

            webView.loadUrl("javascript:stop()");

            image_play.setSelected(false);


            mCircleAnimator.pause();


            isPlaying=false;
            positon = msg.getData();
        }
        if(msg.getType() == 2){


            Toast.makeText(MusicFloatWindowService.this, musicid, Toast.LENGTH_SHORT).show();
            webView.loadUrl("http://192.168.1.106:8080/?songid="+musicid);


//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    webView.loadUrl("javascript:play()");
//
//                }
//            }, 1000);


            image_play.setSelected(true);

            mCircleAnimator.start();

            isPlaying=true;
            positon = msg.getData();
        }

        if (msg.getType() == 3){
            Glide.with(this).load(msg.getData()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(img_bg);
            Log.e("music_play","图片换");
        }

        if (msg.getType() == 4){
            musicid = msg.getData();

        }

        if (msg.getType() == 6){
            webView.removeAllViews();
                webView.destroy();
                stopSelf();
            windowManager.removeView(toucherLayout);
            Log.e("退出","退出");
        }
    }


    @Override
    public void onDestroy()
    {

        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
