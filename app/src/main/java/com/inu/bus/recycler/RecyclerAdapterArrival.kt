package com.inu.bus.recycler

import android.databinding.ObservableBoolean
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.inu.bus.databinding.RecyclerArrivalHeaderBinding
import com.inu.bus.databinding.RecyclerArrivalItemBinding
import com.inu.bus.databinding.RecyclerArrivalSeparatorBinding
import com.inu.bus.model.BusArrivalInfo
import com.inu.bus.model.RecyclerArrivalItem
import com.inu.bus.util.ArrivalInfoDiffUtil
import com.inu.bus.util.Singleton
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Minjae Son on 2018-08-07.
 */

class RecyclerAdapterArrival(val mStrBusStop : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var isShowing = ObservableBoolean(false)
    private val mArrivalItems = ArrayList<RecyclerArrivalItem>()
    private var mFilteredItems = ArrayList<RecyclerArrivalItem>()
    private var mFilteringString = ""

    init {
        // Header
        mArrivalItems.add(RecyclerArrivalItem())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val type = RecyclerArrivalItem.ItemType.findByOrdinal(viewType)
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(type){
            RecyclerArrivalItem.ItemType.Header->
                ViewHolderArrivalHeader(RecyclerArrivalHeaderBinding.inflate(layoutInflater, parent, false))
            RecyclerArrivalItem.ItemType.SectionHeader->
                ViewHolderArrivalSection(RecyclerArrivalSeparatorBinding.inflate(layoutInflater, parent, false))
            RecyclerArrivalItem.ItemType.ArrivalInfo->
                ViewHolderArrivalItem(RecyclerArrivalItemBinding.inflate(layoutInflater, parent, false), isShowing)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(mFilteredItems[position].itemType){
            RecyclerArrivalItem.ItemType.Header->{ }
            RecyclerArrivalItem.ItemType.SectionHeader->{
                (holder as ViewHolderArrivalSection).bind(mFilteredItems[position].sectionHeader!!)
            }
            RecyclerArrivalItem.ItemType.ArrivalInfo->{
                (holder as ViewHolderArrivalItem).bind(mFilteredItems[position].arrivalInfo!!)
            }
        }
    }

    override fun getItemCount(): Int = mFilteredItems.size
    override fun getItemViewType(position: Int): Int = mFilteredItems[position].itemType.ordinal

    fun applyDataSet(items: ArrayList<BusArrivalInfo>) {
        mArrivalItems.clear()
        val sorted = items.sortedWith(Comparator { o1, o2 ->
            when{
                (o1.type != o2.type) -> o1.type!!.ordinal - o2.type!!.ordinal
                else -> o1.no.compareTo(o2.no)
            }
        })

        val grouped  = sorted.groupBy { it.type }
        grouped.forEach { group ->
            // 현재 필요한 섹션 헤더만 추가
            mArrivalItems.add(RecyclerArrivalItem(group.key!!.value))
            group.value.forEach {
                mArrivalItems.add(RecyclerArrivalItem(it))
            }
        }
        mArrivalItems.add(0,RecyclerArrivalItem())
        filter()
    }

    fun filter(str : String = mFilteringString) {
        mFilteringString = str
        val filtered =
        // 검색 취소
                if(str == ""){
                    mArrivalItems
                }
                else {
                    ArrayList(
                            mArrivalItems.filter { item ->
                                if (item.itemType == RecyclerArrivalItem.ItemType.ArrivalInfo){
                                    !Singleton.busInfo.get()!![item.arrivalInfo!!.no]
                                            ?.nodeList
                                            ?.find{
                                                it.contains(str)
                                            }.isNullOrEmpty()
                                }
                                else false
                            }
                    )
                }
        val diffUtil = ArrivalInfoDiffUtil(mFilteredItems, filtered)
        val result = DiffUtil.calculateDiff(diffUtil)
        mFilteredItems.clear()
        mFilteredItems.addAll(filtered)
        result.dispatchUpdatesTo(this)
    }

    // 아이템이 화면에 보일때만 Ticker가 작동하도록 설정
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if(holder is ViewHolderArrivalItem){
            if(isShowing.get()){
                holder.startTick()
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if(holder is ViewHolderArrivalItem){
            holder.stopTick()
        }
    }
}