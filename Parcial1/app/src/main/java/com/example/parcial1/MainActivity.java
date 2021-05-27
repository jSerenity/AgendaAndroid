package com.example.parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppSQLiteOpenHepler conn= new AppSQLiteOpenHepler(this,"db_agenda",null,1);

    }
    public void onClick(View view){
        Intent redirectintent= null;
        switch (view.getId()){
            case R.id.button_articles:
                redirectintent=new Intent(MainActivity.this,registrararticulos.class);
                break;

            case R.id.button_shop:
                redirectintent=new Intent(MainActivity.this,RegistroCompras.class);
                break;
        }
        if (redirectintent!=null){
            startActivity(redirectintent);
        }
    }
}