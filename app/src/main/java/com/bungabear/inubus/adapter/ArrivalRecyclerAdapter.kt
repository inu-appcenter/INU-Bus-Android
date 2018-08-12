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
import com.bungabear.inubus.model.ArrivalInfoModel
import com.bungabear.inubus.model.ArrivalRecyclerItem
import com.bungabear.inubus.util.ArrivalInfoDiffUtil
import com.bungabear.inubus.util.Singleton
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Minjae Son on 2018-08-07.
 */

class ArrivalRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mArrivalItems = ArrayList<ArrivalRecyclerItem>()
    private var mFilteredItems = ArrayList<ArrivalRecyclerItem>()
    private var mFilteringString = ""

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
                SectionHeaderViewHolder(v)

            }
            ArrivalRecyclerItem.ItemType.ArrivalInfo->{
                ItemViewHolder(RecyclerBusinfoItemBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(mFilteredItems[position].itemType){
            ArrivalRecyclerItem.ItemType.Header->{ }
            ArrivalRecyclerItem.ItemType.SectionHeader->{
                (holder as SectionHeaderViewHolder).bind(mFilteredItems[position].sectionHeader!!)
            }
            ArrivalRecyclerItem.ItemType.ArrivalInfo->{
                (holder as ItemViewHolder).bind(mFilteredItems[position].arrivalInfo!!)
            }
        }

    }

    override fun getItemCount(): Int = mFilteredItems.size
    override fun getItemViewType(position: Int): Int = mFilteredItems[position].itemType.ordinal

    fun applyDataSet(items: ArrayList<ArrivalInfoModel.BusArrivalInfo>) {
        mArrivalItems.clear()
        val sorted = itemSort(items)
        val grouped  = sorted.groupBy { it.type }
        grouped.forEach { group ->
            // 현재 필요한 섹션 헤더만 추가
            mArrivalItems.add(ArrivalRecyclerItem(group.key!!))
            group.value.forEach {
                mArrivalItems.add(ArrivalRecyclerItem(it))
            }
        }

        mArrivalItems.add(0,ArrivalRecyclerItem())
        filter()

    }


    private fun itemSort(items : ArrayList<ArrivalInfoModel.BusArrivalInfo>) : ArrayList<ArrivalInfoModel.BusArrivalInfo>{
        // 버스종류순 정렬
        items.sortWith(Comparator { o1, o2 ->
            o1.type!!.ordinal - o2.type!!.ordinal
        })

        // 번호순 정렬
        items.sortWith(compareBy { it.no })
        return items
    }

    // 개별 버스 정보용 홀더
    class ItemViewHolder(private val binding : RecyclerBusinfoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data : ArrivalInfoModel.BusArrivalInfo){
            binding.data = data
            binding.listener = this
        }
        fun onClick(data : ArrivalInfoModel.BusArrivalInfo){
            Log.d("test", "OnClick called")
            val context = binding.root.context
            val intent = Intent(context, RouteActivity::class.java)
            intent.putExtra("routeNo", data.no)
            context.startActivity(intent)
        }
    }

    // 버스 분류를 위한 홀더
    class SectionHeaderViewHolder(private val v : View) : RecyclerView.ViewHolder(v) {
        fun bind(type : ArrivalInfoModel.BusType){
            v.findViewById<TextView>(R.id.separator).text = type.value
        }
    }

    // 문구를 위한 헤더 홀더
    class HeaderHolder(v : View) : RecyclerView.ViewHolder(v)

    fun filter(str : String = mFilteringString) {
        mFilteringString = str
        val filtered =
        // 검색 취소
                if(str == ""){
                    mArrivalItems
                }
                // 부서 안에서 검색
                else {
                    ArrayList(
                            mArrivalItems.filter { item ->
                                if (item.itemType == ArrivalRecyclerItem.ItemType.ArrivalInfo)
                                    !Singleton.busInfo[item.arrivalInfo!!.no]!!.nodeList.find{ it.contains(str) }.isNullOrEmpty()
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
}