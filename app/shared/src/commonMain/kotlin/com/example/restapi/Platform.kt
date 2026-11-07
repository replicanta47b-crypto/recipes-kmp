package com.example.restapi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform