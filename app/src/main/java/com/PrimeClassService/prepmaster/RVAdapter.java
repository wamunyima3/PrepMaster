package com.PrimeClassService.prepmaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{
    private ArrayList<CourseMod> courses = new ArrayList<>();
    private Context context;
    public RVAdapter(Context context) {
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

            // Optionally, you can set a click listener for the item view here
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle item click event here if needed
                    Toast.makeText(context, courses.get(position).getCourseName(), Toast.LENGTH_LONG).show();
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
