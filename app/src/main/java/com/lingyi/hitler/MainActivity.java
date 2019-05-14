package com.lingyi.hitler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int a[] = new int[2];
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TargetMath targetMath = new TargetMath();
                TestMethodInsert insert = new TestMethodInsert();
                insert.show();
                insert.testTryCatch();
                ((TextView)v).setText("8+2= "+targetMath.add(8,2));
            }
        });
    }
}
