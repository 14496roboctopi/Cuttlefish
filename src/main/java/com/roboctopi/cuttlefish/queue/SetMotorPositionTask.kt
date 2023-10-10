package com.roboctopi.cuttlefish.queue

import com.roboctopi.cuttlefish.utils.MotorPositionController

/**
 * Set the position of a motor using a MotorPositionController
 * @see MotorPositionController
 * @param position target position
 * @param ctrlr Motor position controller
 * */
class SetMotorPositionTask(val position:Double, val ctrlr:MotorPositionController): Task
{
    override fun loop(): Boolean
    {
        System.out.println("Set Motor Position to " + position);
        ctrlr.setAngle(position);
        return true;
    }
}