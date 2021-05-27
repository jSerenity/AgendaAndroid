package com.example.parcial1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial1.clases.articulos;
import com.example.parcial1.clases.listas;
import com.example.parcial1.tablas.tablas;

import java.util.ArrayList;

public class RegistroCompras extends AppCompatActivity {

    ListView listVfecha;
    ArrayList<listas> listarfecha;
    ArrayList<String> listainfocompra;
    ArrayAdapter adaptador;
    AppSQLiteOpenHepler conn;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_compras);

        listVfecha= (ListView) findViewById(R.id.listviewfecha);

        conn= new AppSQLiteOpenHepler(this,"db_agenda",null,1);
        consultarlistadecompras();

        adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainfocompra);
        listVfecha.setAdapter(adaptador);

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

    public void onClickRC(View view){
        Intent redirectintent= null;
        switch (view.getId()){
            case R.id.listviw:
                redirectintent=new Intent(RegistroCompras.this,RegistroCompra2.class);
                break;
        }
        if (redirectintent!=null){
            startActivity(redirectintent);
        }
        // registrararticulo();
    }
}
