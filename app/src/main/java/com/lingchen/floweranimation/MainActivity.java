package com.lingchen.floweranimation;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //终点坐标imageView
    private ImageView endFlowerIv;
    //开启动画按钮 也是起点坐标
    private Button startFlowerBt;
    private FlowerAnimation flowerAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        endFlowerIv = (ImageView) findViewById(R.id.main_end_flower_iv);
        startFlowerBt = (Button) findViewById(R.id.main_start_flower_bt);
        startFlowerBt.setOnClickListener(this);
        findViewById(R.id.goValue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ValueAnimActivity.class));
            }
        });
        flowerAnimation = new FlowerAnimation(this, (ViewGroup) findViewById(R.id.activity_main));
    }

    @Override
    public void onClick(View v) {
        flowerAnimation.addFlowerByScope(new PointF(endFlowerIv.getX(), endFlowerIv.getY()));
    }


}
