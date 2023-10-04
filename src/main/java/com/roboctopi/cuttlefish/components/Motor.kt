package com.roboctopi.cuttlefish.components

import com.roboctopi.cuttlefish.utils.Direction

interface Motor {
    fun setPower(power: Double);
    fun setDirection(direction: Direction);
}