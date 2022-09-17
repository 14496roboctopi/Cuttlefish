package com.roboctopi.cuttlefish.components

interface Motor {
    fun setPower(power: Double);
    fun setDirection(direction: Boolean);
}