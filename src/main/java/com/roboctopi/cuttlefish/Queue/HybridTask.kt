package com.roboctopi.cuttlefish.Queue

class HybridTask(val task1:Task,val task2:Task): Task
{
    override val persistant = false;
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