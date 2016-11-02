package com.lingchen.floweranimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Author    lingchen
 * Email     838878458@qq.com
 * Time      2016/11/2
 * Function  开启花的效果
 */

public class FlowerAnimation {

    private static final String TAG = "FlowerAnimation";
    //上下文
    private Context mContext;
    //生成的View添加在的ViewGroup
    private ViewGroup rootView;
    private ViewGroup.LayoutParams layoutParams;

    //资源文件
    private Drawable[] drawables;
    //插值器
    private Interpolator[] interpolators;

    //起点终点坐标
    private PointF startP, stopP;

    private Random rand = new Random();

    public FlowerAnimation(Context mContext, ViewGroup rootView) {
        this(mContext, rootView, null, null);
    }


    public FlowerAnimation(Context mContext, ViewGroup rootView, PointF startP, PointF stopP) {
        this.mContext = mContext;
        this.rootView = rootView;
        this.startP = startP;
        this.stopP = stopP;
        init();
    }

    private void init() {
        drawables = new Drawable[8];
        drawables[0] = mContext.getResources().getDrawable(R.mipmap.flower_01);
        drawables[1] = mContext.getResources().getDrawable(R.mipmap.flower_02);
        drawables[2] = mContext.getResources().getDrawable(R.mipmap.flower_03);
        drawables[3] = mContext.getResources().getDrawable(R.mipmap.flower_04);
        drawables[4] = mContext.getResources().getDrawable(R.mipmap.flower_05);
        drawables[5] = mContext.getResources().getDrawable(R.mipmap.flower_06);
        drawables[6] = mContext.getResources().getDrawable(R.mipmap.flower_07);
        drawables[7] = mContext.getResources().getDrawable(R.mipmap.flower_08);

        interpolators = new Interpolator[4];
        interpolators[0] = new LinearInterpolator();//线性
        interpolators[1] = new AccelerateInterpolator();//加速
        interpolators[2] = new DecelerateInterpolator();//减速
        interpolators[3] = new AccelerateDecelerateInterpolator();//先加速后减速

        layoutParams = new ViewGroup.LayoutParams(DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
    }


    /**
     * 添加花朵 随即生成起点（rootView范围）
     *
     * @param stopP 终点
     */
    public void addFlowerByScope(@NonNull PointF stopP) {
        float x = rand.nextFloat() * rootView.getWidth();
        float y = rand.nextFloat() * rootView.getHeight();
        addFlower(new PointF(x, y), stopP);
    }


    /**
     * 添加花朵 随即生成起点
     *
     * @param stopP  终点
     * @param scopeP 范围  随即生成的点将会按照此范围随即取值
     */
    public void addFlowerByScope(@NonNull PointF stopP, @NonNull PointF scopeP) {
        float x = rand.nextFloat() * scopeP.x;
        float y = rand.nextFloat() * scopeP.y;
        addFlower(new PointF(x, y), stopP);
    }

    /**
     * 添加花朵
     *
     * @param startPoint 起点
     * @param stopP      终点
     */
    public void addFlower(@NonNull PointF startPoint, @NonNull PointF stopP) {
        ImageView flower = new ImageView(mContext);
        flower.setX(startPoint.x);
        flower.setY(startPoint.y);
        Drawable drawable = drawables[rand.nextInt(drawables.length)];
        flower.setBackground(drawable);
        rootView.addView(flower, layoutParams);
        startAnim(flower, null, stopP);
    }


    /**
     * 开启动画
     *
     * @param view  执行动画的view
     * @param stopP 终点
     */
    public void startAnim(@NonNull final View view, @NonNull PointF stopP) {
        startAnim(view, null, stopP);
    }


    /**
     * 开启动画
     *
     * @param view   执行动画的view
     * @param startP 起点 如果传null 默认从view位置开始
     * @param stopP  终点
     */
    public void startAnim(@NonNull final View view, @Nullable PointF startP, @NonNull PointF stopP) {
        if (startP == null) {
            startP = new PointF(view.getX(), view.getY());
        }
        //透明度变化
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        animatorAlpha.setDuration(200);
        //位移动画
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "translationX", startP.x, stopP.x);
        animatorX.setDuration(1000);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "translationY", startP.y, stopP.y);
        animatorY.setDuration(1000);
        //生成动画集合
        final AnimatorSet set = new AnimatorSet();

