package com.inu.bus.recycler

/**
 * Created by Bunga on 2018-01-29.
 */

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.inu.bus.R
import com.inu.bus.databinding.RecyclerRouteItemBinding
import java.util.*

class RecyclerAdapterRoute : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class RouteType{
        STOP, LINE, RETURN
    }

    enum class Direction{
        NONE, LEFT, RIGHT
    }

    private val mDataSet = ArrayList<CustomItem>()

    override fun getItemViewType(position: Int) : Int = mDataSet[position].type.ordinal

    // direction : 1이면 오른쪽, 2면 왼쪽 표시
    // state :
    //      0 : 정류소
    //      1 : 중간 라인
    //      2 : 회차지
    inner class CustomItem(val stopName: String, val direction: Direction, val type: RouteType) {
        constructor( type : RouteType) : this("", Direction.NONE, type)
    }

    // 정류소 표시용 뷰홀더
    class StopHolder(private val binding : RecyclerRouteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : CustomItem){
            binding.item = item
        }
    }

    // 회차지 뷰홀더
    class ReturnHolder(v: ConstraintLayout) : RecyclerView.ViewHolder(v)

    // 중간 라인 뷰홀더
    class LineHolder(v: ConstraintLayout) : RecyclerView.ViewHolder(v)

    fun addStop(stopName: String, direction: Direction, type: RouteType) {
        mDataSet.add(CustomItem(stopName, direction, type))
    }

    fun addReturn() {
        mDataSet.add(CustomItem(RouteType.RETURN))
    }

    fun addLine(){
        mDataSet.add(CustomItem(RouteType.LINE))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: ConstraintLayout
        return when(RouteType.values()[viewType]){
            RouteType.STOP-> StopHolder(RecyclerRouteItemBinding.inflate(LayoutInflater.from(parent.context)))
            RecyclerAdapterRoute.RouteType.LINE -> {
                v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_route_line_item, parent, false) as ConstraintLayout
                LineHolder(v)
            }
            RecyclerAdapterRoute.RouteType.RETURN -> {
                v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_route_return, parent, false) as ConstraintLayout
                ReturnHolder(v)
            }
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mDataSet[position]
        if (item.type == RouteType.STOP) {
            (holder as StopHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = mDataSet.size
}