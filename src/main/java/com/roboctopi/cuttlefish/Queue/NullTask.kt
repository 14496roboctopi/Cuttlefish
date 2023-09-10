package com.roboctopi.cuttlefish.Queue

class NullTask: Task{
    override fun loop():Boolean
    {
        return true;
    }
}