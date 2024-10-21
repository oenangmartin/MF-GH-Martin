package com.example.githubexplorer.common.formatter

private const val THOUSAND = 1_000

class NumberFormatter {
    /**
     * A function that will replace thousand using k
     * ie. 2000 -> 2k, 2800 -> 2.8k
     * This function currently doesn't support if the number is >= 1.000.000
     * (it will show as 1000k, which should be shown as 1.000k)
     */
    fun getAbbreviatedNumberFormat(number: Long): String {
        if (number < THOUSAND) return number.toString()
        val bigNumber = number / THOUSAND
        val remainder = number % THOUSAND / 100
        return if (remainder == 0L) "${bigNumber}k" else "$bigNumber.${remainder}k"
    }
}