package com.codingwithjks.retrofithttprequest.util

interface Listener {

    fun onClick(position:Int,busId:String)
    fun openDialog(position: Int,busId: String,town:String)
}