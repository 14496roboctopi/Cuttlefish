package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.components.Servo

class ServoTask(val servo: Servo, val position: Double): Task
{
    override fun loop(): Boolean
    {
        servo.setPosition(position);
        return true;
    }
}