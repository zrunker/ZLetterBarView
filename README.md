# ZLetterBar
按照字母#A~Z自定义侧边栏控件，实现列表索引，同时具有字母弹出效果。

>作者：邹峰立，微博：zrunker，邮箱：zrunker@yahoo.com，微信公众号：书客创作，个人平台：[www.ibooker.cc](http://www.ibooker.cc)。

>本文选自[书客创作](http://www.ibooker.cc)平台第136篇文章。[阅读原文](http://www.ibooker.cc/article/136/detail) 。

![书客创作](http://upload-images.jianshu.io/upload_images/3480018-5a4c7931e356027a..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

侧边栏，索引条是APP开发当中非常常见的功能之一，多用于索引列表，如城市选择，分类等。现在网上也提供了很多样式的索引条，这里我也提供两种索引条使用，项目已开源。

#### 引入资源

这里提供两种方式引入资源文件：

1、使用gradle
```
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	compile 'com.github.zrunker:ZLetterBar:v1.0.0'
}
```
2、使用maven
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.zrunker</groupId>
	<artifactId>ZLetterBar</artifactId>
	<version>v1.0.0</version>
</dependency>
```
引入资源之后，就可以使用该框架，该框架提供了两种索引栏。

#### 第一种LetterView

首先看看效果图：

![LetterView效果图](http://upload-images.jianshu.io/upload_images/3480018-e166a46719d0094f..gif?imageMogr2/auto-orient/strip)

要实现这样的效果，只需要三步：

**一、引入布局**

```
<cc.ibooker.letterbarview.LetterView
    android:id="@+id/letterView"
    android:layout_width="32dp"
    android:layout_height="match_parent"
    android:layout_marginBottom="10dp"
    android:layout_marginTop="10dp" />
```
**二、属性设置**

```
LetterView letterView = findViewById(R.id.letterView);
// 基本属性设置
letterView.setBgColor(Color.parseColor("#80000000"))// 设置背景颜色
        .setSelectTextColor(Color.parseColor("#FE7513"))// 设置选中之后字体颜色
        .setTextColor(Color.parseColor("#555555"))// 设置默认字体颜色
        .setTextSize(15)
        .setLetters(this.getResources().getStringArray(cc.ibooker.letterbarview.R.array.letter_list));// 设置数据

// 设置索引栏触摸监听
letterView.setOnTouchingLetterChangedListener(new LetterView.OnTouchingLetterChangedListener() {
    @Override
    public void onTouchingLetterChanged(String s) {
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
```
**三、弹出提示框设置**

在第二步中，已经给出了弹出提示框的使用代码，下面给出弹出提示框的初始化方法，如下：

```
/**
 * 初始化首字母弹出提示框
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

// 当界面关闭的时候将弹出提示框移除
@Override
public void finish() {
    super.finish();
    if (overlay != null) {
        windowManager.removeView(overlay);
    }
}
```
其中letter_overlay为弹窗布局文件，代码如下：
```
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:background="#a0000000"
    android:gravity="center"
    android:minHeight="85dp"
    android:minWidth="85dp"
    android:padding="10dp"
    android:textColor="#fff"
    android:textSize="55sp" />
```

#### 第二种LetterBar

首先看看效果图：

![LetterBar效果图](http://upload-images.jianshu.io/upload_images/3480018-6c8e0e35c78c686a..gif?imageMogr2/auto-orient/strip)

要实现这样的效果，只需要二步：

**一、引入布局**
```
<cc.ibooker.letterbarview.LetterBar
    android:id="@+id/letterBar"
    android:layout_width="wrap_content"
    android:layout_height="match_parent" />
```

**二、属性设置**

```
LetterBar letterBar = findViewById(R.id.letterBar);

// 设置索引栏触摸监听
letterBar.setOnTouchingLetterChangedListener(new LetterBar.OnTouchingLetterChangedListener() {
    @Override
    public void onTouchingLetterChanged(String s) {
        // 触摸选择事件监听
    }
});

// 基本属性设置
letterBar.setTextColor(Color.parseColor("#40aff2")) // 设置字体颜色
    .setLetters(this.getResources().getStringArray(cc.ibooker.letterbarview.R.array.letter_list)) // 设置数据
    .setbothSidesMargin(16); // 设置两侧间距
```

[Github地址](https://github.com/zrunker/ZLetterBarView)
[阅读原文](http://www.ibooker.cc/article/136/detail)

----------
![微信公众号：书客创作](http://upload-images.jianshu.io/upload_images/3480018-656e02579d7fd141..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
