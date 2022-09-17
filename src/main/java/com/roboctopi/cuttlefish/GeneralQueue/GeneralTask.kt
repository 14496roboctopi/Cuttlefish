package com.roboctopi.cuttlefish.GeneralQueue

interface GeneralTask {
    var id:Int
    var dependencies:ArrayList<Int>
    var failed:Boolean
    fun onBegin():Boolean
    fun loop():Boolean
    fun kill():Unit
}