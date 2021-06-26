package com.example.bai1c5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Author;
import com.example.model.Book;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class QuanLySach extends AppCompatActivity {
    ListView lvBookData;
    ArrayAdapter<Book> adapter;
    Spinner spinner;
    EditText edtBookName;
    TextView txtDate;
    Button btnDate,btnThemSach;
    ArrayAdapter<Author> authorList;
    Author selectedAuthor = XemDanhSachTacGia.list.get(0);
    Calendar calendarDate = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_sach);
        addControls();
        addEvents();
    }

    private void addEvents() {
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAuthor = authorList.getItem(position);
                adapter.clear();
                hienthiDanhsach();
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
         btnThemSach.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Random random = new Random();
                 int bookID = random.nextInt(999);
                 String bookName =  edtBookName.getText().toString();
                 String Date = txtDate.getText().toString();
                 int authorID = selectedAuthor.getAuthorID();
                 Book book = new Book(bookID,bookName,Date,authorID);

                 ContentValues values = new ContentValues();
                 values.put("bookID",bookID);
                 values.put("bookName",bookName);
                 values.put("DateOfPublication",Date);
                 values.put("authorID",authorID);
                 long result = MainActivity.database.insert("BookData",null,values);
                 if(result>0) {
                     Toast.makeText(QuanLySach.this, "Them thanh cong", Toast.LENGTH_LONG).show();
                 }
                 else {
                     Toast.makeText(QuanLySach.this, "Them that bai", Toast.LENGTH_LONG).show();
                 }

                 selectedAuthor.getBooks().add(book);
                 adapter.clear();
                 hienthiDanhsach();
                 edtBookName.setText("");
                 txtDate.setText("");
                 edtBookName.requestFocus();
             }
         });
         btnDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 XuLyNhapDate();
             }
         });
    }

    private void XuLyNhapDate() {
        DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarDate.set(Calendar.YEAR,year);
                calendarDate.set(Calendar.MONTH,month);
                calendarDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                txtDate.setText(simpleDateFormat.format(calendarDate.getTime()));
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(QuanLySach.this,
                callBack,
                calendarDate.get(Calendar.YEAR),
                calendarDate.get(Calendar.MONTH),
                calendarDate.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void addControls() {
        lvBookData = findViewById(R.id.lvBook);
        adapter = new ArrayAdapter<>(QuanLySach.this, android.R.layout.simple_list_item_1);
        lvBookData.setAdapter(adapter);


        spinner = findViewById(R.id.spinner);
        authorList = new ArrayAdapter<>(QuanLySach.this, android.R.layout.simple_spinner_item);
        authorList.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(authorList);


        edtBookName = findViewById(R.id.edtBookName);
        txtDate = findViewById(R.id.txtDate);
        btnDate = findViewById(R.id.btnDate);
        btnThemSach = findViewById(R.id.btnThemSach);
    }
     private void hienthiDanhsach() {
         MainActivity.database= openOrCreateDatabase(MainActivity.DATABASE_NAME_1,MODE_PRIVATE,null);
         Cursor cursor = MainActivity.database.rawQuery("select * from BookData where authorID = ?",
                 new String[]{String.valueOf(selectedAuthor.getAuthorID())});
         adapter.clear();
         while (cursor.moveToNext())
         {
             int bookID = cursor.getInt(0);
             String bookName = cursor.getString(1);
             String dateof = cursor.getString(2);
             int authorID = cursor.getInt(3);
             Book book = new Book(bookID,bookName,dateof,authorID);
             adapter.add(book);
         }
         cursor.close();
    }
    private void hienthiDanhsachTacGia() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = MainActivity.database.rawQuery("select * from Author",null);
        authorList.clear();
        while(cursor.moveToNext())
        {
            int author_id = cursor.getInt(0);
            String author_name = cursor.getString(1);
            Author author = new Author(author_id,author_name);
            authorList.add(author);
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hienthiDanhsach();
        hienthiDanhsachTacGia();
    }
}