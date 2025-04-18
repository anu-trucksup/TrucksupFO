package com.trucksup.field_officer.data.model.home

data class HomeMenuItems(
    val userDetails: UserDetails?,
    val menuItems: MenuItems?,
    val menuItemsCount: MenuItemsCount?,
    val msg: String,
    val otherItemsCount: OtherItemsCount?,
    val todayPerformanceCount: TodayPerformanceCount?
)