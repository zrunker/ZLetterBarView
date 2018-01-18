package cc.ibooker.zletterbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cc.ibooker.letterbarview.LetterBar;


public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        LetterBar letterBar = findViewById(R.id.letterBar);
        letterBar.setOnTouchingLetterChangedListener(new LetterBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 触摸选择事件监听
                textView.setText("select " + s);
            }
        });
        letterBar.setTextColor(Color.parseColor("#40aff2")) // 设置字体颜色
                .setLetters(this.getResources().getStringArray(cc.ibooker.letterbarview.R.array.letter_list)) // 设置数据
                .setbothSidesMargin(16); // 设置两侧间距
    }
}
