package com.PrimeClassService.prepmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PrepMaster.db";
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private static final String SQL_CREATE_COURSES_TABLE =
            "CREATE TABLE Courses (" +
                    "CourseID INTEGER PRIMARY KEY," +
                    "CourseName TEXT)";

    private static final String SQL_CREATE_SLIDES_TABLE =
            "CREATE TABLE Slides (" +
                    "SlideID INTEGER PRIMARY KEY," +
                    "SlideName TEXT," +
                    "SlideType TEXT," +
                    "SlidePath TEXT)";

    private static final String SQL_CREATE_COURSE_SLIDES_TABLE =
            "CREATE TABLE CourseSlides (" +
                    "CourseID INTEGER," +
                    "SlideID INTEGER," +
                    "FOREIGN KEY (CourseID) REFERENCES Courses(CourseID)," +
                    "FOREIGN KEY (SlideID) REFERENCES Slides(SlideID)," +
                    "PRIMARY KEY (CourseID, SlideID))";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_COURSES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SLIDES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_COURSE_SLIDES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Courses");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Slides");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CourseSlides");
        onCreate(sqLiteDatabase);
    }

    public void addCourse(String courseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CourseName", courseName);
        db.beginTransaction();
        try {
            db.insert("Courses", null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void addSlide(String courseName, String slideName, String slideType, String slidePath) {
        SQLiteDatabase db = this.getWritableDatabase();

        try (Cursor cursor = db.query(
                "Courses",
                new String[]{"CourseID"},
                "CourseName = ?",
                new String[]{courseName},
                null,
                null,
                null
        )) {
            ContentValues values = new ContentValues();
            values.put("SlideName", slideName);
            values.put("SlideType", slideType);
            values.put("SlidePath", slidePath);

            long slideId = db.insert("Slides", null, values);

            if (cursor != null && cursor.moveToFirst() && slideId != -1) {
                long courseId = cursor.getLong(cursor.getColumnIndex("CourseID"));

                ContentValues courseSlideValues = new ContentValues();
                courseSlideValues.put("CourseID", courseId);
                courseSlideValues.put("SlideID", slideId);
                db.insert("CourseSlides", null, courseSlideValues);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(context,"Error: "+ e.getMessage(),Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    public ArrayList<CourseMod> getAllCourses() {
        ArrayList<CourseMod> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM Courses", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String courseName = cursor.getString(cursor.getColumnIndex("CourseName"));

                    // Create a CourseMod object and add it to the courses list
                    CourseMod course = new CourseMod(courseName);
                    courses.add(course);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // Handle exceptions or errors here
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return courses;
    }

    public ArrayList<SlideMod> getAllSlides(String courseName) {
        ArrayList<SlideMod> slides = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT s.* FROM Slides s INNER JOIN CourseSlides cs ON s.SlideID = cs.SlideID INNER JOIN Courses c ON cs.CourseID = c.CourseID WHERE c.CourseName = ?", new String[]{courseName});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long slideID = cursor.getLong(cursor.getColumnIndex("SlideID"));
                    String slideName = cursor.getString(cursor.getColumnIndex("SlideName"));
                    String slideType = cursor.getString(cursor.getColumnIndex("SlideType"));
                    String slidePath = cursor.getString(cursor.getColumnIndex("SlidePath"));

                    // Create a SlideMod object and add it to the slides list
                    SlideMod slide = new SlideMod(slideName, slidePath, slideType);
                    slides.add(slide);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // Handle exceptions or errors here
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return slides;
    }

}
