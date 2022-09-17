package com.roboctopi.cuttlefish.GeneralQueue

class GeneralConditionalTask(val condition: () -> Boolean, override var id:Int,
                             override var dependencies: ArrayList<Int>
): GeneralTask
{
    override var failed: Boolean = false

    override fun onBegin(): Boolean {
        return condition();
    }

    override fun loop(): Boolean
    {
        return true;
    }

    override fun kill() {
    }
}