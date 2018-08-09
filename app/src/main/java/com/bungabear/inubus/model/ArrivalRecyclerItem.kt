package com.bungabear.inubus.model

/**
 * Created by Minjae Son on 2018-08-07.
 */

// Column Header, Section Header, ArrivalItem을 함께 담는 객체
class ArrivalRecyclerItem(
        val itemType : ItemType = ItemType.ArrivalInfo,
        val sectionHeader : ArrivalInfo.BusType? = null,
        val arrivalInfo: ArrivalInfo.BusArrivalInfo? = null
){
    constructor() : this(ItemType.Header)
    constructor(sectionHeader : ArrivalInfo.BusType) : this(ItemType.SectionHeader, sectionHeader)
    constructor(arrivalInfo: ArrivalInfo.BusArrivalInfo) : this(ItemType.ArrivalInfo, arrivalInfo = arrivalInfo)

    // RecyclerView's Multiple Type Enum
    enum class ItemType{
        Header,SectionHeader, ArrivalInfo;
        companion object {
            fun findByOridinal(value: Int): ItemType = ItemType.values().find { it.ordinal == value } ?: Header
        }
    }

    fun equals(other: ArrivalRecyclerItem): Boolean {
        return when(this.itemType){
            ItemType.Header -> other.itemType == ItemType.Header
            ItemType.SectionHeader -> this.sectionHeader == other.sectionHeader
            ItemType.ArrivalInfo -> this.arrivalInfo!!.no == other.arrivalInfo!!.no
        }
    }
}