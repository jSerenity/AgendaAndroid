package com.example.parcial1;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial1.clases.articulos;
import com.example.parcial1.tablas.tablas;

import java.util.ArrayList;
import java.util.List;

public class registrararticulos extends AppCompatActivity {
    EditText nombre;
    ListView listViewtarticulos;
    ArrayList<articulos> listaarticulos;
    ArrayList<String> listainfoartic;
    ArrayAdapter adaptador;
    AppSQLiteOpenHepler conn;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_articulos);

        nombre= (EditText) findViewById(R.id.txt_arti);
        listViewtarticulos= (ListView) findViewById(R.id.listviwarti);

        conn= new AppSQLiteOpenHepler(this,"db_agenda",null,1);
        consultarlistadearticulos();

        adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainfoartic);
        listViewtarticulos.setAdapter(adaptador);

    }
    public void onClickR(View view){
        registro();
        consultarlistadearticulos();
        recargardata();
       // registrararticulo();
    }
private  void registro(){
        SQLiteDatabase db = conn.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(tablas.CAMPO_NOMBRE, nombre.getText().toString());

    long id = db.insert(tablas.TABLA_ARTICULOS, tablas.CAMPO_NOMBRE,values);
    Toast.makeText(getApplicationContext(),"id: "+id, Toast.LENGTH_SHORT).show();
    db.close();
}
    private void registrararticulo() {

        SQLiteDatabase db=conn.getWritableDatabase();

        String insert="INSERT INTO " + tablas.TABLA_ARTICULOS + "( "+tablas.CAMPO_NOMBRE+")" +
                " VALUES ("+nombre.getText().toString()+")";

        db.execSQL(insert);
        consultarlistadearticulos();
        db.close();
        Toast.makeText(this, "Articulo Agregado", Toast.LENGTH_SHORT).show();

    }

    public void consultarlistadearticulos() {
        SQLiteDatabase db=conn.getReadableDatabase();
        articulos arti=null;
        listaarticulos= new ArrayList<articulos>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ tablas.TABLA_ARTICULOS,null);

        while (cursor.moveToNext()){
            arti = new articulos();
            arti.setID(cursor.getInt(0));
            arti.setNombre(cursor.getString(1));

            listaarticulos.add(arti);

        }
        db.close();
        obtenerlista();

    }

    private void obtenerlista() {
        listainfoartic = new ArrayList<String>();

        for(int i=0; i<listaarticulos.size();i++){
            listainfoartic.add(listaarticulos.get(i).getNombre());
        }
    }

    private void recargardata(){
        adaptador.notifyDataSetChanged();

    }
}
