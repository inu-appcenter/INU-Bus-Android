package com.inu.bus.util

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Minjae Son on 2018-08-09.
 */

// LayoutMargin 은 데이터바인딩에서 dp값을 적용하지 못해서 바인딩 어댑터로 override
@BindingAdapter("android:layout_marginTop")
fun setTopMargin(view: View, topMargin: Float) {
    val layoutParams = view.layoutParams as MarginLayoutParams
    layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
            layoutParams.rightMargin, Math.round(topMargin))
    view.layoutParams = layoutParams
}

@BindingAdapter("android:layout_marginBottom")
fun setBottomMargin(view: View, bottomMargin: Float) {
    val layoutParams = view.layoutParams as MarginLayoutParams
    layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
            layoutParams.rightMargin, Math.round(bottomMargin))
    view.layoutParams = layoutParams
}

@BindingConversion
fun millisToDate(millis : Long) : String{
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    return formatter.format(calendar.time)
}