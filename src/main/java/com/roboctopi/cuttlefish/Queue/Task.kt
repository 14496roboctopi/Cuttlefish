package com.roboctopi.cuttlefish.Queue

interface Task
{
    val persistant:Boolean; // ?
    fun onBegin(): Boolean {return true};
    fun loop(): Boolean; // return true when done looping
    fun kill(): Unit{};
}