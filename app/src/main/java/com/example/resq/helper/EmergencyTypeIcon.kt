package com.example.resq.helper

import androidx.compose.ui.graphics.Color
import com.example.resq.R

class EmergencyTypeIcon {
    companion object {
        fun getIconId(
            emTypeId: String
        ): Int? {
            return when (emTypeId) {
                "7RsOFLzwf5HMcMfCycGr" -> R.drawable.icon_emtype_sar
                "DBcNw5u9DSdLtx0w5Yay" -> R.drawable.icon_emtype_polisi
                "XnhtKNbaOIHd1fVGRGlq" -> R.drawable.icon_emtype_ambulans
                "oleEOFjY5z9JMd4Pffw9" -> R.drawable.icon_emtype_damkar
                else -> null
            }
        }

        fun getContainerColor(
            emTypeId: String
        ):Color {
            return when (emTypeId) {
                "7RsOFLzwf5HMcMfCycGr" -> Color(0xffECC6FF)
                "DBcNw5u9DSdLtx0w5Yay" -> Color(0xffFFEBB9)
                "XnhtKNbaOIHd1fVGRGlq" -> Color(0xffFFC3CF)
                "oleEOFjY5z9JMd4Pffw9" -> Color(0xffC3D4FF)
                else -> Color.LightGray
            }
        }

        fun getContentColor(
            emTypeId: String
        ):Color{
            return when (emTypeId) {
                "7RsOFLzwf5HMcMfCycGr" -> Color(0xff8E27C2)
                "DBcNw5u9DSdLtx0w5Yay" -> Color(0xffD49900)
                "XnhtKNbaOIHd1fVGRGlq" -> Color(0xffCD002A)
                "oleEOFjY5z9JMd4Pffw9" -> Color(0xff1254D6)
                else -> Color.LightGray
            }
        }
    }
}