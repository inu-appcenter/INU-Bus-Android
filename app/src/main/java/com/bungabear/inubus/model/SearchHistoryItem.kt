package com.bungabear.inubus.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Minjae Son on 2018-08-10.
 */

@Entity
data class SearchHistoryItem(
        @PrimaryKey(autoGenerate = true)
        var id : Int = 0,
        var name : String = "",
        var date : Long = System.currentTimeMillis()
)