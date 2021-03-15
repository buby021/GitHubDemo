package com.vidovicbranimir.githubdemo.data

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String) {
    Picasso.get().load(url).transform(CropCircleTransformation()).into(this)
}

