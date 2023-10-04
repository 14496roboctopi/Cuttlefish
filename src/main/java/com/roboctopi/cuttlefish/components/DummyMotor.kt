package com.roboctopi.cuttlefish.components

import com.roboctopi.cuttlefish.utils.Direction

class DummyMotor:Motor {
    public var lastPower:Double = 0.0;
    override fun setPower(power: Double)
    {
        lastPower = power;
    }

    override fun setDirection(direction: Direction) {

    }
}