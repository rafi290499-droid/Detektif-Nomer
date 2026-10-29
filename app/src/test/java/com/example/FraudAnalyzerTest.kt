package com.example

import com.example.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FraudAnalyzerTest {

    @Test
    fun testApkThreatDetection() {
        val sampleMessage = "Unduh dan instal file surat tilang elektronik Surat_Tilang.apk untuk konfirmasi pelanggaran"
        val lower = sampleMessage.lowercase()
        val containsApk = lower.contains(".apk")
        val containsTilang = lower.contains("surat tilang")

        assertTrue("Message should be flagged for APK extension", containsApk)
        assertTrue("Message should be flagged for Tilang keyword", containsTilang)
    }

    @Test
    fun testRiskLevelRanking() {
        assertTrue(RiskLevel.DANGER.levelNumber > RiskLevel.HIGH.levelNumber)
        assertTrue(RiskLevel.HIGH.levelNumber > RiskLevel.MODERATE.levelNumber)
        assertTrue(RiskLevel.MODERATE.levelNumber > RiskLevel.SAFE.levelNumber)
    }

    @Test
    fun testPhoneNumberRegexExtraction() {
        val message = "Hubungi admin kami di +6281289341209 atau via link berikut"
        val regex = "(\\+62|08)[0-9]{8,13}".toRegex()
        val match = regex.find(message)
        assertEquals("+6281289341209", match?.value)
    }
}
