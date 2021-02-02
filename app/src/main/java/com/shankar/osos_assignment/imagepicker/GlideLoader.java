package com.shankar.osos_assignment.imagepicker;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaiky.imagespickers.ImageLoader;
import com.shankar.osos_assignment.R;

public class GlideLoader implements ImageLoader {

	private static final long serialVersionUID = 1L;

	@Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.global_img_default)
                .centerCrop()
                .into(imageView);
    }

}
