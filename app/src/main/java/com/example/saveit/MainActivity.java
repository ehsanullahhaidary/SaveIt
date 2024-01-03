package com.example.saveit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    // Initialize variables
    Button btAdd;
    Button btnRemove;
    EditText etText;
    ListView listView;

    DatabaseHelper databaseHelper;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign variables
        etText = findViewById(R.id.et_text);
        btAdd = findViewById(R.id.bt_add);
        btnRemove = findViewById(R.id.bt_remove);
        listView = findViewById(R.id.list_view);


        //Initialize databaseHelper
        databaseHelper = new DatabaseHelper(MainActivity.this);

        //Add database values to arrayList
        arrayList = databaseHelper.getAllText();


        Collections.sort(arrayList);


        //Initialize Array Adapter
        arrayAdapter = new ArrayAdapter(MainActivity.this,
                android.R.layout.simple_list_item_multiple_choice, arrayList) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);

                // Get the Layout Parameters for ListView Current Item View
                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = listView.getMinimumHeight();
                view.setLayoutParams(params);


                return view;
            }
        };

        // set arrayAdapter to listView
        listView.setAdapter(arrayAdapter);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from edit text
                String text = etText.getText().toString();
                if (!text.isEmpty()) {
                    if (databaseHelper.addText(text)) {
                        etText.setText("");//clear edit text
                        // display toast massage
                        Toast.makeText(getApplicationContext(), "Data Inserted...",
                                Toast.LENGTH_SHORT).show();
                        //clear arrayList
                        arrayList.clear();
                        arrayList.addAll(databaseHelper.getAllText());
                        //Refresh listView data
                        arrayAdapter.notifyDataSetChanged();
                        listView.invalidateViews();
                        listView.refreshDrawableState();
                        Collections.sort(arrayList);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Input is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnRemove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, String.valueOf(listView.getCheckedItemPosition()), Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "Data Deleted...",
                        Toast.LENGTH_SHORT).show();


                ArrayList<String> clone = new ArrayList<String>();

                SparseBooleanArray positionChecker = listView.getCheckedItemPositions();


                for (int i = listView.getCount(); i >= 0; i--) {
                    if (positionChecker.get(i)) {
                        arrayList.remove(i);
                    }
                }


                clone.addAll(arrayList);
                databaseHelper.removeText();
                for (int i = 0; i < clone.size(); i++) {
                    databaseHelper.addText(clone.get(i));
                }

                
                arrayAdapter.notifyDataSetChanged();

            }
        });
    }

}
