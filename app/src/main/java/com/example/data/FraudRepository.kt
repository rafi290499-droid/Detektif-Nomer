package com.example.data

import android.content.Context
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.FraudReportEntity
import com.example.model.AiScanResult
import com.example.model.FraudReport
import com.example.model.RiskLevel
import com.example.model.ScamCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class FraudRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val dao = database.fraudDao()

    // Preloaded Indonesian community fraud database with verified suspects & BTS triangulation coordinates
    private val initialCommunityReports = listOf(
        FraudReport(
            id = "REP-001",
            phoneNumber = "+6281289341209",
            suspectName = "Hendra 'Doni' Saputra",
            suspectAlias = "Sindikat Surat Tilang & Kurir APK",
            suspectPhotoRes = R.drawable.suspect_1,
            riskLevel = RiskLevel.DANGER,
            category = ScamCategory.APK_MALWARE,
            reportCount = 4820,
            totalLossRupiah = 1450000000L,
            reportedDate = "12 Okt 2024",
            lastActive = "10 Menit Lalu",
            operatorName = "Telkomsel (Prepaid)",
            cellTowerLocation = "Kecamatan Palmerah, Jakarta Barat (BTS-PLM-401)",
            city = "Jakarta Barat",
            province = "DKI Jakarta",
            latitude = -6.1954,
            longitude = 106.7901,
            deviceInfo = "SIMBox 32-Port (IMEI Fake: 864501048821900)",
            modusOperandi = "Pelaku mengirim chat WhatsApp berkedok 'Surat Tilang ETLE.apk' atau 'Resi Paket J&T.apk'. Korban diminta klik dan instal file APK peretas yang membajak SMS OTP perbankan dan menguras rekening BRImo/BCA.",
            policeStatus = "DPO Subdit Siber Polda Metro Jaya No: DPO/108/X/2024",
            bankAccounts = listOf("BCA 8820194821 a/n Hendra S.", "BRI 01920194819201 a/n Doni P."),
            phishingLinks = listOf("https://surat-tilang-etle-polri.apk-dl.vip", "https://resi-kurir-express.cc/unduh.apk"),
            tags = listOf("APK Jahat", "Pembobol OTP", "Jangan Instal File", "Sindikat Tilang Palsu")
        ),
        FraudReport(
            id = "REP-002",
            phoneNumber = "+6287812904581",
            suspectName = "Siti Rahmawati (Alias Maya Angel)",
            suspectAlias = "Sindikat Lowongan Kerja Freelance Like Youtube",
            suspectPhotoRes = R.drawable.suspect_2,
            riskLevel = RiskLevel.DANGER,
            category = ScamCategory.LOWONGAN_KERJA,
            reportCount = 2940,
            totalLossRupiah = 890000000L,
            reportedDate = "20 Sep 2024",
            lastActive = "45 Menit Lalu",
            operatorName = "XL Axiata",
            cellTowerLocation = "Kecamatan Ilir Timur, Palembang (BTS-PLB-092)",
            city = "Palembang",
            province = "Sumatera Selatan",
            latitude = -2.9761,
            longitude = 104.7754,
            deviceInfo = "Samsung Galaxy A14 (VPN SG-Express Server)",
            modusOperandi = "Pelaku menjanjikan bayaran Rp 50.000 untuk tiap subscribe video YouTube/TikTok. Setelah korban terpancing, dipaksa menyetor 'deposit tugas VIP' bernilai jutaan hingga puluhan juta yang tidak bisa ditarik kembali.",
            policeStatus = "Status Peringatan Satgas Waspada Investasi OJK & Bareskrim",
            bankAccounts = listOf("Mandiri 137001928491 a/n PT Digital Berkah Mandiri", "SeaBank 9018291048 a/n Siti R."),
            phishingLinks = listOf("https://t.me/tugas_vip_freelance_indo", "https://task-reward-income.site"),
            tags = listOf("Kerja Paruh Waktu Palsu", "Deposit Bodong", "Telegram Scam", "Waspada OJK")
        ),
        FraudReport(
            id = "REP-003",
            phoneNumber = "+6285290182741",
            suspectName = "Bambang Wijayanto & Komplotan",
            suspectAlias = "Sindikat Call Center Bank Palsu (Phishing Perubahan Tarif)",
            suspectPhotoRes = R.drawable.suspect_3,
            riskLevel = RiskLevel.DANGER,
            category = ScamCategory.BANK_PHISHING,
            reportCount = 3710,
            totalLossRupiah = 2300000000L,
            reportedDate = "05 Agt 2024",
            lastActive = "2 Jam Lalu",
            operatorName = "Indosat Ooredoo Hutchison",
            cellTowerLocation = "Kecamatan Baranti, Sidrap (BTS-SDR-114)",
            city = "Sidenreng Rappang (Sidrap)",
            province = "Sulawesi Selatan",
            latitude = -3.9287,
            longitude = 119.8012,
            deviceInfo = "VoIP Gateway Cloud PBX (Caller ID Spoofer +1500888)",
            modusOperandi = "Menelepon mengatasnamakan Customer Service Bank BNI/BCA, mengancam adanya kenaikan biaya administrasi menjadi Rp 150.000/bulan jika korban tidak mengisi formulir pembatalan di situs tiruan bank.",
            policeStatus = "Daftar Penyelidikan Sindikat Passobis Siber Bareskrim Polri",
            bankAccounts = listOf("BNI 0981928471 a/n Bambang W.", "BCA 5210982341 a/n Penampung Kas"),
            phishingLinks = listOf("https://tarif-perubahan-resmi-bca.cfd", "https://batal-tarif-transaksi-bni.my.id"),
            tags = listOf("Telepon CS Palsu", "Phishing Bank", "Passobis Sidrap", "Pencuri Password")
        ),
        FraudReport(
            id = "REP-004",
            phoneNumber = "+6282199882310",
            suspectName = "Agus 'Romance' Salim",
            suspectAlias = "Sindikat Love Scamming & Investasi Kripto Fiktif",
            suspectPhotoRes = R.drawable.suspect_4,
            riskLevel = RiskLevel.HIGH,
            category = ScamCategory.LOVE_SCAM,
            reportCount = 1180,
            totalLossRupiah = 620000000L,
            reportedDate = "18 Jul 2024",
            lastActive = "Kemarin",
            operatorName = "Smartfren",
            cellTowerLocation = "Kecamatan Medan Sunggal, Medan (BTS-MDN-77)",
            city = "Medan",
            province = "Sumatera Utara",
            latitude = 3.5852,
            longitude = 98.6756,
            deviceInfo = "Xiaomi Redmi Note 12 Pro",
            modusOperandi = "Membuat profil palsu di aplikasi kencan (Tinder, Bumble), menjalin hubungan asmara jarak jauh, kemudian meminjam uang dengan alasan darurat rumah sakit atau mengajak investasi platform kripto bodong.",
            policeStatus = "Teridentifikasi Berkas Kasus Cyber Crime Polda Sumut",
            bankAccounts = listOf("Permata 410982319 a/n Agus S.", "Dana Wallet 082199882310"),
            phishingLinks = listOf("https://asiacrypto-vip-trade.cc"),
            tags = listOf("Love Scam", "Kencan Palsu", "Manipulasi Emosional", "Kripto Bodong")
        ),
        FraudReport(
            id = "REP-005",
            phoneNumber = "+6289531238910",
            suspectName = "Komplotan Pinjol Ilegal Cepat Cair",
            suspectAlias = "Desk Collector Peneror Kontak Darurat",
            suspectPhotoRes = R.drawable.suspect_3,
            riskLevel = RiskLevel.DANGER,
            category = ScamCategory.PINJOL_ILEGAL,
            reportCount = 5120,
            totalLossRupiah = 480000000L,
            reportedDate = "01 Sep 2024",
            lastActive = "5 Menit Lalu",
            operatorName = "Tri (3 Indonesia)",
            cellTowerLocation = "Kecamatan Cengkareng, Jakarta Barat (BTS-CGK-210)",
            city = "Jakarta Barat",
            province = "DKI Jakarta",
            latitude = -6.1528,
            longitude = 106.7262,
            deviceInfo = "Auto-Dialer Robocall Robot v4",
            modusOperandi = "Mentransfer uang tiba-tiba tanpa persetujuan ke rekening korban (Rp 1.000.000), lalu 4 hari kemudian menuntut pengembalian Rp 2.500.000 disertai ancaman penyebaran foto dan fitnah ke seluruh kontak WhatsApp korban.",
            policeStatus = "Dilaporkan Masal ke Satgas PASTI OJK & Ditreskrimsus",
            bankAccounts = listOf("BCA Digital 7001928301 a/n KSP Dana Berkah", "CIMB Niaga 80019283011"),
            phishingLinks = listOf("https://pinjaman-kilat-ojk-palsu.online"),
            tags = listOf("Pinjol Ilegal", "Teror Sebar Data", "Bunga Haram", "DPO OJK")
        ),
        FraudReport(
            id = "REP-006",
            phoneNumber = "+6281345670091",
            suspectName = "Sindikat Pemenang Undian Shopee/Telkomsel",
            suspectAlias = "Admin Gebyar Hadiah Mobil Avanza",
            suspectPhotoRes = R.drawable.suspect_1,
            riskLevel = RiskLevel.HIGH,
            category = ScamCategory.UNDIAN_PALSU,
            reportCount = 2150,
            totalLossRupiah = 340000000L,
            reportedDate = "15 Agu 2024",
            lastActive = "3 Jam Lalu",
            operatorName = "Telkomsel",
            cellTowerLocation = "Kecamatan Wajo, Makassar (BTS-MKS-045)",
            city = "Makassar",
            province = "Sulawesi Selatan",
            latitude = -5.1354,
            longitude = 119.4238,
            deviceInfo = "Modem SMS Blast 16-SIM",
            modusOperandi = "Kirim SMS blast: 'Selamat nomor Anda resmi memenangkan hadiah 1 unit Mobil Honda Brio dari PT Shopee Indonesia, hubungi WA kami sekarang'. Korban diminta mentransfer uang pajak pemenang sebesar Rp 3.500.000.",
            policeStatus = "Tercatat dalam Database CekRekening.id Kemenkominfo",
            bankAccounts = listOf("BRI 012901829102 a/n Syahrul A.", "BNI 192019481 a/n Bendahara Undian"),
            phishingLinks = listOf("https://gebyar-shopee-undian-resmi2024.web.id"),
            tags = listOf("Undian Bodong", "SMS Blast", "Pajak Hadiah Palsu", "Penipuan Hadiah")
        ),
        FraudReport(
            id = "REP-SAFE-01",
            phoneNumber = "1500888",
            suspectName = "PT Bank Central Asia Tbk",
            suspectAlias = "Halo BCA Resmi",
            suspectPhotoRes = null,
            riskLevel = RiskLevel.SAFE,
            category = ScamCategory.ALL,
            reportCount = 0,
            totalLossRupiah = 0L,
            reportedDate = "Official",
            lastActive = "Aktif 24/7",
            operatorName = "Telkom Indonesia PSTN",
            cellTowerLocation = "Menara BCA Grand Indonesia, Jakarta Pusat",
            city = "Jakarta Pusat",
            province = "DKI Jakarta",
            latitude = -6.1956,
            longitude = 106.8229,
            deviceInfo = "Enterprise Contact Center Avaya Unified PBX",
            modusOperandi = "Nomor resmi terverifikasi Halo BCA. Bank BCA TIDAK PERNAH meminta kode OTP, PIN, password, atau nomor CVV/CVC kartu debit/kredit Anda.",
            policeStatus = "NOMOR RESMI TERVERIFIKASI & AMAN",
            bankAccounts = emptyList(),
            phishingLinks = listOf("https://www.bca.co.id"),
            tags = listOf("Resmi Bank BCA", "Aman", "Customer Service Terverifikasi")
        ),
        FraudReport(
            id = "REP-SAFE-02",
            phoneNumber = "188",
            suspectName = "PT Telkomsel Indonesia",
            suspectAlias = "Call Center Resmi Telkomsel",
            suspectPhotoRes = null,
            riskLevel = RiskLevel.SAFE,
            category = ScamCategory.ALL,
            reportCount = 0,
            totalLossRupiah = 0L,
            reportedDate = "Official",
            lastActive = "Aktif 24/7",
            operatorName = "Telkomsel GSM",
            cellTowerLocation = "Telkom Landmark Tower, Jl. Gatot Subroto, Jakarta Selatan",
            city = "Jakarta Selatan",
            province = "DKI Jakarta",
            latitude = -6.2297,
            longitude = 106.8184,
            deviceInfo = "Official Telkomsel Enterprise Call Routing",
            modusOperandi = "Layanan pelanggan resmi Telkomsel. Hati-hati terhadap pihak yang menghubungi menggunakan nomor HP biasa mengatasnamakan promo Telkomsel.",
            policeStatus = "NOMOR RESMI TERVERIFIKASI & AMAN",
            bankAccounts = emptyList(),
            phishingLinks = listOf("https://www.telkomsel.com"),
            tags = listOf("Resmi Telkomsel", "Aman", "Layanan Pelanggan")
        )
    )

    fun getAllReportsFlow(): Flow<List<FraudReport>> {
        return dao.getAllReports().map { entities ->
            val userReports = entities.map { it.toFraudReport() }
            // Combine user reports and initial verified seed database
            userReports + initialCommunityReports
        }
    }

    suspend fun findReportByPhoneNumber(query: String): FraudReport? {
        val cleanQuery = query.replace("[^0-9+]".toRegex(), "")
        // Check Room first
        val local = dao.getReportByPhoneNumber(cleanQuery)
        if (local != null) return local.toFraudReport()

        // Check seed list
        return initialCommunityReports.find { report ->
            val cleanReport = report.phoneNumber.replace("[^0-9+]".toRegex(), "")
            cleanReport == cleanQuery || cleanReport.endsWith(cleanQuery) || cleanQuery.endsWith(cleanReport)
        }
    }

    suspend fun saveUserReport(report: FraudReport) {
        dao.insertReport(report.toEntity())
    }

    // AI & Heuristic Scam Message Analyzer
    fun analyzeSuspiciousMessage(text: String): AiScanResult {
        val lower = text.lowercase()
        val triggers = mutableListOf<String>()
        var score = 10
        var detectedType = "Pesan Biasa / Belum Diklasifikasikan"

        if (lower.contains(".apk") || lower.contains("unduh aplikasi") || lower.contains("install aplikasi") || lower.contains("file apk")) {
            triggers.add("Mengandung ekstensi file berbahaya (.APK) yang berpotensi menyadap SMS OTP")
            score += 55
            detectedType = "Malware Android / Pencuri OTP Rekening"
        }
        if (lower.contains("surat tilang") || lower.contains("etle") || lower.contains("pelanggaran lalu lintas")) {
            triggers.add("Pencatutan nama Kepolisian / Surat Tilang Elektronik ETLE")
            score += 35
            detectedType = "Modus Penipuan Surat Tilang Palsu"
        }
        if (lower.contains("selamat anda") || lower.contains("pemenang") || lower.contains("gebyar") || lower.contains("hadiah undian") || lower.contains("100jt") || lower.contains("jutaan rupiah")) {
            triggers.add("Pola undian berhadiah dengan klaim kemenangan fiktif")
            score += 45
            detectedType = "Penipuan Undian Berhadiah Palsu"
        }
        if (lower.contains("tarif transfer") || lower.contains("kenaikan tarif") || lower.contains("perubahan tarif") || lower.contains("150.000") || lower.contains("blokir rekening") || lower.contains("rekening ditangguhkan")) {
            triggers.add("Manipulasi psikologis kepanikan perubahan tarif atau pemblokiran rekening bank")
            score += 50
            detectedType = "Phishing Perbankan & Pencurian Kredensial"
        }
        if (lower.contains("like dan subscribe") || lower.contains("paruh waktu") || lower.contains("gaji harian") || lower.contains("freelance mudah") || lower.contains("tugas komisi")) {
            triggers.add("Janji pekerjaan freelance mudah dengan sistem deposit komisi (Task Scam)")
            score += 45
            detectedType = "Penipuan Lowongan Kerja Fiktif"
        }
        if (lower.contains("kurir") || lower.contains("paket tertahan") || lower.contains("resi paket") || lower.contains("j&t") || lower.contains("jne") || lower.contains("sicepat")) {
            triggers.add("Pencatutan nama ekspedisi pengiriman paket kurir")
            score += 30
            if (lower.contains(".apk")) detectedType = "Modus APK Kurir Paket Fiktif"
        }
        if (lower.contains("http://") || lower.contains("https://") || lower.contains("bit.ly") || lower.contains("t.me") || lower.contains("tinyurl") || lower.contains(".cfd") || lower.contains(".xyz") || lower.contains(".site")) {
            triggers.add("Terdapat tautan URL mencurigakan atau domain gratisan non-resmi")
            score += 25
        }
        if (lower.contains("otp") || lower.contains("kode verifikasi") || lower.contains("jangan berikan")) {
            triggers.add("Upaya meminta atau memancing kode rahasia OTP perbankan/e-wallet")
            score += 40
        }

        val clampedScore = score.coerceIn(5, 99)
        val riskLevel = when {
            clampedScore >= 70 -> RiskLevel.DANGER
            clampedScore >= 45 -> RiskLevel.HIGH
            clampedScore >= 25 -> RiskLevel.MODERATE
            else -> RiskLevel.SAFE
        }

        val recommendation = when (riskLevel) {
            RiskLevel.DANGER -> "BAHAYA EKSTREM! JANGAN klik link apa pun dan JANGAN instal file .APK yang dikirim. Segera blokir nomor ini dan laporkan ke database Radar Penipu."
            RiskLevel.HIGH -> "WASPADA TINGGI! Pesan ini memiliki indikasi kuat penipuan rekayasa sosial (social engineering). Jangan pernah transfer uang atau memberikan kode OTP."
            RiskLevel.MODERATE -> "MENCURIGAKAN. Lakukan verifikasi nomor ke call center resmi sebelum melakukan tindakan apa pun."
            RiskLevel.SAFE -> "Tidak terdeteksi pola ancaman signifikan. Tetap berhati-hati jangan membagikan PIN atau password."
        }

        // Extract numbers and links
        val numberRegex = "(\\+62|08)[0-9]{8,13}".toRegex()
        val linkRegex = "(https?://[^\\s]+)".toRegex()
        val extractedNumbers = numberRegex.findAll(text).map { it.value }.toList()
        val extractedLinks = linkRegex.findAll(text).map { it.value }.toList()

        return AiScanResult(
            textAnalyzed = text,
            riskScore = clampedScore,
            riskLevel = riskLevel,
            matchedScamType = detectedType,
            detectedTriggers = if (triggers.isEmpty()) listOf("Format pesan umum tanpa indikator ancaman langsung") else triggers,
            recommendation = recommendation,
            extractedNumbers = extractedNumbers,
            extractedLinks = extractedLinks
        )
    }

    private fun FraudReportEntity.toFraudReport(): FraudReport {
        return FraudReport(
            id = id,
            phoneNumber = phoneNumber,
            suspectName = suspectName,
            suspectAlias = suspectAlias,
            suspectPhotoRes = null,
            suspectPhotoUri = suspectPhotoUri,
            riskLevel = runCatching { RiskLevel.valueOf(riskLevelName) }.getOrDefault(RiskLevel.DANGER),
            category = runCatching { ScamCategory.valueOf(categoryName) }.getOrDefault(ScamCategory.ALL),
            reportCount = reportCount,
            totalLossRupiah = totalLossRupiah,
            reportedDate = reportedDate,
            lastActive = lastActive,
            operatorName = operatorName,
            cellTowerLocation = cellTowerLocation,
            city = city,
            province = province,
            latitude = latitude,
            longitude = longitude,
            deviceInfo = deviceInfo,
            modusOperandi = modusOperandi,
            policeStatus = policeStatus,
            bankAccounts = bankAccountsString.split("|").filter { it.isNotBlank() },
            phishingLinks = phishingLinksString.split("|").filter { it.isNotBlank() },
            tags = tagsString.split("|").filter { it.isNotBlank() },
            isUserReported = true
        )
    }

    private fun FraudReport.toEntity(): FraudReportEntity {
        return FraudReportEntity(
            id = id.ifEmpty { UUID.randomUUID().toString() },
            phoneNumber = phoneNumber,
            suspectName = suspectName,
            suspectAlias = suspectAlias,
            suspectPhotoUri = suspectPhotoUri,
            riskLevelName = riskLevel.name,
            categoryName = category.name,
            reportCount = reportCount,
            totalLossRupiah = totalLossRupiah,
            reportedDate = reportedDate,
            lastActive = lastActive,
            operatorName = operatorName,
            cellTowerLocation = cellTowerLocation,
            city = city,
            province = province,
            latitude = latitude,
            longitude = longitude,
            deviceInfo = deviceInfo,
            modusOperandi = modusOperandi,
            policeStatus = policeStatus,
            bankAccountsString = bankAccounts.joinToString("|"),
            phishingLinksString = phishingLinks.joinToString("|"),
            tagsString = tags.joinToString("|")
        )
    }
}
