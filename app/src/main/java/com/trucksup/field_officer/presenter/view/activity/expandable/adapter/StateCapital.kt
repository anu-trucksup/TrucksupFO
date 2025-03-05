package com.trucksup.field_officer.presenter.view.activity.expandable.adapter

data class StateCapital(
    val countries: List<Country>
) {
    data class Country(
        val title: String, // India
        val states: List<State>
    ) {
        data class State(
            val capital: String, // Hyderabad
            val name: String // Telangana
        )
    }
}