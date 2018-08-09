package com.bungabear.inubus.util

import android.support.v7.util.DiffUtil
import com.bungabear.inubus.model.ArrivalRecyclerItem

/**
 * Created by Minjae Son on 2018-08-08.
 */

class ArrivalInfoDiffUtil(private val mOldList : ArrayList<ArrivalRecyclerItem>, private val mNewList : ArrayList<ArrivalRecyclerItem>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].equals(mNewList[newItemPosition])
    }

    override fun getOldListSize(): Int = mOldList.size

    override fun getNewListSize(): Int = mNewList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = mOldList[oldItemPosition]
        val new = mNewList[newItemPosition]
        return if(old.itemType == ArrivalRecyclerItem.ItemType.ArrivalInfo)
             old.arrivalInfo!!.arrival == new.arrivalInfo!!.arrival
        else false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }



}