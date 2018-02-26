package cc.ibooker.letterbarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义索引栏
 *
 * @author 邹峰立
 */
public class LetterView extends View {
    private String[] mLetters = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    private int choose = -1;
    private Paint paint = new Paint();
    private boolean showBkg = false;

    /**
     * 设置侧边栏数据 - 默认#A~Z
     *
     * @param letters 待设置数据
     * @return LetterBar
     */
    public LetterView setLetters(String[] letters) {
        this.mLetters = letters;
        return this;
    }

    // 标记字体大小
    private float textSize = 14;

    // 标记字体颜色
    private int textColor = Color.parseColor("#8c8c8c");

    // 标记选中之后字体颜色
    private int selectTextColor = Color.parseColor("#3399ff");

    // 标记选中之后背景颜色
    private int bgColor = Color.parseColor("#40000000");

    public LetterView setTextSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public LetterView setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        return this;
    }

    public LetterView setSelectTextColor(@ColorInt int selectTextColor) {
        this.selectTextColor = selectTextColor;
        return this;
    }

    public LetterView setBgColor(@ColorInt int bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    // 构造方法
    public LetterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LetterView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(bgColor);
        }
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / mLetters.length;
        for (int i = 0; i < mLetters.length; i++) {
            paint.setColor(textColor);
            float size = dp2px(getContext(), textSize);
            if (size <= 0)
                size = 30;
            paint.setTextSize(size);
//            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(selectTextColor);
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(mLetters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(mLetters[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * mLetters.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < mLetters.length) {
                        listener.onTouchingLetterChanged(mLetters[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < mLetters.length) {
                        listener.onTouchingLetterChanged(mLetters[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private float dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    // 定义接口 - 监听索引栏触摸事件
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }
}
