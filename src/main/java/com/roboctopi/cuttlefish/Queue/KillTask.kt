package com.roboctopi.cuttlefish.Queue

class KillTask(val task:Task): Task
{
    override fun loop(): Boolean
    {
        task.kill();
        return true;
    }
}