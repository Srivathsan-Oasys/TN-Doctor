package com.oasys.digihealth.doctor.component.extention

import android.app.Activity
import android.content.ContextWrapper
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.oasys.digihealth.doctor.view.dp

fun AppCompatImageView.loadImage(imageUrl: String?, radius: Int = 0, placeholder: Int = 0) {

    val drawable =
        if (placeholder > 0) context.getCompatDrawable(placeholder) else null
    if (imageUrl == null) {
        setImageDrawable(drawable)
        return
    }
    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
    if (radius > 0)
        options.transform(CenterCrop(), RoundedCorners(dp(radius)))
    when (context) {
        is Activity ->
            if (!(context as AppCompatActivity).isFinishing)
                Glide.with(this.context)
                    .load(imageUrl)
                    .placeholder(drawable)
                    .error(drawable)
                    .apply(options)
                    .into(this)
        is ContextWrapper ->
            Glide.with(this.context)
                .load(imageUrl)
                .placeholder(drawable)
                .error(drawable)
                .apply(options)
                .into(this)
    }
}

fun AppCompatImageView.loadImage(@DrawableRes drawableId: Int, radius: Int, placeholder: Int = 0) {
    val drawable =
        if (placeholder > 0) context.getCompatDrawable(placeholder) else null
    if (drawableId == 0) {
        setImageDrawable(drawable)
        return
    }
    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
    if (radius > 0)
        options.transform(CenterCrop(), RoundedCorners(dp(radius)))
    when (context) {
        is Activity ->
            if (!(context as AppCompatActivity).isFinishing)
                Glide.with(this.context)
                    .load(drawableId)
                    .placeholder(drawable)
                    .error(drawable)
                    .apply(options)
                    .into(this)
        is ContextWrapper ->
            Glide.with(this.context)
                .load(drawableId)
                .placeholder(drawable)
                .error(drawable)
                .apply(options)
                .into(this)
    }
}

fun AppCompatImageView.loadCircleImage(imageUrl: String?, placeholder: Int = 0) {
    val drawable =
        if (placeholder > 0) context.getCompatDrawable(placeholder) else null
    if (imageUrl == null) {
        setImageDrawable(drawable)
        return
    }
    val options = RequestOptions.circleCropTransform()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
    when (context) {
        is Activity ->
            if (!(context as AppCompatActivity).isFinishing)
                Glide.with(this.context)
                    .load(imageUrl)
                    .placeholder(drawable)
                    .error(drawable)
                    .apply(options)
                    .into(this)
        is ContextWrapper ->
            Glide.with(this.context)
                .load(imageUrl)
                .placeholder(drawable)
                .error(drawable)
                .apply(options)
                .into(this)
    }
}

