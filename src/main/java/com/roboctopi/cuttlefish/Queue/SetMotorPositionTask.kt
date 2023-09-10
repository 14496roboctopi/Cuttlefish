package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.utils.MotorPositionController

class SetMotorPositionTask(val position:Double, val ctrlr:MotorPositionController): Task
{
    override fun loop(): Boolean
    {
        System.out.println("Set Motor Position to " + position);
        ctrlr.setAngle(position);
        return true;
    }
}