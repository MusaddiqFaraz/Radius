package com.faraz.app.radius.data_manager.api


enum class Source{
    Database,
    NETWORK
}

enum class TYPE {
    SUCCESS,
    ERROR
}
data class Resource<out T>(val type: TYPE,val source: Source,val data: T?)
