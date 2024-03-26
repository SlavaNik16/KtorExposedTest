package com.example.Services.Authentication.HTTPRequest

data class Request (
    val header: MutableMap<String,String>,
    val req: MutableMap<String,String>,
)