package com.roboctopi.cuttlefish.Queue

/** Task that doesn't do anything*/
class NullTask: Task{
    override fun loop():Boolean
    {
        return true;
    }
}