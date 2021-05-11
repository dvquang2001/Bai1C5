package com.example.bai1c5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Author;

import java.util.ArrayList;

public class XemDanhSachTacGia extends AppCompatActivity {
    ListView lvAuthor;
    ArrayAdapter<Author> adapter;
    Author selectedAuthor = null;
    public static ArrayList<Author> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_danh_sach_tac_gia);
        lvAuthor = findViewById(R.id.lvAuthor);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvAuthor.setAdapter(adapter);


        lvAuthor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAuthor = adapter.getItem(position);
                Dialog dialog = new Dialog(XemDanhSachTacGia.this);
                dialog.setContentView(R.layout.xoa_hoac_sua);
                dialog.setTitle("Option");
                dialog.setCanceledOnTouchOutside(false);

                Button btnSuatgia = dialog.findViewById(R.id.btnSuatgia);
                Button btnXoatgia = dialog.findViewById(R.id.btnXoatgia);

                btnSuatgia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Author author = adapter.getItem(position);
                        SuaTacGia();
                    }
                });
                btnXoatgia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XoaTacGia();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    private void XoaTacGia() {
        Dialog dialogXoa = new Dialog(XemDanhSachTacGia.this);
        dialogXoa.setCanceledOnTouchOutside(false);
        dialogXoa.setTitle("remove");
        dialogXoa.setContentView(R.layout.xoa_tgia);
        TextView txtNoidung = dialogXoa.findViewById(R.id.txtNoidung);
        Button btnXoa = dialogXoa.findViewById(R.id.btnXoa);
        Button btnKhongXoa = dialogXoa.findViewById(R.id.btnKhongXoa);
        txtNoidung.setText("Ban co muon xoa thong tin ve "+selectedAuthor.getAuthorName());
        btnKhongXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogXoa.dismiss();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = MainActivity.database.delete(
                        "Author",
                        "authorID=?",
                        new String[]{String.valueOf(selectedAuthor.getAuthorID())});
                if(result>0)
                {
                    Toast.makeText(XemDanhSachTacGia.this,"Xoa thanh cong",Toast.LENGTH_LONG).show();
                    dialogXoa.dismiss();
                }
                else {
                    Toast.makeText(XemDanhSachTacGia.this,"Xoa that bai",Toast.LENGTH_LONG).show();
                    dialogXoa.dismiss();
                }
            }
        });
        dialogXoa.show();

    }

    private void SuaTacGia() {
        Dialog dialogSua = new Dialog(XemDanhSachTacGia.this);
        dialogSua.setContentView(R.layout.chinh_sua_tgia);
        dialogSua.setTitle("Chinh sua");

        EditText edtAuthorNameFix = dialogSua.findViewById(R.id.edtAuthorNameFix);
        Button btnFocusFix = dialogSua.findViewById(R.id.btnFocusFix);
        Button btnUpdate = dialogSua.findViewById(R.id.btnUpdate);

        btnFocusFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAuthorNameFix.setText("");
                edtAuthorNameFix.requestFocus();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("authorName",edtAuthorNameFix.getText().toString());
                int result = MainActivity.database.update("Author",values,
                        "authorID=?",new String[]{String.valueOf(selectedAuthor.getAuthorID())});
                if(result>0)
                    Toast.makeText(XemDanhSachTacGia.this,"Cap nhat thanh cong",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(XemDanhSachTacGia.this,"Cap nhat that bai",Toast.LENGTH_LONG).show();
            }
        });
        dialogSua.show();
    }

    private void hienthiDanhsach() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = MainActivity.database.rawQuery("select * from Author",null);
        adapter.clear();
        while(cursor.moveToNext())
        {
            int author_id = cursor.getInt(0);
            String author_name = cursor.getString(1);
            Author author = new Author(author_id,author_name);
            adapter.add(author);
            list.add(author);
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hienthiDanhsach();
    }
}