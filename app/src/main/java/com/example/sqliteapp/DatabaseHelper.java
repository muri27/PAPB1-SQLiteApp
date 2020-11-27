package com.example.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Konstanta untuk kebutuhan create DB
    public static String DATABASE_NAME="student_db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_STUDENTS="students";
    //Konstanta nama2 kolom
    private static final String KEY_ID="id";
    private static final String KEY_NAMA="nama";
    private static final String KEY_ALAMAT="alamt";
    //Konstanta bikin tabel baru
    private static final String CREATE_TABLE_STUDENTS=
            "CREATE TABLE "+TABLE_STUDENTS
            +"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +KEY_NAMA+" TEXT," +KEY_ALAMAT+" TEXT)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Membuat tabel baru dari konstanta di atas
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
    }

    //Menghapus tabel jika ada untuk keperluan update tabel
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS '"+TABLE_STUDENTS+"'");
    }

    //Method untuk menambahkan siswa baru, param disesuaikan kebutuhan
    public long addStudent(String nama, String alamt){
        SQLiteDatabase db = this.getWritableDatabase();
        //Tambah row baru di tabel
        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, nama);
        values.put(KEY_ALAMAT, alamt);

        //Masukkan data row ke tabel
        long insert=db.insert(TABLE_STUDENTS, null, values);
        return insert;
    }

    //Mengambil data dari tabel (SELECT SQL)
    //Map menggantikan objek
    public ArrayList<Map<String,String>> getAllStudents(){
        ArrayList<Map<String,String>> arrayList=new ArrayList<>();
        String nama="";
        String alamt="";
        int id=0;
        String selectQuery="SELECT * FROM "+TABLE_STUDENTS;
        SQLiteDatabase db=this.getWritableDatabase();
        //Memasukkan query + inisiasi cursor untuk membaca
        Cursor cursor=db.rawQuery(selectQuery, null);
        //Pindahkan cursor ke row pertama
        if(cursor.moveToFirst()){
            do {
                //Mendapatkan id di index tertentu
                id=cursor.getInt(cursor.getColumnIndex(KEY_ID));
                //Membaca konten menggunakan id
                nama=cursor.getString(cursor.getColumnIndex(KEY_NAMA));
                alamt=cursor.getString(cursor.getColumnIndex(KEY_ALAMAT));
                Map<String,String> itemMap=new HashMap<>();
                //Memasukkan data yg dibaca ke Map dan diubah ke ArrayList
                itemMap.put(KEY_ID,id+"");
                itemMap.put(KEY_NAMA,nama);
                itemMap.put(KEY_ALAMAT,alamt);
                arrayList.add(itemMap);
            }while (cursor.moveToNext()); //Pengecekan jika sudah sampai row terakhir akan berhenti
        }
        return arrayList;
    }

    //Method Delete Row di Table
    public void delete(int id){
    SQLiteDatabase db=this.getWritableDatabase();
    String deleteQuery="DELETE FROM "+TABLE_STUDENTS
            +" WHERE "+KEY_ID+"='"+id+"'";
    db.execSQL(deleteQuery);
    db.close();
    }
}
