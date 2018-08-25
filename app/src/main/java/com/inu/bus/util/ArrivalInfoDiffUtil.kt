package com.inu.bus.util

import android.support.v7.util.DiffUtil
import com.inu.bus.model.RecyclerArrivalItem

/**
 * Created by Minjae Son on 2018-08-08.
 * 도착정보 RecyclerItems에 대하여 추가, 삭제, 데이터 변경의 적용을 계산해주는 유틸
 */

class ArrivalInfoDiffUtil(private val mOldList : ArrayList<RecyclerArrivalItem>, private val mNewList : ArrayList<RecyclerArrivalItem>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].equals(mNewList[newItemPosition])
    }

    override fun getOldListSize(): Int = mOldList.size

    override fun getNewListSize(): Int = mNewList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = mOldList[oldItemPosition]
        val new = mNewList[newItemPosition]
        return if(old.itemType == RecyclerArrivalItem.ItemType.ArrivalInfo)
             old.arrivalInfo!!.arrival == new.arrivalInfo!!.arrival
        else false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }



}