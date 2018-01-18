# ZLetterBar
按照字母#A~Z自定义侧边栏控件，实现列表索引，同时具有字母弹出效果。

### 引入布局

```
<cc.ibooker.letterbarview.LetterBar
    android:id="@+id/letterBar"
    android:layout_width="wrap_content"
    android:layout_height="match_parent" />
```

### 属性设置

```
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
```
