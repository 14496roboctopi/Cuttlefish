package com.roboctopi.cuttlefish.Queue

/** Task that kills another task.
 * @see ForkTask
 * @param task
 * */
class KillTask(val task:Task): Task
{
    override fun loop(): Boolean
    {
        task.kill();
        return true;
    }
}