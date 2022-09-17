package com.roboctopi.cuttlefish.GeneralQueue

import com.roboctopi.cuttlefish.components.Motor

class GeneralMotorPowerTask(var motor: Motor,var power:Double, override var id:Int,
                                override var dependencies: ArrayList<Int>
): GeneralTask
{
    override var failed: Boolean = false
    var t: Long = 0;
    var count = 0;
    override fun onBegin(): Boolean
    {
        return true;
    }

    override fun loop(): Boolean
    {
        motor.setPower(power);
        count++;
        return true;
    }

    override fun kill() {
    }
}