package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.PID

/** Task that controls the velocity of a motor using a PID controller. Stop task using kill function.
 * @param target Target Velocity
 * @param motor Motor
 * @param encoder Motor Encoder
 * @param pid PID controller for the velocity
 * */
class MotorVelocityTask(var target:Double, val motor: Motor,
                        val encoder: RotaryEncoder, val pid: PID): Task
{
    var complete = false;

    var pT   = System.currentTimeMillis();
    var pPos = encoder.getRotation();
    var power = 0.0;

    override fun loop(): Boolean
    {
        var t = System.currentTimeMillis();
        var pos = encoder.getRotation();
        pid.update((((pos-pPos)/(t-pT))/(Math.PI*2))*60000,target)
        if(target == 0.0)
        {
            power = 0.0;
        }
        else
        {
            power = pid.power;
        }
        motor.setPower(power);
        pT = t;
        pPos = pos;


        return complete;
    }
    /**
     * Set the target velocity
     * @param speed Speed
     * */
    fun setSpeed(speed:Double)
    {
        target = speed;
    }
    override fun kill()
    {
        complete = true;
    }

}