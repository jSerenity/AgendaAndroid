package com.example.parcial1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial1.clases.articulos;
import com.example.parcial1.clases.listas;
import com.example.parcial1.tablas.tablas;

import java.util.ArrayList;
import java.util.Calendar;

public class RegistroCompra2 extends AppCompatActivity {

    AppSQLiteOpenHepler conn;
    EditText nombreFecha;
    ListView listViewarticulos;
    ArrayList<articulos> listaarticulos;
    ArrayList<String> listainfoartic;
    ArrayAdapter adaptadorr;
    listas list_fecha;
    ImageView SAVEB;
    ImageView UPDATEB;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_compras2);

        conn= new AppSQLiteOpenHepler(this,"db_agenda",null,1);
        nombreFecha =(EditText) findViewById(R.id.text_fecha);
        listViewarticulos= (ListView) findViewById(R.id.listadearti);
        SAVEB = (ImageView) findViewById(R.id.save);
        UPDATEB =(ImageView) findViewById(R.id.update);

        Bundle objetoEnviado=getIntent().getExtras();

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

        if (objetoEnviado!=null){
            list_fecha=(listas) objetoEnviado.getSerializable("Fecha");
            nombreFecha.setText(list_fecha.getFecha().toString());
            SAVEB.setVisibility(View.GONE);
            UPDATEB.setVisibility(View.VISIBLE);
            consultarUpdate();
         }
           else {
            SAVEB.setVisibility(View.VISIBLE);
            UPDATEB.setVisibility(View.GONE);
                   consultarlistadearticulos();
                }

        nombreFecha.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    nombreFecha.setText(current);
                    nombreFecha.setSelection(sel < current.length() ? sel : current.length());



                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        // adaptadorr= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainfoartic);
        //listViewarticulos.setAdapter(adaptadorr);
    }


    private  void registro(){
        SQLiteDatabase db = conn.getWritableDatabase();
        ArrayList<Integer> at =articulosSeleccionados();
        if(!nombreFecha.getText().toString().equals("") && at.size()>0){
            ContentValues values = new ContentValues();
            values.put(tablas.CAMPO_FECHA, nombreFecha.getText().toString());

            long id = db.insert(tablas.TABLA_LISTAS, tablas.CAMPO_ID_LISTA,values);
            Toast.makeText(getApplicationContext(),"id: "+id, Toast.LENGTH_SHORT).show();


            registrarFechaARTICULO(at,id);
            db.close();
            redirect();
        }else {
            Toast.makeText(getApplicationContext(),"LISTA SIN NOMBRE O SIN ARTICULOS SELECCIONADOS", Toast.LENGTH_SHORT).show();
        }


    }

    public void onClickRF(View view){
        switch (view.getId()) {
            case R.id.save:
                registro();

                break;
            case R.id.update:
                registroUpdate();
                break;
            case R.id.previus_2:
                redirect();
                break;
        }
    }

    public void consultarlistadearticulos() {
        SQLiteDatabase db=conn.getReadableDatabase();
        articulos arti=null;
        listaarticulos= new ArrayList<articulos>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ tablas.TABLA_ARTICULOS +" WHERE "+ tablas.CAMPO_ESTADO +"= 1",null);

        while (cursor.moveToNext()){
            arti = new articulos();
            arti.setID(cursor.getInt(0));
            arti.setNombre(cursor.getString(1));

            listaarticulos.add(arti);

        }
        db.close();
        obtenerlista();

    }
    public void consultarUpdate() {
        SQLiteDatabase db=conn.getReadableDatabase();
        articulos arti=null;
        listaarticulos= new ArrayList<articulos>();
        String consulta = "SELECT a."+tablas.CAMPO_ID+",a."+tablas.CAMPO_NOMBRE+",CASE "+ "IFNULL(b."+tablas.ID_LISTAS +",'0')WHEN '0'THEN'0'ELSE'1' END  checkactivos"+ " FROM "+ tablas.TABLA_ARTICULOS+" a" +" LEFT JOIN "+ tablas.TABLA_ARTI_LIST +" b" +" ON a."
                + tablas.CAMPO_ID+" = b."+tablas.ID_ARTICULOS+ " AND b." +tablas.ID_LISTAS+ " = " +"'"+list_fecha.getID()+"'"
                + " WHERE a."+tablas.CAMPO_ESTADO +"= 1";
        try {
            Cursor cursor=db.rawQuery(consulta,null);
            while (cursor.moveToNext()){
                arti = new articulos();
                arti.setID(cursor.getInt(0));
                arti.setNombre(cursor.getString(1));
                arti.setActive(cursor.getString(2).equals("0")?false:true);
                listaarticulos.add(arti);
            }
        }catch (Exception e){
            String A =e.toString();
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

    public ArrayList<Integer> articulosSeleccionados()  {

        SparseBooleanArray sp = listViewarticulos.getCheckedItemPositions();

        ArrayList<Integer> sb= new ArrayList<Integer>();

        for(int i=0;i<sp.size();i++){
            if(sp.valueAt(i)==true){
                articulos ARTIC= (articulos) listViewarticulos.getItemAtPosition(i);
                int s= ARTIC.getID();
                  sb.add(s);
            }
        }
        return sb;
    }
    public void registrarFechaARTICULO(ArrayList<Integer> L_A, long IDfecha){
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        String insert="INSERT INTO " + tablas.TABLA_ARTI_LIST + "( "+tablas.ID_ARTICULOS+ ","+tablas.ID_LISTAS+","+tablas.CAMPO_ESTADO_ARTI+") VALUES";
        String valuesSTR="";
        for(int i=0;i<L_A.size();i++){
            //values.put(tablas.ID_ARTICULOS,L_A.get(i).toString() );
            //values.put(tablas.ID_LISTAS,IDfecha );
            //long id = db.insert(tablas.TABLA_ARTI_LIST, tablas.ID_REGISTRO_ARTI_LIST,values);
            String coma=i+1!=L_A.size()?",":";";
            valuesSTR+=" ("+L_A.get(i).toString()+","+IDfecha+","+0+")"+ coma;
            }
        insert += valuesSTR;
        db.execSQL(insert);
        Log.i("queries: ", insert);
        Toast.makeText(getApplicationContext(),"ARTICULOS ACTUALIZADOS: ", Toast.LENGTH_SHORT).show();
        db.close();
    }
    private  void registroUpdate(){
        SQLiteDatabase db = conn.getWritableDatabase();
        ArrayList<Integer> at =articulosSeleccionados();
        if(!nombreFecha.getText().toString().equals("") && at.size()>0){
            String[] parametro ={list_fecha.getID().toString()};
            ContentValues values = new ContentValues();
            values.put(tablas.CAMPO_FECHA, nombreFecha.getText().toString());

            db.update(tablas.TABLA_LISTAS,values, tablas.CAMPO_ID_LISTA+"=?",parametro);
            Toast.makeText(getApplicationContext(),"NOMBRE ACTUALIZADO: ", Toast.LENGTH_SHORT).show();


            EliminarArticulos();
            ArrayList<Integer> articulos =articulosSeleccionados();
            registrarFechaARTICULO(articulos,list_fecha.getID());
        }else {
            Toast.makeText(getApplicationContext(),"NO SE PUEDE ACTUALIZAR, NOMBRE O LISTA EN BLANCO ", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }
   private  void EliminarArticulos(){
       SQLiteDatabase db = conn.getWritableDatabase();
       String[] parametro ={list_fecha.getID().toString()};

       db.delete(tablas.TABLA_ARTI_LIST,tablas.ID_LISTAS+"=?",parametro);
       db.close();
   }
    private void redirect() {
        Intent intent=new Intent(RegistroCompra2.this,RegistroCompras.class);

        startActivity(intent);
    }
}
