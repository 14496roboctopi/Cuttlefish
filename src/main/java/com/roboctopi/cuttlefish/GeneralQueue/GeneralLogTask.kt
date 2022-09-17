package com.roboctopi.cuttlefish.GeneralQueue

class GeneralLogTask(var text: String, override var id:Int,
                     override var dependencies: ArrayList<Int>
): GeneralTask
{
    override var failed: Boolean = false
    override fun onBegin(): Boolean
    {
        return true;
    }

    override fun loop(): Boolean
    {
        System.out.println(text);
        return true;
    }

    override fun kill() {
    }
}