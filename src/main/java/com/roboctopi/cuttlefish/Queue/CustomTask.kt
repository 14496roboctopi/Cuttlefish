package com.roboctopi.cuttlefish.Queue

/**
 * Task that allows you to execute code using a lambda
 * @param onLoop Lambda be executed every loop cycle that returns a boolean
 * */
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