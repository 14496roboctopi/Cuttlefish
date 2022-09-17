package com.roboctopi.cuttlefish.components

interface Servo {
    fun setPosition(position: Double);
    fun getPosition(): Double;
}