package com.enestigli.androidmvcnetcoreapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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

public class RegisterActivity extends AppCompatActivity {

    IMyApi iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edt_user ,edt_password;
    TextView txt_account;
    Button btn_create;




    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Init APİ
        iMyAPI = RetrofitClient.getInstance().create(IMyApi.class);
        //VIEWS
        edt_user = (EditText) findViewById(R.id.txt_user_name);
        edt_password = (EditText) findViewById(R.id.txt_password);
        btn_create = (Button) findViewById(R.id.btn_create);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new SpotsDialog.Builder()
                        .setContext(RegisterActivity.this)
                        .build();
                dialog.show();

                tblUser user = new tblUser(edt_user.getText().toString(),edt_password.getText().toString(),"");


                compositeDisposable.add(iMyAPI.registerUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if(s.equals("Kayıt başarılı")){
                                    finish();

                                }
                                Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this,throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }));
            }
        });
    }
}