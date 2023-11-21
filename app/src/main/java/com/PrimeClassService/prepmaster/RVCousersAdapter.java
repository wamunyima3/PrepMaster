package com.PrimeClassService.prepmaster;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVCousersAdapter extends RecyclerView.Adapter<RVCousersAdapter.ViewHolder>{
    private ArrayList<CourseMod> courses = new ArrayList<>();
    private Context context;
    public RVCousersAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (courses != null && position < courses.size()) {
            CourseMod currentCourse = courses.get(position);
            holder.courseName.setText(currentCourse.getCourseName());

            // Set click listener for item view
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the clicked course name
                    String clickedCourseName = courses.get(position).getCourseName();

                    // Open CourseActivity and pass the clicked course name
                    Intent intent = new Intent(context, Slide.class);
                    intent.putExtra("CourseName", clickedCourseName);
                    context.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView courseName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
        }
    }

    public void setCourses(ArrayList<CourseMod> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }
}
