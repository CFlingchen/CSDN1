package com.lingchen.floweranimation;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Author    lingchen
 * Email     838878458@qq.com
 * Time      2016/11/2
 * Function  实现观众看直播 送花效果
 */

public class ValueAnimActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ValueAnimActivity";
    private TextView countTv;
    private Button startBt;

    //终点坐标imageView
    private ImageView endFlowerIv;
    //开启动画按钮 也是起点坐标
    private Button startFlowerBt;
    private FlowerAnimation flowerAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value);
        initView();
    }

    private void initView() {
       /* countTv = (TextView) findViewById(R.id.value_count_tv);
        startBt = (Button) findViewById(R.id.value_start_bt);
        startBt.setOnClickListener(this);*/
        endFlowerIv = (ImageView) findViewById(R.id.value_end_flower_iv);
        startFlowerBt = (Button) findViewById(R.id.value_start_flower_bt);
        startFlowerBt.setOnClickListener(this);
        flowerAnimation = new FlowerAnimation(this, (ViewGroup) findViewById(R.id.value_rootView_ff));

    }


    @Override
    public void onClick(View v) {
        flowerAnimation.addFlowerByValueAnim(new PointF(v.getX(), v.getY()), new PointF(endFlowerIv.getX(), endFlowerIv.getY()));
    }

    private void startValueAnim() {
        //从0-10 时间10s
        ValueAnimator countAnim = ValueAnimator.ofInt(0, 10)
                .setDuration(10000);
        countAnim.setInterpolator(new LinearInterpolator());
        countAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                countTv.setText(animation.getAnimatedValue().toString());
            }
        });
        countAnim.start();
    }

    private void startValueAnim1() {
        //从0-10 时间10s
        ValueAnimator countAnim = ValueAnimator.ofObject(new IntEvaluator() {
            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                Log.e(TAG, "evaluate() called with: fraction = [" + fraction + "], startValue = [" + startValue + "], endValue = [" + endValue + "]");
                return super.evaluate(fraction, startValue, endValue);
            }
        }, 0, 10)
                .setDuration(10000);
        countAnim.setInterpolator(new LinearInterpolator());
        countAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                countTv.setText(animation.getAnimatedValue().toString());
            }
        });
        countAnim.start();
    }

    @Override
    public void onBackPressed() {
        if (flowerAnimation != null)
            flowerAnimation.onDestroy();
        super.onBackPressed();
    }

}
