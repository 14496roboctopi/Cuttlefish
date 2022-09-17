package com.roboctopi.cuttlefish.Queue

class LogTask(var text: String): Task
{
    override val persistant = false;
    override fun onBegin(): Boolean
    {
        return true;
    }

    override fun loop(): Boolean
    {
        System.out.println(text);
        return true;
    }
}