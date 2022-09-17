package com.roboctopi.cuttlefish.Queue

class NullTask: Task{
    override val persistant = false;
    override fun loop():Boolean
    {
        return true;
    }
}