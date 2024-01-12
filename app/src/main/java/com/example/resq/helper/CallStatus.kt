package com.example.resq.helper

object CallStatus {
    fun get(em_call_status_id:String) = when(em_call_status_id){
        "S6LQDRJKurqtVAmRgBqy" -> "Menunggu Konfirmasi"
        "Bc1fUMyOIZZSDoUFWUSr" -> "Diproses"
        "rBiU5gy2mwSus2n96cMu" -> "Dalam Perjalanan"
        "HHxMYs0dSM10gS37PEjk" -> "Selesai"
        "dpjTG7Dxd1MwTR1m0Uph" -> "Dibatalkan"
        else -> "..."
    }

    val MENUNGGU_KONFIRMASI = "S6LQDRJKurqtVAmRgBqy"
    val DIPROSES = "Bc1fUMyOIZZSDoUFWUSr"
    val DALAM_PERJALANAN = "rBiU5gy2mwSus2n96cMu"
    val SELESAI = "HHxMYs0dSM10gS37PEjk"
    val DIBATALKAN = "dpjTG7Dxd1MwTR1m0Uph"
}