package com.PrimeClassService.prepmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Slide extends AppCompatActivity {

    private RecyclerView rvSlides;
    private FloatingActionButton addSlide;
    private DBHelper dbHelper;
    private String courseName;
    private RVSlideAdapter rvSlideAdapter;
    private ArrayList<SlideMod> slides;

    // Request code for file picker intent
    private static final int FILE_PICKER_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        rvSlides = findViewById(R.id.rvSlides);
        addSlide = findViewById(R.id.fabAddSlide);
        rvSlideAdapter = new RVSlideAdapter(this);
        slides = new ArrayList<>();

        courseName = getIntent().getStringExtra("CourseName"); // Set course name first

        dbHelper = new DBHelper(this);
        slides = dbHelper.getAllSlides(courseName); // Get slides based on the course name
        rvSlideAdapter.setSlides(slides);

        // Set the LinearLayoutManager to the RecyclerView
        rvSlides.setLayoutManager(new LinearLayoutManager(this));
        rvSlides.setAdapter(rvSlideAdapter);

        addSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open a file picker or system's file manager
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");  // You can set specific mime types here if needed
                startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected file
            Uri selectedFileUri = data.getData();

            // Get the file path from the URI
            String filePath = selectedFileUri.getPath();
            // Get the file name
            String fileName = getFileName(selectedFileUri);
            // Get the file extension
            String fileExtension = getFileExtension(filePath);

            dbHelper.addSlide(courseName, fileName, fileExtension, filePath);
            slides = dbHelper.getAllSlides(courseName);
            rvSlideAdapter.setSlides(slides);
        }
    }

    // Helper method to get file name from URI
    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            String name = cursor.getString(nameIndex);
            cursor.close();
            return name;
        }
        return null;
    }

    // Helper method to get file extension from file path
    private String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }
}

