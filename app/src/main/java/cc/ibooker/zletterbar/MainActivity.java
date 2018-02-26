package cc.ibooker.zletterbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

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
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                textView.setText("select " + s);
            }
        });
    }
}
