package com.roboctopi.cuttlefish.Queue

class SetMotorPositionTask(val position:Double, val task:MotorPositionTask): Task
{
    override val persistant = false;

    override fun loop(): Boolean
    {
        System.out.println("Set Motor Position to " + position);
        task.setAngle(position);
        return true;
    }
}