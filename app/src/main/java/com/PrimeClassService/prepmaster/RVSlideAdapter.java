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

public class RVSlideAdapter extends RecyclerView.Adapter<RVSlideAdapter.ViewHolder>{

    private ArrayList<SlideMod> slides = new ArrayList<>();
    private Context context;
    public RVSlideAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RVSlideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide,parent, false);
        return new RVSlideAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVSlideAdapter.ViewHolder holder, int position) {
        if (slides != null && position < slides.size()) {
            SlideMod currentCourse = slides.get(position);
            holder.slideName.setText(currentCourse.getSlideName());

            // Set click listener for item view
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the clicked course name
                    String clickedCourseName = slides.get(position).getSlideName();
                    Toast.makeText(context,clickedCourseName,Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return slides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView slideName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slideName = itemView.findViewById(R.id.slideName);
        }
    }

    public void setSlides(ArrayList<SlideMod> slides) {
        this.slides = slides;
        notifyDataSetChanged();
    }
}