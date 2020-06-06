package com.paras.baseapplication.base.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

object GenericImageLoader {

    fun setImageView(
        imageView: ImageView,
        url: String?
    ) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }

}