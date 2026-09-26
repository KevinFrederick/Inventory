package com.kevinfreyap.database.model

enum class SyncState {
    SYNCED,  // Matches server
    CREATED, // Created Offline
    UPDATED, // Modified Offline
    DELETED  // Deleted Offline
}