package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fraud_reports")
data class FraudReportEntity(
    @PrimaryKey
    val id: String,
    val phoneNumber: String,
    val suspectName: String,
    val suspectAlias: String,
    val suspectPhotoUri: String? = null,
    val riskLevelName: String,
    val categoryName: String,
    val reportCount: Int,
    val totalLossRupiah: Long,
    val reportedDate: String,
    val lastActive: String,
    val operatorName: String,
    val cellTowerLocation: String,
    val city: String,
    val province: String,
    val latitude: Double,
    val longitude: Double,
    val deviceInfo: String,
    val modusOperandi: String,
    val policeStatus: String,
    val bankAccountsString: String,
    val phishingLinksString: String,
    val tagsString: String
)
