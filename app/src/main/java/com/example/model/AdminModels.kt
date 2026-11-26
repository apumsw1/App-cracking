package com.example.model

data class AppItem(
    val id: String,
    val name: String,
    val packageName: String,
    val versionName: String,
    val isSystemApp: Boolean,
    val targetSdk: Int,
    val permissionsCount: Int,
    val signatureHash: String
)

data class ToolItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val icon: String
)

data class LogEntry(
    val id: Long,
    val timestamp: String,
    val level: String, // INFO, WARN, ERROR, DEBUG
    val tag: String,
    val message: String
)

data class SecurityCheck(
    val id: String,
    val title: String,
    val description: String,
    val isPassed: Boolean,
    val severity: String
)
