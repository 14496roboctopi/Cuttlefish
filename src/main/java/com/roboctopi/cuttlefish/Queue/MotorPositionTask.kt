package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.PID

class MotorPositionTask(var pos:Double, var motor: Motor, var enc:RotaryEncoder, var relative:Boolean): Task
{
    override val persistant = true;
    var goal = pos;
    public var pid:PID = PID(1.5,0.3,0.0);
    private var initial = 0.0;
    var complete = false;
    private var enabled = true;

    override fun onBegin(): Boolean {
        if(relative)
        {
            initial = enc.getRotation();
        }
        return true;
    }

    override fun loop(): Boolean {
        if(pid.pGain == 2.0)
        {
            println("intake update")
            println(pid.power)
            println(initial)
        }
        if(enabled)
        {
            motor.setPower(pid.update(enc.getRotation(),goal+initial));
        }
        if(complete)
        {
            motor.setPower(0.0)
        }
        return complete;
    }
    fun enable()
    {
        enabled = true;
        pid.reInit();
    }
    fun disable()
    {
        enabled = false;
    }
    fun setAngle(angle:Double)
    {
        goal = angle;
    }
    fun setZeroPosition()
    {
        initial = enc.getRotation();
    }
    fun getZeroPosition():Double
    {
        return initial;
    }
    override fun kill()
    {
        complete = true;
    }
    fun isAtGoal(epsilon: Float): Boolean
    {
        //println(Math.abs(goal - (enc.getRotation() - initial)));
        return Math.abs(goal - (enc.getRotation() - initial)) < epsilon;
    }
    fun getZeroedEncoderPosition():Double
    {
        return (enc.getRotation() - initial);
    }
}