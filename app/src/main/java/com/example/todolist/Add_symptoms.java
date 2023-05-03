package com.example.todolist;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.data.ListContract;
import com.example.todolist.data.ListDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Add_symptoms extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private  EditText name;
    private ListDbHelper mDbHelper;
   private String[] list_symptoms={"Cough", "Fever", "HearAttack", "Siezures","Fatigue","Pain","Abdominal ","Blood","Yellow Urine","goitre",};
    private ArrayList<item_model> courseModelArrayList;
    private  String symptoms="";
    private boolean mListChange = false;
    GridView coursesGV;

    private CheckBox checkbox;
    private Uri List_Uri;
    private static final int BOOK_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_symptoms);

        final Calendar myCalendar = Calendar.getInstance();


        name= (EditText) findViewById(R.id.name);
         checkbox = findViewById(R.id.check_box);
        coursesGV = findViewById(R.id.idGVcourses);
     courseModelArrayList = new ArrayList<item_model>();

        for(int i=0;i<list_symptoms.length;i++)
        {

            courseModelArrayList.add(new item_model(list_symptoms[i],false));

        }
//        courseModelArrayList.add(new item_model("DSA"));
//        courseModelArrayList.add(new item_model("JAVA"));
//        courseModelArrayList.add(new item_model("C++"));
//        courseModelArrayList.add(new item_model("Python"));
//        courseModelArrayList.add(new item_model("Javascript"));
//        courseModelArrayList.add(new item_model("DSA"));

        Disease_Adapter adapter = new Disease_Adapter(this, courseModelArrayList);
        coursesGV.setAdapter(adapter);

     Button addlist=(Button)findViewById(R.id.addtodo_butt);
        Intent intent = getIntent();
        mDbHelper=new ListDbHelper(this);
        List_Uri = intent.getData();
        if(List_Uri==null)
        {
            setTitle("Add");
            invalidateOptionsMenu();
        }
        else
        {
            setTitle("Edit ToDo");
            addlist.setText("Save Changes");
            getLoaderManager().initLoader(BOOK_LOADER, null, this);
        }
     addlist.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             Disease_Adapter myAdapter = new Disease_Adapter(Add_symptoms.this, courseModelArrayList);
              symptoms = myAdapter.getData();
            //  Toast.makeText(Add_symptoms.this, "Item clicked at position " + symptoms, Toast.LENGTH_SHORT).show();
             insert();
             Intent intent=new Intent(Add_symptoms.this,MainActivity.class);
             startActivity(intent);
         }
     });







    }
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mListChange = true;
            return false;
        }
    };




    private void insert() {
        String Name = name.getText().toString().trim();



        ContentValues values = new ContentValues();
        values.put(ListContract.ListEntry.NAME, Name);
        values.put(ListContract.ListEntry.SYMPTOMS, symptoms);


        if(List_Uri==null) {
            Uri newUri = getContentResolver().insert(
                    ListContract.ListEntry.CONTENT_URI,   // the pet content URI
                    values                  // the values to insert
            );
            if (newUri != null) {
                Toast.makeText(this, "successfull", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            int rowsAffected = getContentResolver().update(List_Uri, values, null, null);
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "editor_update_book_failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "editor_update_book_successful",
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }


    }
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mListChange) {
            super.onBackPressed();
            return;
        }
        else
        {
            showUnsavedChangesDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if(List_Uri==null)
        {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mListChange) {
                    finish();
                    return true;
                }
                else
                {
                    showUnsavedChangesDialog();
                    return true;
                }

        }

        return super.onOptionsItemSelected(item);
    }
    private void showUnsavedChangesDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Save Changes");
        builder.setPositiveButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Delete Todo");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                deletePet();

                }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deletePet() {
        if (List_Uri != null) {

            int rowsDeleted = getContentResolver().delete(List_Uri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "editor_delete_pet_failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "editor_delete_pet_successful", Toast.LENGTH_SHORT).show();
            }
           getContentResolver().notifyChange(List_Uri,null);
            finish();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ListContract.ListEntry._ID,
                ListContract.ListEntry.NAME,
                ListContract.ListEntry.SYMPTOMS,
                };

        return new CursorLoader(this,
                List_Uri,
                projection,
                null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String m_name = cursor.getString(cursor.getColumnIndex(ListContract.ListEntry.NAME));
            String symp = cursor.getString(cursor.getColumnIndex(ListContract.ListEntry.SYMPTOMS));


            name.setText(m_name);

        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        name.setText(" ");
    }


}
