package com.enestigli.androidmvcnetcoreapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enestigli.androidmvcnetcoreapi.Model.tblUser;
import com.enestigli.androidmvcnetcoreapi.Remote.IMyApi;
import com.enestigli.androidmvcnetcoreapi.Remote.RetrofitClient;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    IMyApi iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edt_user ,edt_password;
    TextView txt_account;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init APİ
        iMyAPI = RetrofitClient.getInstance().create(IMyApi.class);
        //VIEWS
        edt_user = (EditText) findViewById(R.id.txt_user_name);
        edt_password = (EditText) findViewById(R.id.txt_password);
        btn_login = (Button) findViewById(R.id.btn_register);
        txt_account = (TextView) findViewById(R.id.txt_kayıt_ol);

        //EVENTS

        txt_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new SpotsDialog.Builder()
                                         .setContext(MainActivity.this)
                                         .build();
                dialog.show();

              tblUser user = new tblUser(edt_user.getText().toString(),edt_password.getText().toString(),"");

              compositeDisposable.add(iMyAPI.registerUser(user)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Consumer<String>() {
                  @Override
                  public void accept(String s) throws Exception {
                      Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                  }
              }, new Consumer<Throwable>() {
                  @Override
                  public void accept(Throwable throwable) throws Exception {
                      dialog.dismiss();
                      Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_LONG).show();
                  }
              }));

            }
        });
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}