package com.example.viniloapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.viniloapp.R

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        Glide.with(view.context)
            .load(url)
            .apply(
               RequestOptions()
                   .placeholder(R.drawable.placeholder_image)
                   .diskCacheStrategy(DiskCacheStrategy.ALL)
                   .error(R.drawable.placeholder_image)
            )
            .into(view)
    }
}
