package com.example.parcial1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial1.clases.articulos;
import com.example.parcial1.clases.listas;
import com.example.parcial1.tablas.tablas;

import java.util.ArrayList;
import java.util.List;

public class registrararticulos extends AppCompatActivity {
    EditText nombre;
    Switch estado;
    ListView listViewtarticulos;
    ArrayList<articulos> listaarticulos;
    ArrayList<String> listainfoartic;
    ArrayAdapter adaptador;
    AppSQLiteOpenHepler conn;
    boolean updateStatus=false;
    articulos articuloSelected;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_articulos);

        nombre= (EditText) findViewById(R.id.txt_arti);
        estado = (Switch) findViewById(R.id.switch1);
        listViewtarticulos= (ListView) findViewById(R.id.listviwarti);

        conn= new AppSQLiteOpenHepler(this,"db_agenda",null,1);
        consultarlistadearticulos();
        recargardata();
        this.listViewtarticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                articuloSelected  = listaarticulos.get(position);
                nombre.setText(articuloSelected.getNombre());
                estado.setChecked(articuloSelected.isEstado());
                updateStatus=true;
            }
        });

    }
    public void onClickR(View view){
        switch (view.getId()) {
            case R.id.guardarArticulo:
                if(updateStatus){
                    registroUpdate();
                    updateStatus=false;
                    nombre.setText("");
                    estado.setChecked(false);
                }else{
                    registro();
                    updateStatus=false;
                    nombre.setText("");
                    estado.setChecked(false);
                }
                break;
        }


        consultarlistadearticulos();
        recargardata();
       // registrararticulo();
    }
    public  void onClickREDIRECT(View view){
        switch (view.getId()) {
            case R.id.preview_articulo:
                redirect();
                break;
        }
    }

    private void redirect() {
        Intent intent=new Intent(registrararticulos.this,MainActivity.class);

        startActivity(intent);
    }

    private  void registro(){
        SQLiteDatabase db = conn.getWritableDatabase();
        if(nombre.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"CAMPO NOMBRE SIN VALOR", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put(tablas.CAMPO_NOMBRE, nombre.getText().toString());
            values.put(tablas.CAMPO_ESTADO, estado.isChecked()?1:0);

            long id = db.insert(tablas.TABLA_ARTICULOS, tablas.CAMPO_ID,values);
            Toast.makeText(getApplicationContext(),"id: "+id, Toast.LENGTH_SHORT).show();
            db.close();
        }

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
            arti.setEstado(cursor.getInt(2)==1?true:false);
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
        adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainfoartic);
        listViewtarticulos.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }
    private  void registroUpdate(){
        if(nombre.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"CAMPO NOMBRE SIN VALOR", Toast.LENGTH_SHORT).show();
        }else {
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametro = {articuloSelected.getID().toString()};
            ContentValues values = new ContentValues();
            values.put(tablas.CAMPO_NOMBRE, nombre.getText().toString());
            values.put(tablas.CAMPO_ESTADO, estado.isChecked() ? 1 : 0);

            db.update(tablas.TABLA_ARTICULOS, values, tablas.CAMPO_ID + "=?", parametro);
            Toast.makeText(getApplicationContext(), "ARTICULO ACTUALIZADO: ", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }
}
