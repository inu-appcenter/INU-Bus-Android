package com.inu.bus.recycler

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import com.inu.bus.R
import com.inu.bus.activity.MainActivity
import com.inu.bus.custom.IconPopUp
import com.inu.bus.databinding.RecyclerArrivalBitzonSeparatorBinding

/**
 * Created by Minjae Son on 2018-08-25.
 */
class ViewHolderArrivalBitZonSection(
        private val mBinding : RecyclerArrivalBitzonSeparatorBinding
        ,
        private val mContext : Context = mBinding.root.context) : RecyclerView.ViewHolder(mBinding.root) {

    fun bind(sectionText: String, needButton: Boolean = false){
        mBinding.tvSection.text = sectionText
        if(needButton){
            mBinding.btnSection.visibility = View.VISIBLE
            mBinding.btnSection.text = "지도보기"
            mBinding.btnSection.setOnClickListener{
                val popupView = IconPopUp(mContext)
                        .setBtnVisible(View.GONE)
                        .setIconSizeDp(86f)
                        .setIconBackgroundColor(0xfffeeb7f.toInt())
                        .setDismissOnBackgroundTouch(true)
                        .setDimBlur(MainActivity.mWrBlurringView2.get()!!)
                        .setIconText("MAP")
                        .setIconTextColor(0xffffffff.toInt())
                        .setMessageBackground(R.drawable.bg_bitzon_map, false ,true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(mContext is Activity){
                        popupView.setWindow(mContext.window)
                    }
                }
                popupView.show()
            }
        }
    }

}