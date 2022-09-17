package com.roboctopi.cuttlefish.GeneralQueue

class GeneralCustomTask(val onLoop: () -> Boolean, override var id: Int,
                        override var dependencies: ArrayList<Int>
): GeneralTask
{
    override var failed: Boolean = false

    override fun onBegin(): Boolean {
        return true;
    }

    override fun loop(): Boolean
    {
        return onLoop();
    }

    override fun kill() {
    }
}