package com.roboctopi.cuttlefish.queue

/** Runs two tasks concurrently. Will wait for both tasks to complete before moving on. This can be used with TaskList for complex behavior
 * @see com.roboctopi.cuttlefish.queue.TaskList
 * @param task1
 * @param task2
 * */
class ForkTask(val task1:Task, val task2:Task): Task
{
    var t1Complete = false;
    var t2Complete = false;
    override fun onBegin(): Boolean
    {
        task1.onBegin();
        task2.onBegin();
        return true;
    }

    override fun loop(): Boolean
    {
        if(task1.loop()&&!t1Complete)
        {
            t1Complete = true;
        }
        if(task2.loop()&&!t2Complete)
        {
            t2Complete = true;
        }

        return t1Complete&&t2Complete;
    }
}