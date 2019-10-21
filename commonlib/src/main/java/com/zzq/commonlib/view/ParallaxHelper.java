package com.zzq.commonlib.view;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * @auther tangedegushi
 * @creat 2019/10/21
 * @Decribe 仿MIUI视差动画的一个工具类，可使用与RecyclerView，ScrollView，ListView以及GridView
 * 使用方法如下：
 * public class ParallaxScrollView extends ScrollView implements ParallaxHelper.SuperEvent{
 *
 *     private ParallaxHelper parallaxHelper;
 *
 *     public ParallaxScrollView(Context context, AttributeSet attrs) {
 *         super(context, attrs);
 *         parallaxHelper = new ParallaxHelper(this);
 *     }
 *
 *     @Override
 *     public boolean onInterceptTouchEvent(MotionEvent event) {
 *         return parallaxHelper.onInterceptTouchEvent(event);
 *     }
 *
 *     @Override
 *     public boolean onTouchEvent(MotionEvent event) {
 *         return parallaxHelper.onTouchEvent(event);
 *     }
 *
 *     @Override
 *     public boolean superInterceptTouchEvent(MotionEvent event) {
 *         return super.onInterceptTouchEvent(event);
 *     }
 *
 *     @Override
 *     public boolean superTouchEvent(MotionEvent event) {
 *         return super.onTouchEvent(event);
 *     }
 * }
 * 这里使用到了弹性动画，需要导入包：api 'com.android.support:support-dynamic-animation:28.0.0'
 */
public class ParallaxHelper {

    private boolean isRestoring;
    private int mActivePointerId;
    private float mInitialMotionY;
    private boolean isBeingDragged;
    private float mScale;
    private float mDistance;
    private int mTouchSlop;
    private ViewGroup parallxView;
    private SuperEvent superEvent;

