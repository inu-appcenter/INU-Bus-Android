package com.inu.bus.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Minjae Son on 2018-08-10.
 */

@Entity
data class DBSearchHistoryItem(
        @PrimaryKey(autoGenerate = true)
        var id : Int = 0,
        var name : String  = "",
        var date : Long = System.currentTimeMillis()
)  {
    @Ignore constructor() : this(0)
}