        //开启透明度动画然后执行位移动画
        set.play(animatorAlpha).before(animatorX).with(animatorY);
        //加入植入器
        set.setInterpolator(interpolators[rand.nextInt(interpolators.length)]);
        //添加动画监听事件 为了移除view 防止造成oom
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rootView.removeView(view);
            }

        });
        set.start();
    }

    /**
     * 开启贝塞尔曲线动画
     * 根据rootView 在上下方向分别随即取1个点 作为中间控制点
     * 本身的位置作为起点
     *
     * @param startP 起点
     * @param stopP  终点
     */
    public void addFlowerByValueAnim(@NonNull PointF startP, @NonNull PointF stopP) {
        ImageView flower = new ImageView(mContext);
        flower.setX(startP.x);
        flower.setY(startP.y);
        Drawable drawable = drawables[rand.nextInt(drawables.length)];
        flower.setBackground(drawable);
        rootView.addView(flower, layoutParams);
        addFlowerByValueAnim(flower, getPointF(0), getPointF(1), startP, stopP);
    }


    /**
     * 开启贝塞尔曲线动画
     * 根据rootView 在上下方向分别随即取1个点 作为中间控制点
     * 本身的位置作为起点
     *
     * @param view  动画view
     * @param stopP 终点
     */
    public void addFlowerByValueAnim(@NonNull final View view, @NonNull PointF stopP) {
        addFlowerByValueAnim(view, getPointF(0), getPointF(1), startP, stopP);
    }

    /**
     * 开启贝塞尔曲线动画
     *
     * @param view    动画view
     * @param startP1 中间控制点1
     * @param startP2 中间控制点2
     * @param startP  起点
     * @param stopP   终点
     */
    public void addFlowerByValueAnim(@NonNull final View view, @Nullable PointF startP1, @Nullable PointF startP2, @Nullable PointF startP, @NonNull PointF stopP) {
        //透明度变化
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        animatorAlpha.setDuration(200);
        //生成动画集合
        final AnimatorSet set = new AnimatorSet();

        //开启透明度动画然后执行位移动画
        set.play(animatorAlpha).before(getValueAnimator(view, startP1, startP2, startP, stopP));
        //加入植入器
        set.setInterpolator(interpolators[rand.nextInt(interpolators.length)]);
        //添加动画监听事件 为了移除view 防止造成oom
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rootView.removeView(view);
            }

        });
        set.start();
    }

    /**
     * 获取贝塞尔曲线动画
     *
     * @param view    动画view
     * @param startP1 中间控制点1
     * @param startP2 中间控制点2
     * @param startP  起点
     * @param stopP   终点
     */
    private ValueAnimator getValueAnimator(@NonNull final View view, @Nullable PointF startP1, @Nullable PointF startP2, @Nullable PointF startP, @NonNull PointF stopP) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MyTypeEvaluator(startP1, startP2), startP, stopP);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(interpolators[rand.nextInt(interpolators.length)]);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                view.setX(pointF.x);
                view.setY(pointF.y);
            }
        });
        return valueAnimator;
    }


    /**
     * 获取中间控制点
     * 取rootView范围
     *
     * @param i =0为上方控制点 ！=0 为下方
     * @return
     */
    public PointF getPointF(int i) {
        return getPointF(i, new PointF(rootView.getWidth(), rootView.getHeight()));
    }

    /**
     * 获取中间控制点
     *
     * @param i      =0为上方控制点 ！=0 为下方
     * @param scopeP 范围
     * @return
     */
    public PointF getPointF(int i, @NonNull PointF scopeP) {
        PointF p = new PointF();
        //随即范围[0,scopeP.x]
        p.x = rand.nextFloat() * scopeP.x;
        float height = scopeP.y / 2;
        //随即范围[0,height]
        float y = rand.nextFloat() * height;
        if (i != 0) {
            //随即范围[height,scopeP.y]
            y = y + height;
        }
        p.y = y;
        return p;
    }


    /**
     * 自定义的估值器
     */
    public static class MyTypeEvaluator implements TypeEvaluator<PointF> {
        private PointF pointF1, pointF2;

        public MyTypeEvaluator(PointF pointF1, PointF pointF2) {
            this.pointF1 = pointF1;
            this.pointF2 = pointF2;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float timeLeft = 1.0f - fraction;
            PointF pointF = new PointF();//结果
            pointF.x = timeLeft * timeLeft * timeLeft * (startValue.x)
                    + 3 * timeLeft * timeLeft * fraction * (pointF1.x)
                    + 3 * timeLeft * fraction * fraction * (pointF2.x)
                    + fraction * fraction * fraction * (endValue.x);

            pointF.y = timeLeft * timeLeft * timeLeft * (startValue.y)
                    + 3 * timeLeft * timeLeft * fraction * (pointF1.y)
                    + 3 * timeLeft * fraction * fraction * (pointF2.y)
                    + fraction * fraction * fraction * (endValue.y);
            return pointF;
        }
    }

}
