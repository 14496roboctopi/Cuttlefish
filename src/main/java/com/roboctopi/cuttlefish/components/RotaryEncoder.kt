package com.roboctopi.cuttlefish.components

interface RotaryEncoder {
    fun getRotation(): Double
    fun getVelocity(): Double
}