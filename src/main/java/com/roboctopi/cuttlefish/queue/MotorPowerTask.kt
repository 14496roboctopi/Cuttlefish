package com.roboctopi.cuttlefish.queue

import com.roboctopi.cuttlefish.components.Motor

/** Set the power of a motor
 * @param power
 * @param motor
 * */
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