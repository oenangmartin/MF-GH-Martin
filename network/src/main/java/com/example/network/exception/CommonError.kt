package com.example.network.exception

data class CommonError(val resultCode: Int, override val message: String): Exception()