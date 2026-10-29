package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiScanResult
import com.example.model.RiskLevel
import com.example.ui.theme.CyberCardDark
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.ScamAlertRed
import com.example.ui.theme.ScamSafeGreen
import com.example.ui.theme.ScamWarningAmber

@Composable
fun ChatScannerScreen(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onAnalyze: () -> Unit,
    onClear: () -> Unit,
    isAnalyzing: Boolean,
    scanResult: AiScanResult?,
    onTrackExtractedNumber: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("chat_scanner_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "DETEKTOR CHAT & SMS PENIPU",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Analisis AI Pola Phishing, APK Berbahaya & Tautan Palsu",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                    .border(1.dp, RadarCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI",
                    tint = RadarCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Preset Sample Scams (Quick paste)
        Text(
            text = "Tempel Contoh Pesan Penipuan Terkini:",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val presets = listOf(
                "APK Tilang ETLE" to "Surat Pemberitahuan Tilang Elektronik ETLE No. 883912. Kendaraan Anda terekam kamera melanggar lampu merah. Silakan unduh dan instal file Surat_Tilang.apk untuk konfirmasi https://surat-tilang-etle.apk-dl.vip atau hubungi +6281289341209",
                "Undian Bank BRI" to "Selamat! Rekening BRI Anda terpilih mendapatkan Gebyar Hadiah Rp 150.000.000 dari PT Bank BRI (Persero) Tbk. Konfirmasi pencairan dana segera di bit.ly/gebyar-bri-resmi2024 atau hubungi call center +6285290182741",
                "Kerja Like Youtube" to "Halo kak, kami dari PT Media Digital membuka lowongan freelance paruh waktu. Tugas hanya like dan subscribe 3 video YouTube dapat komisi Rp 50.000 per video. Hubungi admin kami di +6287812904581 https://t.me/tugas_vip",
                "Kurir J&T Paket" to "Paket Anda dengan No Resi JNT-889123 tertahan di gudang karena alamat tidak jelas. Mohon buka dan verifikasi resi pengiriman melalui file LIHAT_FOTO_PAKET.apk berikut"
            )

            presets.forEach { (title, text) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                        .clickable { onInputTextChange(text) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("preset_chip_$title")
                ) {
                    Text(
                        text = title,
                        color = Color(0xFF93C5FD),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Box for SMS / Chat
        OutlinedTextField(
            value = inputText,
            onValueChange = onInputTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .testTag("chat_scanner_input"),
            placeholder = { Text("Tempel (paste) isi SMS atau chat WhatsApp mencurigakan di sini...") },
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

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onAnalyze,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("analyze_chat_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = RadarCyan),
                shape = RoundedCornerShape(12.dp),
                enabled = inputText.isNotBlank() && !isAnalyzing
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Menganalisis Pesan...",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Analisis",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Deteksi Ancaman AI",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (inputText.isNotEmpty()) {
                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("clear_chat_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Hapus",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Analysis Result Card
        AnimatedVisibility(
            visible = scanResult != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (scanResult != null) {
                val isDanger = scanResult.riskLevel == RiskLevel.DANGER || scanResult.riskLevel == RiskLevel.HIGH
                val accentColor = when (scanResult.riskLevel) {
                    RiskLevel.DANGER -> ScamAlertRed
                    RiskLevel.HIGH -> ScamWarningAmber
                    RiskLevel.MODERATE -> ScamWarningAmber
                    RiskLevel.SAFE -> ScamSafeGreen
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(2.dp, accentColor, RoundedCornerShape(18.dp))
                        .testTag("analysis_result_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111726))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isDanger) Icons.Default.Warning else Icons.Default.CheckCircle,
                                    contentDescription = "Status",
                                    tint = accentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = scanResult.riskLevel.label,
                                    color = accentColor,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                            }

                            Text(
                                text = "Skor Risiko: ${scanResult.riskScore}/100",
                                color = accentColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Progress gauge
                        LinearProgressIndicator(
                            progress = { scanResult.riskScore / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = accentColor,
                            trackColor = Color(0xFF1E293B)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Klasifikasi Modus: ${scanResult.matchedScamType}",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Indikator Bahaya Terdeteksi:",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        scanResult.detectedTriggers.forEach { trigger ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "• ",
                                    color = accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = trigger,
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        // Extracted numbers to track directly in radar
                        if (scanResult.extractedNumbers.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Nomor Telepon Ditemukan dalam Pesan:",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            scanResult.extractedNumbers.forEach { number ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Phone,
                                                contentDescription = "Nomor",
                                                tint = RadarCyan,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = number,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }

                                        Button(
                                            onClick = { onTrackExtractedNumber(number) },
                                            modifier = Modifier.height(36.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = RadarCyan),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.GpsFixed,
                                                contentDescription = "Lacak",
                                                tint = Color.Black,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Lacak di Radar",
                                                color = Color.Black,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Security Recommendation box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                                .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = "Rekomendasi",
                                        tint = accentColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Rekomendasi Keamanan:",
                                        color = accentColor,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = scanResult.recommendation,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
