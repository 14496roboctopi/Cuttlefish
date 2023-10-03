package com.roboctopi.cuttlefish.Queue

interface Task
{
    /** Called when task is started. Returning false will skip the task. */
    fun onBegin(): Boolean {return true};
    /** Called every loop cycle of the queue. Returning true moves on to next task. */
    fun loop(): Boolean; // return true when done looping
    /**Used to stop tasks part of the way through. */
    fun kill(): Unit{};
}