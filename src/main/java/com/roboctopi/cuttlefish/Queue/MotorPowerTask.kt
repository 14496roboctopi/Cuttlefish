package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.PID

class MotorPowerTask(val power:Double,val motor:Motor): Task
{
    override fun onBegin(): Boolean {
        return true;
    }

    override fun loop(): Boolean {
        motor.setPower(power);
        return true;
    }
}