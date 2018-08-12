package com.bungabear.inubus.util

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bungabear.inubus.model.SearchHistoryItem

/**
 * Created by Minjae Son on 2018-08-10.
 */
@Dao
interface SearchHistoryDAO{
    @Insert
    fun insert(contacts : SearchHistoryItem )

    @Delete
    fun delete(item : SearchHistoryItem)

    @Query("SELECT * FROM SearchHistoryItem")
    fun getAll(): List<SearchHistoryItem>
}