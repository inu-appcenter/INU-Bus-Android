package com.bungabear.inubus.adapter

/**
 * Created by Bunga on 2018-01-29.
 */

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bungabear.inubus.R
import com.bungabear.inubus.databinding.RecyclerRouteItemBinding
import java.util.*

class RouteRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mDataSet = ArrayList<CustomItem>()

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
    class StopHolder(private val binding : RecyclerRouteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : CustomItem){
            binding.item = item
        }
    }

    // 회차지 뷰홀더
    class ReturnHolder(v: ConstraintLayout) : RecyclerView.ViewHolder(v)

    fun addStop(stopName: String, direction: Int, state: Int) {
        mDataSet.add(CustomItem(stopName, direction, state))
    }

    fun addReturn() {
        mDataSet.add(CustomItem())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: ConstraintLayout
        return if (viewType == 0) {
            StopHolder(RecyclerRouteItemBinding.inflate(LayoutInflater.from(parent.context)))
        } else {
            v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_route_return, parent, false) as ConstraintLayout
            ReturnHolder(v)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // 0 : 정류소
        // 1 : 회차지
        return if (mDataSet[position].state == 4) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mDataSet[position]
        if (item.state != 4) {
            (holder as StopHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = mDataSet.size
}