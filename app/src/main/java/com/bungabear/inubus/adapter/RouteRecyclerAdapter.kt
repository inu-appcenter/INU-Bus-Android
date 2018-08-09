package com.bungabear.inubus.adapter

/**
 * Created by Bunga on 2018-01-29.
 */

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import com.bungabear.inubus.R
import com.bungabear.inubus.databinding.RecyclerRouteItemBinding
import com.bungabear.inubus.util.LOG_TAG

import java.util.ArrayList

class RouteRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mDataset = ArrayList<CustomItem>()

    inner class CustomItem {
        // 가운데 선을 위한 값. 1이 시작점이라 아래선, 2이 끝점이라 윗선, 3은 중간점들로 전체 4는 회차점 표시
        var state: Int = 0

        // 1이면 오른쪽, 2면 왼쪽 표시
        var direction: Int = 0
        var stopName = ""

        constructor(stopName: String, direction: Int, state: Int) {
            this.stopName = stopName
            this.direction = direction
            this.state = state
        }

        constructor() {
            this.state = 4
        }
    }

    // 정류소 표시용 뷰홀더
    class StopHolder(val binding : RecyclerRouteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : CustomItem){
            binding.item = item
            Log.d(LOG_TAG, "${item.stopName} : ${item.state}")
        }
//        var left: TextView
//        var right: TextView
//        var start: View
//        var middle: View
//        var end: View
//
//        init {
//            left = v.findViewById(R.id.recycler_route_tv_left)
//            right = v.findViewById(R.id.recycler_route_tv_right)
//            start = v.findViewById(R.id.route_line_start)
//            middle = v.findViewById(R.id.route_line_middle)
//            end = v.findViewById(R.id.route_line_end)
//        }
//
//        fun setLine(state: Int) {
//            start.visibility = View.INVISIBLE
//            end.visibility = View.INVISIBLE
//            middle.visibility = View.INVISIBLE
//            when (state) {
//                1 -> start.visibility = View.VISIBLE
//                2 -> end.visibility = View.VISIBLE
//                3 -> middle.visibility = View.VISIBLE
//            }
//        }
//
//        fun setDirection(direction: Int) {
//            if (direction == 1) {
//                right.visibility = View.VISIBLE
//                left.visibility = View.INVISIBLE
//            } else {
//                left.visibility = View.VISIBLE
//                right.visibility = View.INVISIBLE
//            }
//        }
    }

    // 회차지 뷰홀더
    class ReturnHolder(v: ConstraintLayout) : RecyclerView.ViewHolder(v)

    fun addStop(stopName: String, direction: Int, state: Int) {
        mDataset.add(CustomItem(stopName, direction, state))
    }

    fun addReturn() {
        mDataset.add(CustomItem())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: ConstraintLayout
        if (viewType == 0) {
            return StopHolder(RecyclerRouteItemBinding.inflate(LayoutInflater.from(parent.context)))
        } else {
            v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_route_return, parent, false) as ConstraintLayout
            return ReturnHolder(v)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // 0 : 정류소
        // 1 : 회차지
        return if (mDataset[position].state == 4) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mDataset[position]
        if (item.state == 4) {
            val mHolder = holder as ReturnHolder
        } else {
            (holder as StopHolder).bind(item)
//            val mHolder = holder as StopHolder
//            mHolder.setLine(item.state)
//            mHolder.setDirection(item.direction)

        }
    }

    override fun getItemCount(): Int = mDataset.size
}