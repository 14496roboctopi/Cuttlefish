package com.roboctopi.cuttlefish.localizer

import com.roboctopi.cuttlefish.utils.Pose

//Test
interface Localizer {
    var pos: Pose;
    var speed: Double
        get() = 0.0
        set(value) = TODO()
    var rSpeed: Double
        get() = 0.0
        set(value) = TODO()

    fun relocalize() {};
    fun reset(){};
}