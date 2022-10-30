package com.udacity.asteroidradar.common

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.models.NasaImage

@BindingAdapter("nasaImage")
fun bindNasaImage(imageView: ImageView, image: NasaImage?) {
    if (image != null) {
        if (image.media_type == "image") {
            Picasso.get().load(image.url.toUri()).into(imageView)
            imageView.contentDescription = image.title
        } else {
            imageView.contentDescription = image.title
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription =
            String.format(context.getString(R.string.potentially_hazardous_asteroid_image))
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription =
            String.format(context.getString(R.string.not_hazardous_asteroid_image))
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription =
            String.format(context.getString(R.string.potentially_hazardous_asteroid_image))
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription =
            String.format(context.getString(R.string.not_hazardous_asteroid_image))
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("progressBarVisibility")
fun bindLoadingVisibility(progressBar: ProgressBar, visibility: Constants.Visibility) {
    when (visibility) {
        Constants.Visibility.VISIBLE -> {
            progressBar.visibility = ProgressBar.VISIBLE
        }
        Constants.Visibility.GONE -> {
            progressBar.visibility = ProgressBar.GONE
        }
        Constants.Visibility.INVISIBLE -> {
            progressBar.visibility = ProgressBar.INVISIBLE
        }
    }
}
