package com.roboctopi.cuttlefish.components

class DummyMotor:Motor {
    public var lastPower:Double = 0.0;
    override fun setPower(power: Double)
    {
        lastPower = power;
    }

    override fun setDirection(direction: Boolean) {

    }
}