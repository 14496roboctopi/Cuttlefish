package com.roboctopi.cuttlefish.localizer

import com.roboctopi.cuttlefish.utils.Pose

//Test
interface Localizer {
    var pos: Pose;
    var velocity: Pose
    var localVelocity:Pose;

    var speed: Double
        get() = 0.0
        set(value) = TODO()

    fun update() {};
    fun reset(){};
}