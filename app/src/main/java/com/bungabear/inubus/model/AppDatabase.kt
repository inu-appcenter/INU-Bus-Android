package com.bungabear.inubus.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.bungabear.inubus.util.SearchHistoryDAO
import com.bungabear.inubus.util.Singleton.DB_VERSTION

/**
 * Created by Minjae Son on 2018-08-10.
 */

@Database(entities = [SearchHistoryItem::class], version = DB_VERSTION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDAO(): SearchHistoryDAO
}