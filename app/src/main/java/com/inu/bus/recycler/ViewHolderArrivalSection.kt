package com.inu.bus.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import com.inu.bus.databinding.RecyclerArrivalSeparatorBinding

/**
 * Created by Minjae Son on 2018-08-25.
 */
class ViewHolderArrivalSection(private val binding : RecyclerArrivalSeparatorBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(sectionText: String, isWideHeight: Boolean = false, needButton: Boolean = false){
        binding.tvSection.text = sectionText
        if(isWideHeight){
            val dp2px = binding.tvSection.height / 16.5f
            binding.tvSection.height = (dp2px * 23.5f).toInt()
        }
        if(needButton){
            binding.btnSection.visibility = View.VISIBLE
            binding.btnSection.text = "지도보기"
            binding.btnSection.setOnClickListener{
                it.context
            }
        }
    }
}