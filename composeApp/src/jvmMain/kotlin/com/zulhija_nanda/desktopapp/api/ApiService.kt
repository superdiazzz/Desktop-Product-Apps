//package com.zulhija_nanda.desktopapp.api
//
//
//
//class ApiService {
//
//    private val httpClient = HttpClient {
//        install(ContentNegotiation){
//            json(Json {
//                prettyPrint = true
//                isLenient = true
//                ignoreUnknownKeys = true
//            })
//
//        }
//
//        install(HttpTimeout){
//            requestTimeoutMillis = 15000
//        }
//    }
//
//    suspend fun fetchData(): String {
//        val result = httpClient.get {
//            url {
//                protocol = URLProtocol.HTTPS
//                host = "dummyjson.com"
//                path("test")
//            }
//        }
//        return if(result.status.isSuccess()){
//            result.bodyAsText()
//        }else result.status.description
//    }
//}