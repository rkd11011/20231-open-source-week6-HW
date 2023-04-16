package com.example.photoshop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    ImageButton ibZoomin, ibZoomout, ibRotate, ibBright, ibDark, ibGray;
    MyGraphicView graphicView;

    static float scaleX = 1, scaleY = 1;// 확대, 축소 비율
    static float angle = 0;// 회전 각도
    static float color = 1;// 밝기 값
    static float satur = 1; // 채도 값

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바 설정
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.image);
        setTitle("미니 포토샵");


        // LinearLayout 객체 graphicView 추가
        LinearLayout pictureLayout = (LinearLayout) findViewById(R.id.pictureLayout);
        graphicView = (MyGraphicView) new MyGraphicView(this);
        pictureLayout.addView(graphicView);


        // 아이콘들의 클릭 이벤트를 처리하는 메소드 호출
        clickIcons();
    }

    private void clickIcons() {

        // 확대 아이콘 클릭 이벤트 처리
        ibZoomin = (ImageButton) findViewById(R.id.ibZoomin);
        ibZoomin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scaleX = scaleX + 0.2f;
                scaleY = scaleY + 0.2f;
                graphicView.invalidate(); // View 갱신
            }
        });


        // 축소 아이콘 클릭 이벤트 처리
        ibZoomout = (ImageButton) findViewById(R.id.ibZoomout);
        ibZoomout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scaleX = scaleX - 0.2f;
                scaleY = scaleY - 0.2f;
                graphicView.invalidate();
            }
        });


        //회전 아이콘 클릭 이벤트 처리
        ibRotate = (ImageButton) findViewById(R.id.ibRotate);
        ibRotate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                angle = angle + 20;
                graphicView.invalidate();
            }
        });

        //밝기 증가 아이콘 클릭 처리
        ibBright = (ImageButton) findViewById(R.id.ibBright);
        ibBright.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                color = color + 0.2f;
                graphicView.invalidate();
            }
        });

        //밝기 감소 아이콘 클릭 이벤트 처리
        ibDark = (ImageButton) findViewById(R.id.ibDark);
        ibDark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                color = color - 0.2f;
                graphicView.invalidate();
            }
        });

        // 밝기 감소 아이콘 클릭 이벤트 처리
        ibGray = (ImageButton) findViewById(R.id.ibGray);
        ibGray.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (satur == 0)
                    satur = 1;
                else
                    satur = 0;
                graphicView.invalidate();
            }
        });

    }

    //View 클래스를 상속받아 구현
    private static class MyGraphicView extends View {
        public MyGraphicView(Context context) {
            super(context);
        }

        // 그래픽 처리를 위한 onDraw 메서드
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // 중심점을 구하고 scaleX, scaleY로 크기 조절, angle로 회전 처리
            int cenX = this.getWidth() / 2;
            int cenY = this.getHeight() / 2;
            canvas.scale(scaleX, scaleY, cenX, cenY);
            canvas.rotate(angle, cenX, cenY);


            // 컬러 필터를 적용하기 위한 Paint 객체 생성
            Paint paint = new Paint();

            // 색조 변경을 위한 float 배열
            float[] array = { color, 0, 0, 0, 0, 0, color, 0, 0, 0, 0, 0,
                    color, 0, 0, 0, 0, 0, 1, 0 };
            ColorMatrix cm = new ColorMatrix(array);

            // 채도 변경 처리
            if (satur == 0)
                cm.setSaturation(satur);

            // 컬러 필터 적용
            paint.setColorFilter(new ColorMatrixColorFilter(cm));

            // 이미지를 Bitmap으로 디코딩하여 canvas에 그림
            Bitmap picture = BitmapFactory.decodeResource(getResources(),
                    R.drawable.poto);
            int picX = (this.getWidth() - picture.getWidth()) / 2;
            int picY = (this.getHeight() - picture.getHeight()) / 2;
            canvas.drawBitmap(picture, picX, picY, paint);

            // Bitmap 메모리 해제
            picture.recycle();
        }
    }
}
