package com.inu.bus.recycler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.Observable
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout
import com.inu.bus.activity.RouteActivity
import com.inu.bus.databinding.RecyclerDestinationItemBinding
import com.inu.bus.model.BusInformation
import com.inu.bus.util.Singleton


/**
 * Created by Minjae Son on 2018-08-18.
 */

class DestinationRecyclerViewHolder(private val binding : RecyclerDestinationItemBinding) : RecyclerView.ViewHolder(binding.root){

    companion object {
        private val mAllBusInfo : ArrayList<BusInformation> = ArrayList(Singleton.busInfo.get()!!.values)
    }

    fun bind(title : String){
        binding.destinationHeader?.tvSection?.text = title
        load(title)

        Singleton.arrivalFromInfo.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mAllBusInfo.clear()
                Singleton.busInfo.get()?.values?.forEach {
                    mAllBusInfo.add(it)
                }
                load(title)
            }
        })
    }

    private fun load(title : String){

        val filtered = mAllBusInfo.filter {
            it.nodeList.any { subIt -> subIt.contains(title) }
        }

        // 정렬
        val sorted = filtered.sortedWith(Comparator { o1, o2 ->
            when{
                (o1.type != o2.type) -> o1.type.ordinal - o2.type.ordinal
                else -> o1.no.compareTo(o2.no)
            }
        })

        val flexBox = binding.destinationContainer
        flexBox.removeAllViews()
        sorted.forEachIndexed { index, busInformation ->
            val busView = newBusView(flexBox.context, busInformation)
            if(index > 0){
                if(sorted[index-1].type != busInformation.type){
                    val lp = busView.layoutParams as FlexboxLayout.LayoutParams
                    lp.isWrapBefore = true
                    busView.layoutParams = lp
                }
            }
            flexBox.addView(busView)
        }
    }

    private fun newBusView(context : Context, data : BusInformation) : TextView{
        val bus = TextView(context)
        bus.text = data.no
        bus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        bus.setTextColor(data.type.color)
        val scale = context.resources.displayMetrics.density
        val lp = FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(lp.marginLeft,lp.marginTop, (scale*10).toInt(), lp.marginBottom)
        bus.layoutParams = lp
        val typedValue = TypedValue()
        (context as Activity).theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
        bus.setBackgroundResource(typedValue.resourceId)
        bus.setOnClickListener {
            val intent = Intent(context, RouteActivity::class.java)
            intent.putExtra("routeNo", data.no)
            context.startActivity(intent)
        }
        return bus
    }
}