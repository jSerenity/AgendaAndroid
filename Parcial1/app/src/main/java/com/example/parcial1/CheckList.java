package com.example.parcial1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial1.clases.listas;
import com.example.parcial1.tablas.tablas;

import java.util.ArrayList;

public class CheckList extends AppCompatActivity {

    ListView listVfecha;
    ArrayList<listas> listarfecha;
    ArrayList<String> listainfocompra;
    ArrayAdapter adaptador;
    AppSQLiteOpenHepler conn;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_list);

        listVfecha= (ListView) findViewById(R.id.listViewcheck);

        conn= new AppSQLiteOpenHepler(this,"db_agenda",null,1);
        consultarlistadecompras();

        adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainfocompra);
        listVfecha.setAdapter(adaptador);

        this.listVfecha.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listas list  = listarfecha.get(position);

                Intent intent=new Intent(CheckList.this,CheckList_Compra.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("Fecha",list);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }
    private void consultarlistadecompras() {
        SQLiteDatabase db=conn.getReadableDatabase();
        listas fecha=null;
        listarfecha= new ArrayList<listas>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ tablas.TABLA_LISTAS,null);

        while (cursor.moveToNext()){
            fecha = new listas();
            fecha.setID(cursor.getInt(0));
            fecha.setFecha(cursor.getString(1));

            listarfecha.add(fecha);

        }
        db.close();
        obtenerlisfecha();
    }
    private void obtenerlisfecha() {
        listainfocompra = new ArrayList<String>();

        for(int i=0; i<listarfecha.size();i++){
            listainfocompra.add(listarfecha.get(i).getFecha());
        }
    }
    public void onClickCheckMain(View view){
        switch (view.getId()) {
            case R.id.previus_check:
                redirect();
                break;
        }
    }

    private void redirect() {
        Intent intent=new Intent(CheckList.this,MainActivity.class);

        startActivity(intent);
    }
}
