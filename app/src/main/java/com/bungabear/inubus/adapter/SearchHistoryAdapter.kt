package com.bungabear.inubus.adapter

import android.arch.persistence.room.Room
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bungabear.inubus.activity.MainActivity
import com.bungabear.inubus.model.AppDatabase
import com.bungabear.inubus.model.SearchHistoryItem
import com.bungabear.inubus.util.millisToDate
import kotlinx.android.synthetic.main.search_history_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Minjae Son on 2018-08-10.
 */

class SearchHistoryAdapter(private val mContext : Context, private val mLayout : Int) : ArrayAdapter<SearchHistoryItem>(mContext, mLayout) {

    private val DB = MainActivity.DB
    private var mHistoryList = DB.searchHistoryDAO().getAll()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val root = convertView ?: LayoutInflater.from(mContext).inflate(mLayout, parent, false)
        root.tv_autocomplete_item_name.text = mHistoryList[position].name
        root.tv_autocomplete_item_date.text = millisToDate( mHistoryList[position].date)
        root.btn_autocomplete_item_delete.setOnClickListener {
            DB.searchHistoryDAO().delete(mHistoryList[position])
            notifyDataSetChanged()
        }
        return root
    }

    override fun getCount(): Int = mHistoryList.size

    fun refreshHistory(){
        mHistoryList = DB.searchHistoryDAO().getAll()
    }
}