package com.example.parcial1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial1.clases.articulos;
import com.example.parcial1.tablas.tablas;

import java.util.ArrayList;

public class RegistroCompra2 extends AppCompatActivity {

    AppSQLiteOpenHepler conn;
    EditText nombreFecha;
    ListView listViewarticulos;
    ArrayList<articulos> listaarticulos;
    ArrayList<String> listainfoartic;
    ArrayAdapter adaptadorr;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_compras2);

        conn= new AppSQLiteOpenHepler(this,"db_agenda",null,1);
        nombreFecha =(EditText) findViewById(R.id.text_fecha);
        listViewarticulos= (ListView) findViewById(R.id.listadearti);

        this.listViewarticulos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        this.listViewarticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Log.i(TAG, "onItemClick: " +position);
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                articulos arti = (articulos) listViewarticulos.getItemAtPosition(position);
                arti.setActive(!currentCheck);
            }
        });

        consultarlistadearticulos();
        
        // adaptadorr= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainfoartic);
        //listViewarticulos.setAdapter(adaptadorr);
    }


    private  void registro(){
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tablas.CAMPO_FECHA, nombreFecha.getText().toString());

        long id = db.insert(tablas.TABLA_LISTAS, tablas.CAMPO_FECHA,values);
        Toast.makeText(getApplicationContext(),"id: "+id, Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void onClickRF(View view){
        registro();
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
        ArrayAdapter<articulos> arrayAdapter
                = new ArrayAdapter(this, android.R.layout.simple_list_item_checked ,listaarticulos);

        this.listViewarticulos.setAdapter(arrayAdapter);

        for(int i=0;i< listaarticulos.size(); i++ )  {
            this.listViewarticulos.setItemChecked(i,listaarticulos.get(i).isActive());
        }

    }



}
