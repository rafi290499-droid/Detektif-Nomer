package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.IncomingCallAlertOverlay
import com.example.ui.components.SuspectDetailDialog
import com.example.ui.screens.ChatScannerScreen
import com.example.ui.screens.DatabaseScreen
import com.example.ui.screens.RadarScreen
import com.example.ui.screens.ReportScamScreen
import com.example.ui.theme.CyberDark
import com.example.ui.theme.CyberSurfaceDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RadarCyan
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.FraudViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: FraudViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: FraudViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val searchState by viewModel.trackerSearchState.collectAsState()
    val allReports by viewModel.allReports.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val databaseQuery by viewModel.databaseQuery.collectAsState()
    val selectedReportDetail by viewModel.selectedReportDetail.collectAsState()
    val callSimulationState by viewModel.callSimulationState.collectAsState()
    val blockedNumbers by viewModel.blockedNumbers.collectAsState()

    // Scanner state
    val scannerInputText by viewModel.scannerInputText.collectAsState()
    val scannerResult by viewModel.scannerResult.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()

    // Report state
    val reportNumberInput by viewModel.reportNumberInput.collectAsState()
    val reportNameInput by viewModel.reportNameInput.collectAsState()
    val reportCategoryInput by viewModel.reportCategoryInput.collectAsState()
    val reportLossInput by viewModel.reportLossInput.collectAsState()
    val reportModusInput by viewModel.reportModusInput.collectAsState()
    val reportBankAccountInput by viewModel.reportBankAccountInput.collectAsState()
    val reportCityInput by viewModel.reportCityInput.collectAsState()
    val reportSuccessMessage by viewModel.reportSuccessMessage.collectAsState()

    // Handle back button
    BackHandler(enabled = currentTab != AppNavTab.RADAR || selectedReportDetail != null) {
        if (selectedReportDetail != null) {
            viewModel.closeReportDetail()
        } else {
            viewModel.selectTab(AppNavTab.RADAR)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = CyberDark,
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar"),
                    containerColor = CyberSurfaceDark,
                    tonalElevation = 8.dp
                ) {
                    AppNavTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        val icon = when (tab) {
                            AppNavTab.RADAR -> if (isSelected) Icons.Filled.GpsFixed else Icons.Outlined.GpsFixed
                            AppNavTab.DATABASE -> if (isSelected) Icons.Filled.ListAlt else Icons.Outlined.ListAlt
                            AppNavTab.SCANNER -> if (isSelected) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome
                            AppNavTab.REPORT -> if (isSelected) Icons.Filled.Report else Icons.Outlined.Report
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = RadarCyan,
                                indicatorColor = RadarCyan,
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B)
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    AppNavTab.RADAR -> {
                        RadarScreen(
                            searchState = searchState,
                            onQueryChange = { viewModel.setTrackerQuery(it) },
                            onSearch = { viewModel.performTrackerSearch(it) },
                            onOpenSuspectDetail = { viewModel.openReportDetail(it) },
                            onTriggerCallSimulation = { viewModel.triggerCallSimulation(it) }
                        )
                    }
                    AppNavTab.DATABASE -> {
                        DatabaseScreen(
                            reports = allReports,
                            searchQuery = databaseQuery,
                            onSearchQueryChange = { viewModel.setDatabaseQuery(it) },
                            selectedCategory = selectedCategory,
                            onSelectCategory = { viewModel.setSelectedCategory(it) },
                            onOpenSuspectDetail = { viewModel.openReportDetail(it) }
                        )
                    }
                    AppNavTab.SCANNER -> {
                        ChatScannerScreen(
                            inputText = scannerInputText,
                            onInputTextChange = { viewModel.setScannerInputText(it) },
                            onAnalyze = { viewModel.analyzeScannerText() },
                            onClear = { viewModel.clearScanner() },
                            isAnalyzing = isAnalyzing,
                            scanResult = scannerResult,
                            onTrackExtractedNumber = { number ->
                                viewModel.setTrackerQuery(number)
                                viewModel.selectTab(AppNavTab.RADAR)
                                viewModel.performTrackerSearch(number)
                            }
                        )
                    }
                    AppNavTab.REPORT -> {
                        ReportScamScreen(
                            phoneNumber = reportNumberInput,
                            onPhoneNumberChange = { viewModel.setReportNumberInput(it) },
                            suspectName = reportNameInput,
                            onSuspectNameChange = { viewModel.setReportNameInput(it) },
                            category = reportCategoryInput,
                            onCategoryChange = { viewModel.setReportCategoryInput(it) },
                            lossRupiah = reportLossInput,
                            onLossRupiahChange = { viewModel.setReportLossInput(it) },
                            modusDescription = reportModusInput,
                            onModusDescriptionChange = { viewModel.setReportModusInput(it) },
                            bankAccount = reportBankAccountInput,
                            onBankAccountChange = { viewModel.setReportBankAccountInput(it) },
                            city = reportCityInput,
                            onCityChange = { viewModel.setReportCityInput(it) },
                            successMessage = reportSuccessMessage,
                            onSubmit = { viewModel.submitReport() }
                        )
                    }
                }
            }
        }

        // Suspect Detail Modal
        selectedReportDetail?.let { report ->
            SuspectDetailDialog(
                report = report,
                isBlocked = blockedNumbers.contains(report.phoneNumber),
                onToggleBlock = { number ->
                    if (blockedNumbers.contains(number)) {
                        viewModel.unblockNumber(number)
                    } else {
                        viewModel.blockNumber(number)
                    }
                },
                onDismiss = { viewModel.closeReportDetail() }
            )
        }

        // Live Call Simulation Heads-Up Overlay
        if (callSimulationState.isActive) {
            IncomingCallAlertOverlay(
                state = callSimulationState,
                onBlockAndDismiss = { number ->
                    viewModel.blockNumber(number)
                    viewModel.dismissCallSimulation()
                },
                onDismiss = { viewModel.dismissCallSimulation() }
            )
        }
    }
}
