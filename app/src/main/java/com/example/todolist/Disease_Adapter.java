package com.example.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Disease_Adapter extends ArrayAdapter<item_model> {
    private static ArrayList<Boolean> mChecked;
    private  static String symptoms="";
        public Disease_Adapter(@NonNull Context context, ArrayList<item_model> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
            mChecked = new ArrayList<Boolean>(Collections.nCopies(courseModelArrayList.size(), false));

        }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);
        }

        item_model courseModel = getItem(position);
        TextView disease = listitemView.findViewById(R.id.idTVCourse);


        disease.setText(courseModel.getDisease());
        CheckBox checkbox = (CheckBox) listitemView.findViewById(R.id.check_box);
        checkbox.setChecked(courseModel.isCheck());

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the isChecked value of the corresponding Item object to true
                courseModel.setCheck(isChecked);
            }
        });

        return listitemView;
    }
    public String getData() {
     symptoms=" ";
    for (int i = 0; i < getCount(); i++) {
                         if(getItem(i).getCheck()) {
                             symptoms+= getItem(i).getDisease()+" ";
                         }
        }
                Toast.makeText(getContext(), "Item clicked at position " + symptoms, Toast.LENGTH_SHORT).show();
        return symptoms;
    }


    }

