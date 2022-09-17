package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.utils.Pose

class Waypoint {
    var position: Pose = Pose(0.0,0.0,0.0);
    var rSlop:Double = 0.05;
    var tSlop:Double = 10.0;
    var isPassthrough:Boolean = false;
    var maxSpeed = 1.0;

    constructor(position: Pose,maxSpeed:Double, rSlop: Double = 0.05, tSlop: Double = 10.0,
                isPassthrough: Boolean = false)
    {
        this.position = position;
        this.rSlop = rSlop;
        this.tSlop = tSlop;
        this.maxSpeed = maxSpeed;
        this.isPassthrough = isPassthrough;
    }

    constructor(position: Pose,maxSpeed:Double)
    {
        this.position = position;
        this.maxSpeed = maxSpeed;
    }

    constructor(position: Pose)
    {
        this.position = position;
    }
}