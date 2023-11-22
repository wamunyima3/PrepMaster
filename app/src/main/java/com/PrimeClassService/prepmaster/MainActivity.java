package com.PrimeClassService.prepmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCourses;
    private FloatingActionButton addCourse;
    private RVCousersAdapter rvAdapter;
    private DBHelper dbHelper;
    private ArrayList<CourseMod> courses;
    private TextView tvAddCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCourses = findViewById(R.id.rvCourses);
        addCourse = findViewById(R.id.fabAddCourse);
        tvAddCourses = findViewById(R.id.tvAddCourses);
        rvAdapter = new RVCousersAdapter(this);

        courses = new ArrayList<>();

        // Set the GridLayoutManager to the RecyclerView
        rvCourses.setLayoutManager(new GridLayoutManager(this,2));
        rvCourses.setAdapter(rvAdapter);

        dbHelper = new DBHelper(MainActivity.this);
        courses = dbHelper.getAllCourses();
        if(courses.size()>0){
            tvAddCourses.setVisibility(View.GONE);
        }else{
            tvAddCourses.setVisibility(View.VISIBLE);
        }
        rvAdapter.setCourses(courses);

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog to get the course name from the user
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter Course Name");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String courseName = input.getText().toString();

                        // Use your DBHelper to add the course
                        dbHelper.addCourse(courseName);

                        // Refresh the RecyclerView with the updated courses
                        courses = dbHelper.getAllCourses();
                        if(courses.size()>0){
                            tvAddCourses.setVisibility(View.GONE);
                        }else{
                            tvAddCourses.setVisibility(View.VISIBLE);
                        }
                        rvAdapter.setCourses(courses);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


    }
}