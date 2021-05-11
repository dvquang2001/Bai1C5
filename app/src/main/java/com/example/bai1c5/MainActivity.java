package com.example.bai1c5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    public static String DATABASE_NAME ="mydata.sqlite";
    public static String DATABASE_NAME_1 ="Book.sqlite";
    public static String DB_PATH_SUFFIX = "/databases/";

    public static SQLiteDatabase database = null;

    Button btnXemds,btnThem,btnQuanLy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        processCopy();
        processCopy1();
        addControls();
        addEvents();
    }


    private void addEvents() {
        btnXemds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,XemDanhSachTacGia.class);
                startActivity(intent);
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoManHinhDialogThemTacGia();
            }
        });
        btnQuanLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuanLySach.class);
                startActivity(intent);
            }
        });
    }

    private void MoManHinhDialogThemTacGia() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.them_tgia);
        dialog.setTitle("Thêm mới tác giả");
        dialog.setCanceledOnTouchOutside(false);

        EditText edtAuthorID = dialog.findViewById(R.id.edtAuthorID);
        EditText edtAuthorName = dialog.findViewById(R.id.edtAuthorName);
        Button btnFocus = dialog.findViewById(R.id.btnFocus);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        btnFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAuthorID.setText("");
                edtAuthorName.setText("");
                edtAuthorID.requestFocus();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newID = Integer.parseInt(edtAuthorID.getText().toString());
                String newName = edtAuthorName.getText().toString();

                ContentValues values = new ContentValues();
                values.put("authorID",newID);
                values.put("authorName",newName);

                long result = database.insert("Author",null,values);
                if(result>0) {
                    Toast.makeText(MainActivity.this, "Them thanh cong", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(MainActivity.this, "Them that bai", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void addControls() {
        btnXemds = findViewById(R.id.btnXemds);
        btnThem = findViewById(R.id.btnThem);
        btnQuanLy = findViewById(R.id.btnQuanLy);
    }

    private void processCopy()
    {
        try {
            File dbFile = getDatabasePath(DATABASE_NAME);
            if (!dbFile.exists()) {
                CopyDataBaseFromAsset();
                Toast.makeText(MainActivity.this,"Sao chep thanh cong",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,ex.toString(),Toast.LENGTH_LONG).show();
            Log.e("ERROR: ",ex.toString());
        }
    }
    private String getDataBasePath()
    {

        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }


    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDataBasePath();
            File f = new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte [] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer))>0)
            {
                myOutput.write(buffer,0,length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex)
        {
            Log.e("EROOR: ",ex.toString());
        }
    }

    private void processCopy1() {
        try {
            File dbFile = getDatabasePath(DATABASE_NAME_1);
            if (!dbFile.exists()) {
                CopyDataBaseFromAsset1();
                Toast.makeText(MainActivity.this,"Sao chep thanh cong",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,ex.toString(),Toast.LENGTH_LONG).show();
            Log.e("ERROR: ",ex.toString());
        }
    }
    private String getDataBasePath1()
    {

        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME_1;
    }


    private void CopyDataBaseFromAsset1() {
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME_1);
            String outFileName = getDataBasePath1();
            File f = new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte [] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer))>0)
            {
                myOutput.write(buffer,0,length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex)
        {
            Log.e("EROOR: ",ex.toString());
        }
    }

}