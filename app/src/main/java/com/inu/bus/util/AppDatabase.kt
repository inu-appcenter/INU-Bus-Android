package com.inu.bus.util

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.inu.bus.model.DBSearchHistoryItem
import com.inu.bus.util.Singleton.DB_VERSION

/**
 * Created by Minjae Son on 2018-08-10.
 */

@Database(entities = [DBSearchHistoryItem::class], version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDAO(): SearchHistoryDAO
}