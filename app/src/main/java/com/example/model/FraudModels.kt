package com.example.model

enum class RiskLevel(val label: String, val levelNumber: Int) {
    DANGER("BAHAYA TINGGI", 4),
    HIGH("WASPADA", 3),
    MODERATE("MENCURIGAKAN", 2),
    SAFE("TERVERIFIKASI RESMI", 1)
}

enum class ScamCategory(val displayName: String, val iconName: String) {
    ALL("Semua Kategori", "AllInclusive"),
    APK_MALWARE("Modus APK Kurir/Surat Tilang", "Android"),
    BANK_PHISHING("Impersonasi Bank & Phishing", "AccountBalance"),
    UNDIAN_PALSU("Undian Berhadiah Palsu", "CardGiftcard"),
    PINJOL_ILEGAL("Pinjol Ilegal & Teror", "MoneyOff"),
    LOWONGAN_KERJA("Lowongan Kerja Like/Follow", "WorkOutline"),
    LOVE_SCAM("Love Scam / Asmara Fiktif", "FavoriteBorder"),
    PAKET_COD("Paket COD Fiktif", "LocalShipping")
}

data class FraudReport(
    val id: String,
    val phoneNumber: String,
    val suspectName: String,
    val suspectAlias: String,
    val suspectPhotoRes: Int? = null,
    val suspectPhotoUri: String? = null,
    val riskLevel: RiskLevel,
    val category: ScamCategory,
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
    val bankAccounts: List<String> = emptyList(),
    val phishingLinks: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val isUserReported: Boolean = false
)

data class CallSimulationState(
    val isActive: Boolean = false,
    val callerNumber: String = "",
    val callerName: String = "",
    val riskLevel: RiskLevel = RiskLevel.DANGER,
    val scamCategory: String = "",
    val modusSnippet: String = "",
    val location: String = "",
    val suspectPhotoRes: Int? = null
)

data class AiScanResult(
    val textAnalyzed: String,
    val riskScore: Int, // 0 - 100
    val riskLevel: RiskLevel,
    val matchedScamType: String,
    val detectedTriggers: List<String>,
    val recommendation: String,
    val extractedNumbers: List<String>,
    val extractedLinks: List<String>
)
