package com.example.githubexplorer.common.formatter

import io.kotest.core.spec.style.DescribeSpec
import org.junit.jupiter.api.Assertions.*

class NumberFormatterTest: DescribeSpec({
    lateinit var numberFormatter: NumberFormatter

    beforeEach {
        numberFormatter = NumberFormatter()
    }

    describe("getAbbreviatedNumberFormat") {
        context("when number is less than 1000") {
            it("should return the number as a string") {
                val result = numberFormatter.getAbbreviatedNumberFormat(500)
                assertEquals("500", result)
            }
        }
        context("when number is greater than or equal to 1000") {
            it("should return the number with 'k' appended") {
                val result = numberFormatter.getAbbreviatedNumberFormat(2000)
                assertEquals("2k", result)
            }
        }
        context("when number has a decimal part") {
            it("should return the number with 'k' appended") {
                val result = numberFormatter.getAbbreviatedNumberFormat(2800)
                assertEquals("2.8k", result)
            }
        }
    }
})