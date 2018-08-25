package com.inu.bus.model

/**
 * Created by Minjae Son on 2018-08-07.
 */

// Column Header, Section Header, ArrivalItem을 함께 담는 객체
class RecyclerArrivalItem(
        val itemType : ItemType = ItemType.ArrivalInfo,
        val sectionHeader : String? = null,
        val arrivalInfo: BusArrivalInfo? = null,
        val needButton: Boolean = false
){
    constructor() : this(ItemType.Header)
    constructor(sectionHeader : String, needButton : Boolean = false)
            : this(ItemType.SectionHeader, sectionHeader, needButton = needButton)
    constructor(arrivalInfo: BusArrivalInfo) : this(ItemType.ArrivalInfo, arrivalInfo = arrivalInfo)

    // RecyclerView's Multiple Type Enum
    enum class ItemType{
        Header,SectionHeader, ArrivalInfo;
        companion object {
            fun findByOrdinal(value: Int): ItemType = ItemType.values().find { it.ordinal == value } ?: Header
        }
    }

    fun equals(other: RecyclerArrivalItem): Boolean {
        return when(this.itemType){
            ItemType.Header -> other.itemType == ItemType.Header
            ItemType.SectionHeader -> this.sectionHeader == other.sectionHeader
            ItemType.ArrivalInfo -> if(other.arrivalInfo == null) false else this.arrivalInfo!!.no == other.arrivalInfo.no
        }
    }
}