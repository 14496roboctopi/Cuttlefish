package com.roboctopi.cuttlefish.GeneralQueue

class GeneralDelayTask(var delay: Int, override var id:Int,
                       override var dependencies: ArrayList<Int>
): GeneralTask
{
    override var failed: Boolean = false
    var t: Long = 0;
    override fun onBegin(): Boolean
    {
        t = System.currentTimeMillis();
        System.out.println("Begin! at " + t);
        return true;
    }

    override fun loop(): Boolean
    {
        //System.out.println("End! at " + (System.currentTimeMillis() - t));
        return (System.currentTimeMillis() - t) >= delay;
    }

    override fun kill() {
    }
}