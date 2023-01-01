package com.roboctopi.cuttlefish.Queue

/**
 * Pause for a specified amount of time in a queue
 * @param delay Amount of time to delay in milliseconds
 */
class DelayTask(var delay: Int): Task
{
    var t: Long = 0;
    override val persistant = false;
    override fun onBegin(): Boolean
    {
        t = System.currentTimeMillis();
        System.out.println("Begin! at " + t);
        return true;
    }

    override fun loop(): Boolean
    {
        System.out.println("End! at " + (System.currentTimeMillis() - t));
        return (System.currentTimeMillis() - t) >= delay;
    }
}