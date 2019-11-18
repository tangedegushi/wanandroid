package com.zzq.commonlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @auther tangedegushi
 * @creat 2019/10/22
 * @Decribe
 */
public class ParallaxScrollView extends ScrollView implements ParallaxHelper.SuperEvent{

    private ParallaxHelper parallaxHelper;
    public ParallaxScrollView(Context context) {
        super(context);
        initParallax();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParallax();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParallax();
    }

    private void initParallax(){
        parallaxHelper = new ParallaxHelper(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return parallaxHelper.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return parallaxHelper.onTouchEvent(ev);
    }

    @Override
    public boolean superInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean superTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
