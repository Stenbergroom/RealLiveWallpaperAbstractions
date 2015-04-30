package com.stenbergroom.reallivewallpaperabstractions.app;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity{

    private Button btnSetWallpaper;
    private Button btnMoreWallpaper;
    private Button btnShare;
    private Animation anim_btn;

    private final String MY_MARKET_PROFILE = "https://play.google.com/store/apps/developer?id=Stenberg+room";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSetWallpaper = (Button)findViewById(R.id.btnSetWallpaper);
        btnMoreWallpaper = (Button)findViewById(R.id.btnMoreWallpaper);
        btnShare = (Button)findViewById(R.id.btnShare);
    }

    public void onClickSetWallpaper(View view){
        anim_btn = AnimationUtils.loadAnimation(this, R.anim.anim_btn);
        btnSetWallpaper.startAnimation(anim_btn);
        startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER));
    }

    public void onClickMoreWallpaper(View view){
        anim_btn = AnimationUtils.loadAnimation(this, R.anim.anim_btn);
        btnMoreWallpaper.startAnimation(anim_btn);
        Toast.makeText(this, R.string.more_wallp, Toast.LENGTH_LONG).show();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MY_MARKET_PROFILE)));
    }

    public void onClickShare(View view){
        anim_btn = AnimationUtils.loadAnimation(this, R.anim.anim_btn);
        btnShare.startAnimation(anim_btn);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.best_lwp)+"\n"+MY_MARKET_PROFILE);
        shareIntent.setType("text/plane");
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
