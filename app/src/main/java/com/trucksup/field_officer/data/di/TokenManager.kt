package com.trucksup.field_officer.data.di

interface TokenManager {
    val token: String?

    fun hasToken(): Boolean
    fun clearToken()
    fun refreshToken(): String?
}
