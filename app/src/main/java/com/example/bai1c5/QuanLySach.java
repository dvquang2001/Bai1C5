package com.example.bai1c5;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.model.Author;
import com.example.model.Book;

import java.util.Random;


public class QuanLySach extends AppCompatActivity {
    ListView lvBookData;
    ArrayAdapter<Book> adapter;
    Spinner spinner;
    EditText edtBookName,edtDateOfPublication;
    Button btnDate,btnThemSach;
    ArrayAdapter<Author> authorList;
    Author selectedAuthor = XemDanhSachTacGia.list.get(0);
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
                for(int i=0;i<adapter.getCount();i++)
                {
                    selectedAuthor.getBooks().add(adapter.getItem(i));
                }
                adapter.clear();
                adapter.addAll(selectedAuthor.getBooks());
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
                 String Date = edtDateOfPublication.getText().toString();
                 int authorID = selectedAuthor.getAuthorID();
                 Book book = new Book(bookID,bookName,Date,authorID);
                 for(int i=0;i<adapter.getCount();i++)
                 {
                     selectedAuthor.getBooks().add(adapter.getItem(i));
                 }
                 selectedAuthor.getBooks().add(book);
                 adapter.clear();
                 adapter.addAll(selectedAuthor.getBooks());

             }
         });
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
        edtDateOfPublication = findViewById(R.id.edtDateOfPublication);
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