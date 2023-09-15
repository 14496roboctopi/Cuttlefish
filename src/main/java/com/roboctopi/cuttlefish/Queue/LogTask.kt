package com.roboctopi.cuttlefish.Queue

/** Log a string to the console using System.out.println
 * @param text
 **/
class LogTask(var text: String): Task
{
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