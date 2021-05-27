package com.example.parcial1;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.parcial1.tablas.tablas;

public class AppSQLiteOpenHepler extends SQLiteOpenHelper {


    public AppSQLiteOpenHepler(Context context, String DATABASE_NAME, SQLiteDatabase.CursorFactory factory, int DATABASE_VERSION){
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(tablas.CREAR_TABLA_ARTICULOS);
        db.execSQL(tablas.CREAR_TABLA_LISTAS);
        db.execSQL(tablas.CREAR_TABLA_ARTI_LIST);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        try {
            db.execSQL("DROP TABLE IF EXISTS "+ tablas.CREAR_TABLA_ARTICULOS);
            db.execSQL("DROP TABLE IF EXISTS " + tablas.CREAR_TABLA_LISTAS);
            db.execSQL("DROP TABLE IF EXISTS " + tablas.CREAR_TABLA_ARTI_LIST);
            onCreate(db);
        }catch (SQLException e){
            // exepciones
        }
    }


}
