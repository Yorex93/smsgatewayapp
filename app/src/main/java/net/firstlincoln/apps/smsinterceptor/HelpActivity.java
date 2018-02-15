package net.firstlincoln.apps.smsinterceptor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.kbiakov.codeview.CodeView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);


        CodeView codeView = findViewById(R.id.code_view_help);

        codeView.setCode(getResources().getString(R.string.sample_payload));



    }
}
