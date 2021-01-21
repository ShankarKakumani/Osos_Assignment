package com.shankar.osos_assignment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.shankar.osos_assignment.R;
import com.shankar.osos_assignment.model.ParentClass;

import java.util.List;


public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ProductViewHolder> {



    private final List<ParentClass> parentClassList;

    ImageInterface imageInterface;

    public ParentAdapter(ImageInterface imageInterface, List<ParentClass> parentClassList) {
        this.imageInterface = imageInterface;
        this.parentClassList = parentClassList;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_title, null);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ParentClass parentClass = parentClassList.get(position);
        holder.titleTv.setText(parentClass.getTitle());

        holder.imagesTitleRecycler.setHasFixedSize(true);
        holder.imagesTitleRecycler.setNestedScrollingEnabled(false);

        //LinearLayoutManager latestLinearLayout = new GridLayoutManager(act,3);
        LinearLayoutManager latestLinearLayout = new GridLayoutManager(holder.imagesTitleRecycler.getContext(), 2, GridLayoutManager.HORIZONTAL, false);


        holder.imagesTitleRecycler.setLayoutManager(latestLinearLayout);


        holder.titleImageButton.setOnClickListener(view -> {

            imageInterface.onImageClick(position);
        });
        ImagesAdapter imagesAdapter = new ImagesAdapter(parentClass.getImageList());
        holder.imagesTitleRecycler.setAdapter(imagesAdapter);


    }


    @Override
    public int getItemCount() {
        return parentClassList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv;
        ImageButton titleImageButton;
        RecyclerView imagesTitleRecycler;

        public ProductViewHolder(View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleTv);
            titleImageButton = itemView.findViewById(R.id.titleImageButton);
            imagesTitleRecycler = itemView.findViewById(R.id.imagesTitleRecycler);
        }
    }
}
