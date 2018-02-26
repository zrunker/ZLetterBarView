package cc.ibooker.letterbarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;

/**
 * 自定义右侧栏，#A~Z
 *
 * @author 邹峰立
 */
public class LetterBar extends View {
    /**
     * 对外提供触摸接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

    private OnTouchingLetterChangedListener mOnTouchingLetterChangedListener;

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
        this.mOnTouchingLetterChangedListener = listener;
    }

    private String[] mLetters = null;
    private Paint mPaint;

    private int mChoose = -1;

    // 标记到两侧距离
    private int bothSidesMargin = 16;

    private final float mDensity;
    private float mY;
    private float mHalfWidth, mHalfHeight;
    private float mLetterHeight;
    private float mAnimStep;

    private int mTouchSlop;
    private float mInitialDownY;
    private boolean mIsBeingDragged, mStartEndAnim;
    private int mActivePointerId = INVALID_POINTER;

    private RectF mIsDownRect = new RectF();

    // 构造方法
    public LetterBar(Context context) {
        this(context, null);
    }

    public LetterBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        // 设置字体颜色
        this.mPaint.setColor(Color.GRAY);
        // 设置显示文字#A-Z
        this.mLetters = context.getResources().getStringArray(R.array.letter_list);
        // 其他值设置
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mDensity = getContext().getResources().getDisplayMetrics().density;
        // 设置内间距
        setPadding(0, dip2px(8), 0, dip2px(16));
    }

    /**
     * 设置字体颜色
     *
     * @param textColor 字体颜色int - 默认灰色
     */
    public LetterBar setTextColor(int textColor) {
        this.mPaint.setColor(textColor);
        return this;
    }

    /**
     * 设置到两侧距离 - dp
     *
     * @param bothSidesMargin 待设值 默认16
     * @return LetterBar
     */
    public LetterBar setbothSidesMargin(int bothSidesMargin) {
        this.bothSidesMargin = bothSidesMargin;
        return this;
    }

    /**
     * 获取侧边栏数据
     *
     * @return 侧边栏数据
     */
    public String[] getLetters() {
        return mLetters;
    }

    /**
     * 设置侧边栏数据 - 默认#A~Z
     *
     * @param letters 待设置数据
     * @return LetterBar
     */
    public LetterBar setLetters(String[] letters) {
        this.mLetters = letters;
        return this;
    }

    // 获取侧边栏mLetters的长度
    private int getLettersSize() {
        return mLetters.length;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == -1 || !mIsDownRect.contains(ev.getX(), ev.getY())) {
                    return false;
                }
                mInitialDownY = initialDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = Math.abs(y - mInitialDownY);
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                if (mIsBeingDragged) {
                    mY = y;
                    final float moveY = y - getPaddingTop() - mLetterHeight / 1.64f;
                    final int characterIndex = (int) (moveY / mHalfHeight * mLetters.length);
                    if (mChoose != characterIndex) {
                        if (characterIndex >= 0 && characterIndex < mLetters.length) {
                            mChoose = characterIndex;
                            // 滑动时执行
                            if (mOnTouchingLetterChangedListener != null)
                                mOnTouchingLetterChangedListener.onTouchingLetterChanged(mLetters[characterIndex]);
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mOnTouchingLetterChangedListener != null) {
                    if (mIsBeingDragged) {
                        mOnTouchingLetterChangedListener.onTouchingLetterChanged(mLetters[mChoose]);
                    } else {
                        float downY = ev.getY() - getPaddingTop();
                        final int characterIndex = (int) (downY / mHalfHeight * mLetters.length);
                        if (characterIndex >= 0 && characterIndex < mLetters.length) {
                            mOnTouchingLetterChangedListener.onTouchingLetterChanged(mLetters[characterIndex]);
                        }
                    }
                }
                mStartEndAnim = mIsBeingDragged;
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;

                mChoose = -1;
                mAnimStep = 0f;
                invalidate();

                performClick();
                return false;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHalfWidth = w - dip2px(bothSidesMargin);
        mHalfHeight = h - getPaddingTop() - getPaddingBottom();

        float lettersLen = getLettersSize();

        mLetterHeight = mHalfHeight / lettersLen;
        int textSize = (int) (mHalfHeight * 0.75f / lettersLen);
        this.mPaint.setTextSize(textSize);

        mIsDownRect.set(w - dip2px(bothSidesMargin * 2), 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < getLettersSize(); i++) {
            float letterPosY = mLetterHeight * (i + 1) + getPaddingTop();
            float diff, diffY, diffX;
            if (mChoose == i && i != 0 && i != getLettersSize() - 1) {
                diffX = 0f;
                diffY = 0f;
                diff = 2.16f;
            } else {
                float maxPos = Math.abs((mY - letterPosY) / mHalfHeight * 7f);
                diff = Math.max(1f, 2.2f - maxPos);
                if (mStartEndAnim && diff != 1f) {
                    diff -= mAnimStep;
                    if (diff <= 1f) {
                        diff = 1f;
                    }
                } else if (!mIsBeingDragged) {
                    diff = 1f;
                }
                diffY = maxPos * 50f * (letterPosY >= mY ? -1 : 1);
                diffX = maxPos * 100f;
            }
            canvas.save();
            canvas.scale(diff, diff, mHalfWidth * 1.20f + diffX, letterPosY + diffY);
            if (diff == 1f) {
                this.mPaint.setAlpha(255);
                this.mPaint.setTypeface(Typeface.DEFAULT);
            } else {
                int alpha = (int) (255 * (1 - Math.min(0.9, diff - 1)));
                if (mChoose == i)
                    alpha = 255;
                this.mPaint.setAlpha(alpha);
                this.mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            }
            canvas.drawText(mLetters[i], mHalfWidth, letterPosY, this.mPaint);
            canvas.restore();
        }
        if (mChoose == -1 && mStartEndAnim && mAnimStep <= 0.6f) {
            mAnimStep += 0.6f;
            postInvalidateDelayed(25);
        } else {
            mAnimStep = 0f;
            mStartEndAnim = false;
        }
    }

    private int dip2px(int dipValue) {
        return (int) (dipValue * mDensity + 0.5f);
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = ev.findPointerIndex(activePointerId);
        if (index < 0) {
            return -1;
        }
        return ev.getY(index);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }
}
