package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.FraudRepository
import com.example.model.AiScanResult
import com.example.model.CallSimulationState
import com.example.model.FraudReport
import com.example.model.RiskLevel
import com.example.model.ScamCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppNavTab(val title: String, val testTag: String) {
    RADAR("Radar & Lacak", "nav_radar_tab"),
    DATABASE("Laporan Warga", "nav_database_tab"),
    SCANNER("Deteksi Chat", "nav_scanner_tab"),
    REPORT("Lapor Penipu", "nav_report_tab")
}

data class TrackerSearchState(
    val query: String = "",
    val isSearching: Boolean = false,
    val searchCompleted: Boolean = false,
    val matchedReport: FraudReport? = null,
    val simulatedOperator: String = "Telkomsel GSM",
    val simulatedCity: String = "Jakarta Barat",
    val simulatedCoordinates: Pair<Double, Double> = Pair(-6.1954, 106.7901),
    val simulatedSignalStrength: Int = 94,
    val simulatedRiskLevel: RiskLevel = RiskLevel.DANGER
)

class FraudViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FraudRepository(application)

    // Navigation
    private val _currentTab = MutableStateFlow(AppNavTab.RADAR)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    // Search / Tracker tab state
    private val _trackerSearchState = MutableStateFlow(TrackerSearchState())
    val trackerSearchState: StateFlow<TrackerSearchState> = _trackerSearchState.asStateFlow()

    // Category filter in community feed
    private val _selectedCategory = MutableStateFlow(ScamCategory.ALL)
    val selectedCategory: StateFlow<ScamCategory> = _selectedCategory.asStateFlow()

    // Search query in database tab
    private val _databaseQuery = MutableStateFlow("")
    val databaseQuery: StateFlow<String> = _databaseQuery.asStateFlow()

    // Selected suspect report for modal sheet
    private val _selectedReportDetail = MutableStateFlow<FraudReport?>(null)
    val selectedReportDetail: StateFlow<FraudReport?> = _selectedReportDetail.asStateFlow()

    // Call Simulation state
    private val _callSimulationState = MutableStateFlow(CallSimulationState())
    val callSimulationState: StateFlow<CallSimulationState> = _callSimulationState.asStateFlow()

    // AI Scanner state
    private val _scannerInputText = MutableStateFlow("")
    val scannerInputText: StateFlow<String> = _scannerInputText.asStateFlow()

    private val _scannerResult = MutableStateFlow<AiScanResult?>(null)
    val scannerResult: StateFlow<AiScanResult?> = _scannerResult.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    // User submission form fields
    private val _reportNumberInput = MutableStateFlow("")
    val reportNumberInput: StateFlow<String> = _reportNumberInput.asStateFlow()

    private val _reportNameInput = MutableStateFlow("")
    val reportNameInput: StateFlow<String> = _reportNameInput.asStateFlow()

    private val _reportCategoryInput = MutableStateFlow(ScamCategory.APK_MALWARE)
    val reportCategoryInput: StateFlow<ScamCategory> = _reportCategoryInput.asStateFlow()

    private val _reportLossInput = MutableStateFlow("")
    val reportLossInput: StateFlow<String> = _reportLossInput.asStateFlow()

    private val _reportModusInput = MutableStateFlow("")
    val reportModusInput: StateFlow<String> = _reportModusInput.asStateFlow()

    private val _reportBankAccountInput = MutableStateFlow("")
    val reportBankAccountInput: StateFlow<String> = _reportBankAccountInput.asStateFlow()

    private val _reportCityInput = MutableStateFlow("Jakarta Barat")
    val reportCityInput: StateFlow<String> = _reportCityInput.asStateFlow()

    private val _reportSuccessMessage = MutableStateFlow<String?>(null)
    val reportSuccessMessage: StateFlow<String?> = _reportSuccessMessage.asStateFlow()

    // Blocked numbers list
    private val _blockedNumbers = MutableStateFlow<Set<String>>(emptySet())
    val blockedNumbers: StateFlow<Set<String>> = _blockedNumbers.asStateFlow()

    // Filtered Community Reports Flow
    val allReports: StateFlow<List<FraudReport>> = combine(
        repository.getAllReportsFlow(),
        _selectedCategory,
        _databaseQuery
    ) { reports, category, query ->
        reports.filter { report ->
            val matchesCategory = category == ScamCategory.ALL || report.category == category
            val matchesQuery = query.isBlank() ||
                    report.phoneNumber.contains(query, ignoreCase = true) ||
                    report.suspectName.contains(query, ignoreCase = true) ||
                    report.suspectAlias.contains(query, ignoreCase = true) ||
                    report.city.contains(query, ignoreCase = true) ||
                    report.modusOperandi.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun setTrackerQuery(query: String) {
        _trackerSearchState.value = _trackerSearchState.value.copy(
            query = query,
            searchCompleted = false
        )
    }

    fun performTrackerSearch(phoneNumber: String) {
        if (phoneNumber.isBlank()) return
        viewModelScope.launch {
            _trackerSearchState.value = _trackerSearchState.value.copy(isSearching = true)
            delay(1200) // Radar scanning visual effect

            val matched = repository.findReportByPhoneNumber(phoneNumber)
            val operator = detectOperator(phoneNumber)
            val city = matched?.city ?: "Kecamatan Palmerah, Jakarta Barat"
            val coords = if (matched != null) Pair(matched.latitude, matched.longitude) else Pair(-6.1954, 106.7901)
            val risk = matched?.riskLevel ?: if (phoneNumber.startsWith("1500") || phoneNumber == "188") RiskLevel.SAFE else RiskLevel.HIGH

            _trackerSearchState.value = TrackerSearchState(
                query = phoneNumber,
                isSearching = false,
                searchCompleted = true,
                matchedReport = matched,
                simulatedOperator = operator,
                simulatedCity = city,
                simulatedCoordinates = coords,
                simulatedSignalStrength = (85..99).random(),
                simulatedRiskLevel = risk
            )
        }
    }

    fun setSelectedCategory(category: ScamCategory) {
        _selectedCategory.value = category
    }

    fun setDatabaseQuery(query: String) {
        _databaseQuery.value = query
    }

    fun openReportDetail(report: FraudReport) {
        _selectedReportDetail.value = report
    }

    fun closeReportDetail() {
        _selectedReportDetail.value = null
    }

    // Call simulation controls
    fun triggerCallSimulation(isScam: Boolean = true) {
        if (isScam) {
            _callSimulationState.value = CallSimulationState(
                isActive = true,
                callerNumber = "+6281289341209",
                callerName = "Hendra 'Doni' Saputra",
                riskLevel = RiskLevel.DANGER,
                scamCategory = "Modus Surat Tilang APK",
                modusSnippet = "Pelaku mengirimkan file .APK pencuri OTP perbankan. Jangan angkat!",
                location = "Palmerah, Jakarta Barat (BTS-PLM-401)",
                suspectPhotoRes = R.drawable.suspect_1
            )
        } else {
            _callSimulationState.value = CallSimulationState(
                isActive = true,
                callerNumber = "1500888",
                callerName = "Halo BCA Resmi",
                riskLevel = RiskLevel.SAFE,
                scamCategory = "Perbankan Resmi",
                modusSnippet = "Nomor resmi terverifikasi PT Bank Central Asia Tbk. Aman.",
                location = "Grand Indonesia, Jakarta Pusat",
                suspectPhotoRes = null
            )
        }
    }

    fun dismissCallSimulation() {
        _callSimulationState.value = CallSimulationState(isActive = false)
    }

    fun blockNumber(number: String) {
        _blockedNumbers.value = _blockedNumbers.value + number
    }

    fun unblockNumber(number: String) {
        _blockedNumbers.value = _blockedNumbers.value - number
    }

    // AI Scanner controls
    fun setScannerInputText(text: String) {
        _scannerInputText.value = text
    }

    fun analyzeScannerText() {
        val text = _scannerInputText.value
        if (text.isBlank()) return
        viewModelScope.launch {
            _isAnalyzing.value = true
            delay(1000)
            _scannerResult.value = repository.analyzeSuspiciousMessage(text)
            _isAnalyzing.value = false
        }
    }

    fun clearScanner() {
        _scannerInputText.value = ""
        _scannerResult.value = null
    }

    // Form controls for new report
    fun setReportNumberInput(value: String) { _reportNumberInput.value = value }
    fun setReportNameInput(value: String) { _reportNameInput.value = value }
    fun setReportCategoryInput(value: ScamCategory) { _reportCategoryInput.value = value }
    fun setReportLossInput(value: String) { _reportLossInput.value = value }
    fun setReportModusInput(value: String) { _reportModusInput.value = value }
    fun setReportBankAccountInput(value: String) { _reportBankAccountInput.value = value }
    fun setReportCityInput(value: String) { _reportCityInput.value = value }

    fun submitReport() {
        val number = _reportNumberInput.value.trim()
        val name = _reportNameInput.value.trim().ifEmpty { "Pelaku Belum Teridentifikasi" }
        val modus = _reportModusInput.value.trim()

        if (number.length < 5 || modus.isBlank()) return

        val loss = _reportLossInput.value.filter { it.isDigit() }.toLongOrNull() ?: 0L
        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())

        val newReport = FraudReport(
            id = "USER-" + System.currentTimeMillis(),
            phoneNumber = number,
            suspectName = name,
            suspectAlias = "Laporan Masyarakat Terverifikasi",
            suspectPhotoRes = R.drawable.suspect_2, // Evidence preview
            suspectPhotoUri = null,
            riskLevel = RiskLevel.DANGER,
            category = _reportCategoryInput.value,
            reportCount = 1,
            totalLossRupiah = loss,
            reportedDate = dateStr,
            lastActive = "Baru Saja",
            operatorName = detectOperator(number),
            cellTowerLocation = "BTS Kota ${_reportCityInput.value}",
            city = _reportCityInput.value,
            province = "Indonesia",
            latitude = -6.2088,
            longitude = 106.8456,
            deviceInfo = "Perangkat Dilaporkan Korban",
            modusOperandi = modus,
            policeStatus = "Tercatat dalam Laporan Komunitas Radar Penipu",
            bankAccounts = if (_reportBankAccountInput.value.isNotBlank()) listOf(_reportBankAccountInput.value) else emptyList(),
            phishingLinks = emptyList(),
            tags = listOf("Laporan Baru", _reportCategoryInput.value.displayName),
            isUserReported = true
        )

        viewModelScope.launch {
            repository.saveUserReport(newReport)
            _reportSuccessMessage.value = "Laporan nomor $number berhasil dikirim ke database masyarakat!"
            // Reset fields
            _reportNumberInput.value = ""
            _reportNameInput.value = ""
            _reportLossInput.value = ""
            _reportModusInput.value = ""
            _reportBankAccountInput.value = ""
            delay(3000)
            _reportSuccessMessage.value = null
        }
    }

    private fun detectOperator(number: String): String {
        val clean = number.replace("[^0-9]".toRegex(), "")
        return when {
            clean.startsWith("0811") || clean.startsWith("0812") || clean.startsWith("0813") || clean.startsWith("0821") || clean.startsWith("0822") || clean.startsWith("0852") || clean.startsWith("0853") -> "Telkomsel (GSM)"
            clean.startsWith("0814") || clean.startsWith("0815") || clean.startsWith("0816") || clean.startsWith("0855") || clean.startsWith("0856") || clean.startsWith("0857") || clean.startsWith("0858") -> "Indosat Ooredoo Hutchison"
            clean.startsWith("0817") || clean.startsWith("0818") || clean.startsWith("0819") || clean.startsWith("0859") || clean.startsWith("0877") || clean.startsWith("0878") -> "XL Axiata"
            clean.startsWith("0895") || clean.startsWith("0896") || clean.startsWith("0897") || clean.startsWith("0898") || clean.startsWith("0899") -> "Tri (3 Indonesia)"
            clean.startsWith("0881") || clean.startsWith("0882") || clean.startsWith("0888") -> "Smartfren Telecom"
            clean.startsWith("1500") || clean == "188" || clean == "110" -> "PSTN / Khusus Layanan Resmi"
            else -> "Operator Seluler Indonesia"
        }
    }

    fun formatRupiah(amount: Long): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(amount)
    }
}
