package com.stenbergroom.reallivewallpaperabstractions.app;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class LiveWallpaperPlanet extends WallpaperService {

    static final String TAG = "myLogs";
    static final Handler h = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Engine onCreateEngine() {
        try{
            return new GifEngine();
        }catch (IOException e) {
            Log.d(TAG, "Error creating GifEngine", e);
            stopSelf();
            return null;
        }
    }

    class GifEngine extends Engine{

        private final Movie myGif;
        private final int gifDuration;
        private final Runnable runnableGif;
        private float scaleX;
        private float scaleY;
        private int timeGif;
        private long startGif;

        public GifEngine() throws IOException{
            InputStream is = getResources().openRawResource(R.raw.planet);
            if(is != null){
                try{
                    myGif = Movie.decodeStream(is);
                    gifDuration = myGif.duration();
                }finally {
                    is.close();
                }
            }else{
                throw new IOException("My Exception, obj is null");
            }

            timeGif = -1;
            runnableGif = new Runnable() {
                @Override
                public void run() {
                    startGif();
                }
            };
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            h.removeCallbacks(runnableGif);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if(visible){
                startGif();
            }else{
                h.removeCallbacks(runnableGif);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            scaleX = width / (1f * myGif.width());
            scaleY = height / (1f * myGif.height());
            startGif();
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            startGif();
        }

        public void startGif(){

            myTimer();

            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Canvas canvas = null;
            try{
                canvas = surfaceHolder.lockCanvas();
                if(canvas != null){
                    drawGif(canvas);
                }
            }finally {
                try {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error, please try again", Toast.LENGTH_SHORT).show();
                }
            }
            h.removeCallbacks(runnableGif);
            if(isVisible()){
                //*
                h.postDelayed(runnableGif, 5);
            }
        }

        public void myTimer(){
            if(timeGif == -1L){
                timeGif = 0;
                startGif = SystemClock.uptimeMillis();
            }else{
                long mDiff = SystemClock.uptimeMillis() - startGif;
                timeGif = (int) (mDiff % gifDuration);
            }
        }

        public void drawGif(Canvas canvas){
            canvas.save();
            canvas.scale(scaleX, scaleY);
            myGif.setTime(timeGif);
            myGif.draw(canvas, 0, 0);
            canvas.restore();
        }
    }

}
