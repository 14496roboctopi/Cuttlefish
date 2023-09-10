package com.roboctopi.cuttlefish.Queue

class CustomTask(val onLoop: () -> Boolean): Task
{
    override fun onBegin(): Boolean {
        return true;
    }
    override fun loop(): Boolean
    {
        return onLoop();
    }
}