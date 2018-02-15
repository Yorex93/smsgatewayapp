package net.firstlincoln.apps.smsinterceptor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.firstlincoln.apps.smsinterceptor.db.database.AppDatabase;
import net.firstlincoln.apps.smsinterceptor.db.entity.SmsEntity;
import net.firstlincoln.apps.smsinterceptor.models.Sms;

import java.text.SimpleDateFormat;

public class SmsDetail extends AppCompatActivity {

    private int sms_id = 1;

    private TextView tvSender;

    private TextView tvDate;

    private EditText etMessage;

    private Button btnMarkPending;

    private Button btnMarkSuccess;

    private SmsEntity sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            sms_id = extras.getInt("sms_id");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_sms_detail);

        tvSender = findViewById(R.id.textView_sender);

        tvDate =  findViewById(R.id.textView_sms_date);

        etMessage = findViewById(R.id.sms_detail_edit_text);

        btnMarkPending = findViewById(R.id.button_mark_pending);

        btnMarkSuccess = findViewById(R.id.button_success);

        btnMarkPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPending();
            }
        });

        btnMarkSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSuccess();
            }
        });




        new Thread(new Runnable() {
            @Override
            public void run() {
                sms = AppDatabase.getAppDatabase(getApplicationContext()).smsDao().getSmsById(sms_id);
                displaySms();
            }
        }).start();


    }

    private void displaySms() {
       this.runOnUiThread(new Runnable() {
           @Override
           public void run() {
                tvSender.setText(sms.getPhone());
                tvDate.setText(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(sms.getCreatedAt()));
                etMessage.setText(sms.getContent());
           }
       });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sms_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_sms:
                deleteSms();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteSms() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Delete this message?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getAppDatabase(getApplicationContext()).smsDao().deleteSms(sms);
                    }
                }).start();

                Toast.makeText(getApplicationContext(), "Message Deleted", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void setPending(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.setContent(etMessage.getText().toString());
                sms.setStatus(0);
                AppDatabase.getAppDatabase(getApplicationContext()).smsDao().updateSms(sms);

            }
        }).start();

        Toast.makeText(getApplicationContext(), "Marked as pending", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void setSuccess(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.setStatus(1);
                AppDatabase.getAppDatabase(getApplicationContext()).smsDao().updateSms(sms);
            }
        }).start();

        Toast.makeText(getApplicationContext(), "Marked as success", Toast.LENGTH_SHORT).show();
        finish();
    }


}
