package com.bungabear.inubus.adapter

import android.content.Intent
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bungabear.inubus.R
import com.bungabear.inubus.activity.RouteActivity
import com.bungabear.inubus.databinding.RecyclerBusinfoItemBinding
import com.bungabear.inubus.fragment.ArrivalFragment
import com.bungabear.inubus.model.ArrivalInfo
import com.bungabear.inubus.model.ArrivalRecyclerItem
import com.bungabear.inubus.util.ArrivalInfoDiffUtil
import com.bungabear.inubus.util.Singleton
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Minjae Son on 2018-08-07.
 */

class ArrivalRecyclerAdapter(private val mStrStopName : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mArrivalItems = ArrayList<ArrivalRecyclerItem>()

//    private val mSectionHeaderMap = mutableMapOf<ArrivalInfo.BusType, Int>()

    init {
        // Column Header
        mArrivalItems.add(ArrivalRecyclerItem())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val type = ArrivalRecyclerItem.ItemType.findByOridinal(viewType)
        return when(type){
            ArrivalRecyclerItem.ItemType.Header->{
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_businfo_header, parent, false)
                HeaderHolder(v)
            }
            ArrivalRecyclerItem.ItemType.SectionHeader->{
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_businfo_seperator, parent, false)
                SeperatorHolder(v)

            }
            ArrivalRecyclerItem.ItemType.ArrivalInfo->{
                ItemViewHolder(RecyclerBusinfoItemBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(mArrivalItems[position].itemType){
            ArrivalRecyclerItem.ItemType.Header->{ }
            ArrivalRecyclerItem.ItemType.SectionHeader->{
                (holder as SeperatorHolder).bind(mArrivalItems[position].sectionHeader!!)
            }
            ArrivalRecyclerItem.ItemType.ArrivalInfo->{
                (holder as ItemViewHolder).bind(mArrivalItems[position].arrivalInfo!!)
            }
        }

    }

    override fun getItemCount(): Int = mArrivalItems.size
    override fun getItemViewType(position: Int): Int = mArrivalItems[position].itemType.ordinal

    fun addSectionHeader(type : ArrivalInfo.BusType){
//        if(mSectionHeaderMap[type] == null){
        mArrivalItems.add(ArrivalRecyclerItem(sectionHeader = type))
//            mSectionHeaderMap[type] = mArrivalItems.size-1
//            notifyItemInserted( mArrivalItems.size-1)
//        }
//        else {
//            Log.e(LOG_TAG, "섹션헤더 중복 추가 : $type")
//        }

//        return mArrivalItems.size-1
    }

//    fun applyItem(arrivalInfo : ArrivalInfo.BusArrivalInfo){
//        // 이미 목록에 있는 버스의 경우 도착 정보만 변경
//        mArrivalItems.forEachIndexed{ index, it->
//            if(it.arrivalInfo?.no == arrivalInfo.no){
//                Log.d("test", "$mStrStopName ${it.arrivalInfo.no} ${it.arrivalInfo.arrival}")
//                if(it.arrivalInfo.arrival != arrivalInfo.arrival){
//                    it.arrivalInfo.arrival = arrivalInfo.arrival
//                    notifyItemChanged(index)
//                }
//                return
//            }
//        }
//        // 새로 추가
//        addItem(arrivalInfo)
//    }

    fun applyDataSet(items: ArrayList<ArrivalInfo.BusArrivalInfo>) {
        val preArrivalItems = ArrayList<ArrivalRecyclerItem>()
        val sorted = itemSort(items)
        val grouped  = sorted.groupBy { it.type }
        grouped.forEach { group ->
            // 현재 필요한 섹션 헤더만 추가
            preArrivalItems.add(ArrivalRecyclerItem(group.key!!))
            group.value.forEach {
                preArrivalItems.add(ArrivalRecyclerItem(it))
            }
//            addSectionHeader(group.key!!)
//            group.value.forEach {
//                addItem(it)
//        }
        }
        preArrivalItems.add(0,ArrivalRecyclerItem())
        updateDataSet(preArrivalItems)
    }

    private fun updateDataSet(newDataSet : ArrayList<ArrivalRecyclerItem>) {
        val asdfas = ArrivalInfoDiffUtil(mArrivalItems, newDataSet)
        val result = DiffUtil.calculateDiff(asdfas)
        mArrivalItems.clear()
        mArrivalItems.addAll(newDataSet)
        result.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
    }

//    private fun applyPreDataSet(preArrivalItems: ArrayList<ArrivalRecyclerItem>) {
//        var lastMatch = 0
//        mArrivalItems.forEachIndexed old@{ oldIndex, old ->
//            preArrivalItems.forEachIndexed { newIndex, new->
//                if(old.equals(new)){
//                    if(old.itemType == ArrivalRecyclerItem.ItemType.ArrivalInfo){
//                        old.arrivalInfo!!.arrival = new.arrivalInfo!!.arrival
//                    }
//                    mArrivalItems.addAll(oldIndex, preArrivalItems.subList(lastMatch, newIndex))
//                    lastMatch = newIndex
//                    return@old
//                }
//                if(oldIndex == mArrivalItems.size-1){
//                    mArrivalItems.addAll(oldIndex, preArrivalItems.subList(lastMatch, preArrivalItems.size))
//                }
//            }
//        }
//        notifyDataSetChanged()

        // 남아있는 preArrivalItems는 새로 추가되는 버스 도착정보.
        // 각 분류에 맞게 배치.

//        val sectionPosition = mutableMapOf<ArrivalRecyclerItem.ItemType, Int>()
//        mArrivalItems.forEachIndexed{ index, it ->if(it.itemType == ArrivalRecyclerItem.ItemType.SectionHeader) sectionPosition.put(it.itemType, index)}

//    }

    private fun itemSort(items : ArrayList<ArrivalInfo.BusArrivalInfo>) : ArrayList<ArrivalInfo.BusArrivalInfo>{
        // 버스종류순 정렬
        items.sortWith(Comparator { o1, o2 ->
            o1.type!!.ordinal - o1.type.ordinal
        })

        // 번호순 정렬
        items.sortWith(compareBy { it.no })
        return items
    }
//
//    fun addItem(arrivalInfo : ArrivalInfo.BusArrivalInfo){
//        // TODO 나중에 추가되는 버스는 정렬이 안됨
//        val type = arrivalInfo.type
//        // 해당 섹션이 이미 있는 경우
//        if(mSectionHeaderMap[type] != null){
//            var position = mSectionHeaderMap[type]!! +1
//            if(position > mArrivalItems.size)
//                position = mArrivalItems.size
//            mArrivalItems.add(position, ArrivalRecyclerItem(arrivalInfo))
//            notifyItemInserted(position)
//            ArrivalInfo.BusType.eachToEnd(type!!){
//                if(mSectionHeaderMap[it] != null){
//                    mSectionHeaderMap[it] = mSectionHeaderMap[it]!! + 1
//                }
//
//            }
//        }
//
//        // 해당 섹션이 없는 경우
//        else {
//            val position = addSectionHeader(type!!)
//            mSectionHeaderMap[type] = position
//            notifyItemInserted(position)
//            mArrivalItems.add(position+1, ArrivalRecyclerItem(arrivalInfo))
//            notifyItemInserted(position+1)
//
//
//            ArrivalInfo.BusType.eachToEnd(type){
//                if(mSectionHeaderMap[it] != null){
//                    mSectionHeaderMap[it] = mSectionHeaderMap[it]!! + 2
//                }
//            }
//        }
//
//    }

    // 개별 버스 정보용 홀더
    class ItemViewHolder(val binding : RecyclerBusinfoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data : ArrivalInfo.BusArrivalInfo){
            binding.data = data
            binding.listener = this
        }
        fun onClick(data : ArrivalInfo.BusArrivalInfo){
            Log.d("test", "OnClick called")
            val context = binding.root.context
            val intent = Intent(context, RouteActivity::class.java)
            intent.putExtra("routeNo", data.no)
            context.startActivity(intent)
        }
    }

    // 버스 분류를 위한 홀더
    class SeperatorHolder(val v : View) : RecyclerView.ViewHolder(v) {
        fun bind(type : ArrivalInfo.BusType){
            v.findViewById<TextView>(R.id.separator).text = type.value
        }
    }

    // 문구를 위한 헤더 홀더
    class HeaderHolder(val v : View) : RecyclerView.ViewHolder(v)

}