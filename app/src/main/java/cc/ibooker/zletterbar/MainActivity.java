package cc.ibooker.zletterbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

import cc.ibooker.letterbarview.LetterView;


public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

//        LetterBar letterBar = findViewById(R.id.letterBar);
//        letterBar.setOnTouchingLetterChangedListener(new LetterBar.OnTouchingLetterChangedListener() {
//            @Override
//            public void onTouchingLetterChanged(String s) {
//                // 触摸选择事件监听
//                textView.setText("select " + s);
//            }
//        });
//        letterBar.setTextColor(Color.parseColor("#40aff2")) // 设置字体颜色
//                .setLetters(this.getResources().getStringArray(cc.ibooker.letterbarview.R.array.letter_list)) // 设置数据
//                .setbothSidesMargin(16); // 设置两侧间距


        LetterView letterView = findViewById(R.id.letterView);
        letterView.setBgColor(Color.parseColor("#80000000"))
                .setSelectTextColor(Color.parseColor("#FE7513"))
                .setTextColor(Color.parseColor("#555555"))
                .setTextSize(15)
                .setLetters(this.getResources().getStringArray(cc.ibooker.letterbarview.R.array.letter_list)); // 设置数据

        letterView.setOnTouchingLetterChangedListener(new LetterView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                textView.setText("select " + s);

                // 显示弹出提示框
                if (mReady) {
                    overlay.setText(s);
                    overlay.setVisibility(View.VISIBLE);
                    handler.removeCallbacks(overlayThread);
                    // 延迟一秒后执行，让overlay为不可见
                    handler.postDelayed(overlayThread, 1000);
                }
            }
        });

        // 初始化字母提示框
        initOverlay();
    }

    /**
     * 初始化汉语拼音首字母弹出提示框
     */
    private boolean mReady;// 表示overlay提示框是否初始化准备工作已经完成
    private WindowManager windowManager;
    private TextView overlay; // 对话框显示首字母的textView
    private OverlayThread overlayThread;// overlay线程
    private Handler handler;

    // 初始化提示框
    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.letter_overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        // 设置提示信息的显示位置
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(overlay, lp);

            // 初始化数据
            mReady = true;
            overlayThread = new OverlayThread();
            handler = new Handler();
        }
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (overlay != null) {
            windowManager.removeView(overlay);
        }
    }
}
