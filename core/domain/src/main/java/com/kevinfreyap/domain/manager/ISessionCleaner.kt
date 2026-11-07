package com.kevinfreyap.domain.manager

interface ISessionCleaner {
    suspend fun clearSessionData()
}