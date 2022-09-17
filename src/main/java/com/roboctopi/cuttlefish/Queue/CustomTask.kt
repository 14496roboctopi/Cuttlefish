package com.roboctopi.cuttlefish.Queue

class CustomTask(val onLoop: () -> Boolean,override val persistant:Boolean): Task
{
    override fun onBegin(): Boolean {
        return true;
    }
    override fun loop(): Boolean
    {
        return onLoop();
    }
}