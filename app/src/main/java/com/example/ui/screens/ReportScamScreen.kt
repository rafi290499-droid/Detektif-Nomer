package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScamCategory
import com.example.ui.theme.CyberCardDark
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.ScamAlertRed
import com.example.ui.theme.ScamSafeGreen

@Composable
fun ReportScamScreen(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    suspectName: String,
    onSuspectNameChange: (String) -> Unit,
    category: ScamCategory,
    onCategoryChange: (ScamCategory) -> Unit,
    lossRupiah: String,
    onLossRupiahChange: (String) -> Unit,
    modusDescription: String,
    onModusDescriptionChange: (String) -> Unit,
    bankAccount: String,
    onBankAccountChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    successMessage: String?,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("report_scam_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "LAPOR PENIPU KE KOMUNITAS",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Bantu Lindungi Jutaan Warga dari Tindak Kejahatan",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                    .border(1.dp, ScamAlertRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Report,
                    contentDescription = "Lapor",
                    tint = ScamAlertRed,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Success Alert Banner
        AnimatedVisibility(visible = successMessage != null) {
            if (successMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .background(ScamSafeGreen.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, ScamSafeGreen, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .testTag("report_success_banner")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sukses",
                            tint = ScamSafeGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = successMessage,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Form Fields
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111726)),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF263552))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Phone Number
                Text(
                    text = "Nomor Telepon Penipu *",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("report_phone_input"),
                    placeholder = { Text("Contoh: +6281234567890") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = "Nomor", tint = RadarCyan)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CyberCardDark,
                        unfocusedContainerColor = CyberCardDark,
                        focusedBorderColor = RadarCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Suspect Name / Alias
                Text(
                    text = "Nama / Alias / Sindikat Pelaku",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = suspectName,
                    onValueChange = onSuspectNameChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("report_name_input"),
                    placeholder = { Text("Contoh: CS Bank BCA Palsu / Hendra S.") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Nama", tint = RadarCyan)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CyberCardDark,
                        unfocusedContainerColor = CyberCardDark,
                        focusedBorderColor = RadarCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Selection
                Text(
                    text = "Kategori Modus Penipuan *",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val availableCategories = listOf(
                        ScamCategory.APK_MALWARE,
                        ScamCategory.BANK_PHISHING,
                        ScamCategory.UNDIAN_PALSU,
                        ScamCategory.PINJOL_ILEGAL,
                        ScamCategory.LOWONGAN_KERJA,
                        ScamCategory.LOVE_SCAM,
                        ScamCategory.PAKET_COD
                    )

                    availableCategories.forEach { cat ->
                        val isSelected = cat == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategoryChange(cat) },
                            label = {
                                Text(
                                    text = cat.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF1E293B),
                                labelColor = Color(0xFFCBD5E1),
                                selectedContainerColor = ScamAlertRed,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("report_category_${cat.name}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Kerugian & Lokasi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "Kerugian Korban (Rp)",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = lossRupiah,
                            onValueChange = onLossRupiahChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("report_loss_input"),
                            placeholder = { Text("0") },
                            leadingIcon = {
                                Icon(Icons.Default.MonetizationOn, contentDescription = "Uang", tint = RadarCyan)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CyberCardDark,
                                unfocusedContainerColor = CyberCardDark,
                                focusedBorderColor = RadarCyan,
                                unfocusedBorderColor = Color(0xFF334155),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Kota/Wilayah",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = city,
                            onValueChange = onCityChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("report_city_input"),
                            placeholder = { Text("Jakarta") },
                            leadingIcon = {
                                Icon(Icons.Default.LocationOn, contentDescription = "Kota", tint = RadarCyan)
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CyberCardDark,
                                unfocusedContainerColor = CyberCardDark,
                                focusedBorderColor = RadarCyan,
                                unfocusedBorderColor = Color(0xFF334155),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rekening Bank Penampung
                Text(
                    text = "Rekening Bank / e-Wallet Penampung",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = bankAccount,
                    onValueChange = onBankAccountChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("report_bank_account_input"),
                    placeholder = { Text("Contoh: BCA 8839102941 a/n Rahmat") },
                    leadingIcon = {
                        Icon(Icons.Default.CreditCard, contentDescription = "Rekening", tint = RadarCyan)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CyberCardDark,
                        unfocusedContainerColor = CyberCardDark,
                        focusedBorderColor = RadarCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Modus & Kronologi
                Text(
                    text = "Kronologi & Bukti Modus Operandi *",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = modusDescription,
                    onValueChange = onModusDescriptionChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("report_modus_input"),
                    placeholder = { Text("Jelaskan bagaimana pelaku menelpon/chat, meminta transfer, mengirim link APK berbahaya...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CyberCardDark,
                        unfocusedContainerColor = CyberCardDark,
                        focusedBorderColor = RadarCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_report_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = ScamAlertRed),
                    shape = RoundedCornerShape(14.dp),
                    enabled = phoneNumber.trim().length >= 5 && modusDescription.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Kirim",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kirim Laporan ke Database Masyarakat",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
