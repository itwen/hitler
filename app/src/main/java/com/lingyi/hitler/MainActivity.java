package com.lingyi.hitler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lingyi.hitler.test.TestMethodInsert;

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
                ((TextView)v).setText("8+2= "+targetMath.add(8,2));
                TestMethodInsert insert = new TestMethodInsert(v);
                insert.show(new String []{"sss","aaaa"});
                insert.testTryCatch();
                insert.replaceMethodReturn("replacereturn ",3);
                insert.replaceMethod("replace ");
                insert.testTryCatchReturn();
            }
        });
    }
}
