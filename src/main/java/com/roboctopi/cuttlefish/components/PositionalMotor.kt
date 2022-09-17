package com.roboctopi.cuttlefish.components

interface PositionalMotor:Motor {
    fun setPosition(position: Double);
}