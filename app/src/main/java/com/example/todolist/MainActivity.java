package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.todolist.data.ListContract;
import com.example.todolist.data.ListDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.todolist.data.ListContract.ListEntry;
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
private TextView t;
private ImageButton star;
private ListCursorAdapter mcursorAdapter;
   private ListDbHelper mDbHelper;
    private static final int BOOK_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.floating_add);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Add_symptoms.class);
                startActivity(intent);
            }
        });

        ListView displayView = (ListView) findViewById(R.id.list_view);
        mcursorAdapter = new ListCursorAdapter(this, null);
        displayView.setAdapter(mcursorAdapter);

        mDbHelper=new ListDbHelper(this);
        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
// TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, Add_symptoms.class);
                Uri contenturi = ContentUris.withAppendedId(ListEntry.CONTENT_URI, id);
                intent.setData(contenturi);
                startActivity(intent);

            }
        });
        getLoaderManager().initLoader(BOOK_LOADER, null, this);

    }





    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ListEntry._ID,
                ListEntry.NAME,
                ListEntry.SYMPTOMS,
               };

        return new CursorLoader(this,
                ListEntry.CONTENT_URI,
                projection,
                null,
                null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()) {
            mcursorAdapter.swapCursor(data);
        } else if(data != null && data.getCount() == 0) {
            mcursorAdapter.swapCursor(null);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mcursorAdapter.swapCursor(null);

    }
}
