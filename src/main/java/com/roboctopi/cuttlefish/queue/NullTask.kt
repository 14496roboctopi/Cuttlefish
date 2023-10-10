package com.roboctopi.cuttlefish.queue

/** Task that doesn't do anything*/
class NullTask: Task{
    override fun loop():Boolean
    {
        return true;
    }
}