package com.example.resq.helper

import com.example.resq.R

class ContactTypeIcon {
    companion object {
        fun getIconId(
            contactType: String
        ): Int? {
            return when (contactType) {
                "wa" -> R.drawable.icon_whatsapp
                "phone" -> R.drawable.icon_phone
                "office" -> R.drawable.icon_office_phone
                else -> null
            }
        }
    }
}