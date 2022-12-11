package com.roboctopi.cuttlefish.utils

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.PID

class MotorPositionController(var pos:Double, var motor: Motor, var enc:RotaryEncoder, var relative:Boolean)
{
    var goal = pos;
    public var pid:PID = PID(1.5,0.3,0.0);
    private var initial = 0.0;
    var complete = false;
    private var enabled = true;
    var scale = 1.0;
    var lowerLimit = 0.0;
    var upperLimit = 0.0;
    var upperLimitEnabled = false;
    var lowerLimitEnabled = false;

    fun onBegin(): Boolean {
        if(relative)
        {
            initial = enc.getRotation();
        }
        return true;
    }

    fun loop(): Boolean {
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
        if(!enabled)
        {
            enabled = true;
            pid.reInit();
        }
    }
    fun disable()
    {
        enabled = false;
    }
    fun setAngle(angle:Double)
    {
        goal = angle;
    }
    fun setPosition(position:Double)
    {
        goal = position*scale;
        if(position>upperLimit&&upperLimitEnabled)
        {
            println("limiting")
            goal = upperLimit*scale;
        }
        if(goal<lowerLimit&&lowerLimitEnabled)
        {
            goal = lowerLimit;
        }
    }
    fun getPosition():Double
    {
        return (enc.getRotation() - initial)/scale;
    }

    fun setZeroPosition()
    {
        initial = enc.getRotation();
    }
    fun getZeroPosition():Double
    {
        return initial;
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