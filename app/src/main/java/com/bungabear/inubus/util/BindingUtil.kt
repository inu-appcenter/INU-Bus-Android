package com.bungabear.inubus.util

import android.view.ViewGroup.MarginLayoutParams
import android.databinding.BindingAdapter
import android.view.View


/**
 * Created by Minjae Son on 2018-08-09.
 */

@BindingAdapter("android:layout_marginTop")
fun setTopMargin(view: View, topMargin: Float) {
    val layoutParams = view.getLayoutParams() as MarginLayoutParams
    layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
            layoutParams.rightMargin, Math.round(topMargin))
    view.setLayoutParams(layoutParams)
}

@BindingAdapter("android:layout_marginBottom")
fun setBottomMargin(view: View, bottomMargin: Float) {
    val layoutParams = view.getLayoutParams() as MarginLayoutParams
    layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
            layoutParams.rightMargin, Math.round(bottomMargin))
    view.setLayoutParams(layoutParams)
}