    public ParallaxHelper(ViewGroup parallxView) {
        this.parallxView = parallxView;
        mTouchSlop = ViewConfiguration.get(parallxView.getContext()).getScaledTouchSlop();
        if (parallxView instanceof SuperEvent) {
            superEvent = (SuperEvent) parallxView;
        } else {
            throw new IllegalArgumentException("the construct args of view is not implements SuperEvent");
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        if (isRestoring && action == MotionEvent.ACTION_DOWN) {
            isRestoring = false;
        }
        if (!parallxView.isEnabled() || isRestoring || (!isScrollToTop() && !isScrollToBottom())) {
            return actionSuperInterceptTouchEvent(event);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mActivePointerId = event.getPointerId(0);
                isBeingDragged = false;
                float initialMotionY = getMotionEventY(event);
                if (initialMotionY == -1) {
                    return actionSuperInterceptTouchEvent(event);
                }
                mInitialMotionY = initialMotionY;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mActivePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return actionSuperInterceptTouchEvent(event);
                }
                final float y = getMotionEventY(event);
                if (y == -1f) {
                    return actionSuperInterceptTouchEvent(event);
                }
                if (isScrollToTop() && !isScrollToBottom()) {
                    // 在顶部不在底部
                    float yDiff = y - mInitialMotionY;
                    if (yDiff > mTouchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }
                } else if (!isScrollToTop() && isScrollToBottom()) {
                    // 在底部不在顶部
                    float yDiff = mInitialMotionY - y;
                    if (yDiff > mTouchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }
                } else if (isScrollToTop() && isScrollToBottom()) {
                    // 在底部也在顶部
                    float yDiff = y - mInitialMotionY;
                    if (Math.abs(yDiff) > mTouchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }
                } else {
                    // 不在底部也不在顶部
                    return actionSuperInterceptTouchEvent(event);
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                isBeingDragged = false;
                break;
        }
        return isBeingDragged || actionSuperInterceptTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                isBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                float y = getMotionEventY(event);
                if (isScrollToTop() && !isScrollToBottom()) {
                    // 在顶部不在底部
                    mDistance = y - mInitialMotionY;
                    if (mDistance < 0) {
                        return actionSuperTouchEvent(event);
                    }
                    mScale = calculateRate(mDistance);
                    pull(mScale);
                    return true;
                } else if (!isScrollToTop() && isScrollToBottom()) {
                    // 在底部不在顶部
                    mDistance = mInitialMotionY - y;
                    if (mDistance < 0) {
                        return actionSuperTouchEvent(event);
                    }
                    mScale = calculateRate(mDistance);
                    push(mScale);
                    return true;
                } else if (isScrollToTop() && isScrollToBottom()) {
                    // 在底部也在顶部
                    mDistance = y - mInitialMotionY;
                    if (mDistance > 0) {
                        mScale = calculateRate(mDistance);
                        pull(mScale);
                    } else {
                        mScale = calculateRate(-mDistance);
                        push(mScale);
                    }
                    return true;
                } else {
                    // 不在底部也不在顶部
                    return actionSuperTouchEvent(event);
                }
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                mActivePointerId = event.getPointerId(MotionEventCompat.getActionIndex(event));
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (isScrollToTop() && !isScrollToBottom()) {
                    animateRestore(true);
                } else if (!isScrollToTop() && isScrollToBottom()) {
                    animateRestore(false);
                } else if (isScrollToTop() && isScrollToBottom()) {
                    if (mDistance > 0) {
                        animateRestore(true);
                    } else {
                        animateRestore(false);
                    }
                } else {
                    return actionSuperTouchEvent(event);
                }
                break;
            }
        }
        return actionSuperTouchEvent(event);
    }

    private boolean isScrollToTop() {
        return !ViewCompat.canScrollVertically(parallxView, -1);
    }

    private boolean isScrollToBottom() {
        return !ViewCompat.canScrollVertically(parallxView, 1);
    }

    private float getMotionEventY(MotionEvent event) {
        int index = event.findPointerIndex(mActivePointerId);
        return index < 0 ? -1f : event.getY(index);
    }

    private void onSecondaryPointerUp(MotionEvent event) {
        final int pointerIndex = MotionEventCompat.getActionIndex(event);
        final int pointerId = event.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = event.getPointerId(newPointerIndex);
        }
    }

    private boolean actionSuperInterceptTouchEvent(MotionEvent event){
        return superEvent.superInterceptTouchEvent(event);
    }

    private boolean actionSuperTouchEvent(MotionEvent event){
        return superEvent.superTouchEvent(event);
    }

    private float calculateRate(float distance) {
        float originalDragPercent = distance / (parallxView.getResources().getDisplayMetrics().heightPixels);
        float dragPercent = Math.min(1f, originalDragPercent);
        float rate = 2f * dragPercent - (float) Math.pow(dragPercent, 2f);
        return 1 + rate / 8f;
    }

    private void animateRestore(final boolean isPullRestore) {
        if (isPullRestore) {
            parallxView.setPivotY(0);
        }else {
            parallxView.setPivotY(parallxView.getHeight());
        }
        SpringAnimation springAnimation = new SpringAnimation(parallxView, DynamicAnimation.SCALE_Y,1);
        SpringForce spring = springAnimation.getSpring();
        spring.setStiffness(SpringForce.STIFFNESS_LOW);
        spring.setDampingRatio(0.5f);
        springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
                isRestoring = false;
            }
        });
        isRestoring = true;
        springAnimation.setStartValue(mScale);
        springAnimation.start();
    }

    private void pull(float scale) {
        parallxView.setPivotY(0);
        parallxView.setScaleY(scale);
    }

    private void push(float scale) {
        parallxView.setPivotY(parallxView.getHeight());
        parallxView.setScaleY(scale);
    }

    public interface SuperEvent{
        boolean superInterceptTouchEvent(MotionEvent event);
        boolean superTouchEvent(MotionEvent event);
    }

}
