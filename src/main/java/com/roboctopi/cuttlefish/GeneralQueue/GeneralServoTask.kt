package com.roboctopi.cuttlefish.GeneralQueue

import com.roboctopi.cuttlefish.components.Servo

class GeneralServoTask(val servo: Servo, val position: Double,
                       override var id: Int,
                       override var dependencies: ArrayList<Int>
): GeneralTask
{
    override var failed: Boolean = false;

    override fun onBegin(): Boolean {
        return true;
    }

    override fun loop(): Boolean
    {
        servo.setPosition(position);
        return true;
    }

    override fun kill() {
    }
}