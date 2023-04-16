package com.example.diary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

// 메인, AppCompatActivity 상속
public class MainActivity extends AppCompatActivity {

    // UI 구성 요소
    DatePicker dp;
    EditText edtDiary;
    Button btnWrite;
    String fileName;

    // onCreate 메서드 재정의
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 레이아웃 파일 설정
        setContentView(R.layout.activity_main);

        // 앱 타이틀과 앱 아이콘 설정
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.image);
        setTitle("간단 일기장");

        // UI 구성 요소 초기화
        dp = (DatePicker) findViewById(R.id.datePicker1);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnWrite = (Button) findViewById(R.id.btnWrite);

        // 현재 날짜를 기준으로 DatePicker 초기화
        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {

            // 선택한 날짜를 기반으로 파일 이름 생성
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                fileName = Integer.toString(year) + "_"
                        + Integer.toString(monthOfYear + 1) + "_"
                        + Integer.toString(dayOfMonth) + ".txt";

                // 해당 날짜의 일기를 읽어서 EditText에 표시
                String str = readDiary(fileName);
                edtDiary.setText(str);

                // 쓰기 버튼 활성화
                btnWrite.setEnabled(true);
            }
        });


        // 쓰기 버튼 클릭 이벤트 리스너 설정
        btnWrite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    // 선택한 날짜를 기반으로 파일 이름 생성
                    FileOutputStream outFs = openFileOutput(fileName,
                            Context.MODE_PRIVATE);

                    // EditText에서 일기 텍스트를 가져와 파일에 저장
                    String str = edtDiary.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();

                    // 저장 완료를 알리는 토스트 메시지 출력
                    Toast.makeText(getApplicationContext(),
                            fileName + " 이 저장됨", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                }
            }
        });

    }


    // 해당 날짜의 일기를 내부 저장소에서 읽는 메서드
    String readDiary(String fName) {
        String diaryStr = null;
        FileInputStream inFs;
        try {
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
            btnWrite.setText("수정 하기");
        } catch (IOException e) {
            edtDiary.setHint("일기 없음");
            btnWrite.setText("새로 저장");
        }
        return diaryStr;
    }
}