package com.example.searchapp.utils

import com.example.searchapp.utils.Constants.ARP_CURRENCY_SYMBOL
import java.text.DecimalFormat

private val formatter = DecimalFormat("###,###.##")

fun formatAmountTo(amount: String?) =
    "$ARP_CURRENCY_SYMBOL " + if (amount.isNullOrEmpty()) "0.00" else formatter.format(amount.toDouble())
