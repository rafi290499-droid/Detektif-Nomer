package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FraudDao {
    @Query("SELECT * FROM fraud_reports ORDER BY totalLossRupiah DESC")
    fun getAllReports(): Flow<List<FraudReportEntity>>

    @Query("SELECT * FROM fraud_reports WHERE phoneNumber LIKE '%' || :query || '%' OR suspectName LIKE '%' || :query || '%' OR suspectAlias LIKE '%' || :query || '%'")
    fun searchReports(query: String): Flow<List<FraudReportEntity>>

    @Query("SELECT * FROM fraud_reports WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getReportByPhoneNumber(phoneNumber: String): FraudReportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: FraudReportEntity)

    @Query("DELETE FROM fraud_reports WHERE id = :id")
    suspend fun deleteReport(id: String)
}
