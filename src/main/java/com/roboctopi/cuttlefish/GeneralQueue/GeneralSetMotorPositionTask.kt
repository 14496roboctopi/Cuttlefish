package com.roboctopi.cuttlefish.GeneralQueue

class GeneralSetMotorPositionTask(val position:Double, val task:GeneralMotorPositionTask,
                                  override var id: Int,
                                  override var dependencies: ArrayList<Int> = arrayListOf()
): GeneralTask
{
    override var failed: Boolean = false
    override fun onBegin(): Boolean {
        return true;
    }

    override fun loop(): Boolean
    {
        System.out.println("Set Motor Position to " + position);
        task.setAngle(position);
        return true;
    }

    override fun kill() {
    }
}