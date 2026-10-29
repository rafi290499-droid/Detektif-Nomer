package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FraudReport
import com.example.model.RiskLevel
import com.example.ui.components.RadarSweepCanvas
import com.example.ui.theme.CyberCardDark
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.ScamAlertRed
import com.example.ui.theme.ScamSafeGreen
import com.example.ui.theme.ScamWarningAmber
import com.example.ui.viewmodel.TrackerSearchState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RadarScreen(
    searchState: TrackerSearchState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onOpenSuspectDetail: (FraudReport) -> Unit,
    onTriggerCallSimulation: (Boolean) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("radar_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "RADAR PELACAK PENIPU",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Triangulasi BTS & Database Laporan Warga",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                    .border(1.dp, RadarCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ScamSafeGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RADAR AKTIF",
                        color = RadarCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Input Box
        OutlinedTextField(
            value = searchState.query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("phone_search_input"),
            placeholder = { Text("Masukkan nomor telepon penipu (+628... / 08...)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Cari",
                    tint = RadarCyan
                )
            },
            trailingIcon = {
                if (searchState.query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Hapus",
                            tint = Color.White
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearch(searchState.query)
                }
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CyberCardDark,
                unfocusedContainerColor = CyberCardDark,
                focusedBorderColor = RadarCyan,
                unfocusedBorderColor = Color(0xFF334155),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick sample test chips
        Text(
            text = "Nomor Contoh untuk Diuji:",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val samples = listOf(
                "+6281289341209" to "APK Tilang",
                "+6287812904581" to "Lowongan Like",
                "+6285290182741" to "CS Bank BNI/BCA Palsu",
                "1500888" to "Halo BCA Resmi"
            )

            samples.forEach { (number, tag) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                        .clickable {
                            onQueryChange(number)
                            onSearch(number)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("sample_chip_$number")
                ) {
                    Text(
                        text = "$number ($tag)",
                        color = Color(0xFF93C5FD),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Scan Action Button
        Button(
            onClick = {
                keyboardController?.hide()
                onSearch(searchState.query)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("scan_number_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = RadarCyan),
            shape = RoundedCornerShape(14.dp),
            enabled = !searchState.isSearching
        ) {
            if (searchState.isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Menjalankan Triangulasi BTS...",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Icon(
                    imageVector = Icons.Default.GpsFixed,
                    contentDescription = "Lacak",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Lacak Sinyal & Cek Database Pelaku",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Center Radar Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF0B1120))
                .border(1.5.dp, Color(0xFF1E293B), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            RadarSweepCanvas(
                modifier = Modifier.size(220.dp),
                isTargetDetected = searchState.searchCompleted,
                riskColor = if (searchState.simulatedRiskLevel == RiskLevel.SAFE) ScamSafeGreen else ScamAlertRed
            )

            // Radar stats overlay
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = "STATUS SINYAL",
                    color = RadarCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (searchState.searchCompleted) "TARGET TERKUNCI" else "STANDBY SCANNING",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "KUAT SINYAL: ${searchState.simulatedSignalStrength}%",
                    color = ScamSafeGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "JARINGAN: ${searchState.simulatedOperator}",
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Matched Suspect Card (if found)
        AnimatedVisibility(
            visible = searchState.searchCompleted,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val matched = searchState.matchedReport
            if (matched != null) {
                val isDanger = matched.riskLevel == RiskLevel.DANGER || matched.riskLevel == RiskLevel.HIGH
                val accentColor = if (isDanger) ScamAlertRed else ScamSafeGreen

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(2.dp, accentColor, RoundedCornerShape(18.dp))
                        .clickable { onOpenSuspectDetail(matched) }
                        .testTag("matched_suspect_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isDanger) "⚠️ ${matched.riskLevel.label}" else "🛡️ ${matched.riskLevel.label}",
                                    color = accentColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "Laporan: ${matched.reportCount}+ Orang",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Mugshot
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, accentColor, RoundedCornerShape(12.dp))
                                    .background(Color(0xFF1E293B)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (matched.suspectPhotoRes != null) {
                                    Image(
                                        painter = painterResource(id = matched.suspectPhotoRes),
                                        contentDescription = "Foto Pelaku",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (isDanger) Icons.Default.Warning else Icons.Default.Shield,
                                        contentDescription = "Avatar",
                                        tint = accentColor,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = matched.suspectName,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = matched.suspectAlias,
                                    color = accentColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CellTower,
                                        contentDescription = "Tower",
                                        tint = RadarCyan,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${matched.city} (${matched.operatorName})",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = matched.modusOperandi,
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onOpenSuspectDetail(matched) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("view_dossier_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDanger) ScamAlertRed else RadarCyan
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Buka Berkas & Bukti Pelaku",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                // If not in database, show generic telemetry analysis
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, ScamWarningAmber.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Waspada",
                                tint = ScamWarningAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Nomor Belum Ada di Laporan Warga",
                                color = ScamWarningAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Triangulasi BTS mendeteksi nomor aktif di ${searchState.simulatedCity}. Operator: ${searchState.simulatedOperator}.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Live incoming call simulation launcher banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, RadarCyan.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhoneInTalk,
                        contentDescription = "Simulasi",
                        tint = RadarCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Uji Coba Deteksi Panggilan Real-Time",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Lihat bagaimana Radar Penipu langsung menampilkan peringatan bahaya, foto pelaku, dan lokasi pemancar saat ada panggilan telepon masuk.",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onTriggerCallSimulation(true) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("simulate_scam_call_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = ScamAlertRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Simulasi Telepon Penipu",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { onTriggerCallSimulation(false) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("simulate_safe_call_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = ScamSafeGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Simulasi Telepon Resmi",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
