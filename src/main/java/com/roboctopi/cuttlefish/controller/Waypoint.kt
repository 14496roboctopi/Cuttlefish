package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.utils.Pose

class Waypoint {
    var position: Pose = Pose(0.0,0.0,0.0);
    var rSlop:Double = 0.05;
    var tSlop:Double = 10.0;
    var isPassthrough:Boolean = false;
    var maxPower = 1.0;

    /**
     * @param position Target position
     * @param maxPower Maximum power that will be applied during motion
     * @param rSlop Maximum allowable +- rotational error in radians
     * @param tSlop Maximum allowable translational error in mm
     * @param isPassthrough Setting to true will prevent motors from turning off in-between points
     * */
    constructor(position: Pose, maxPower:Double, rSlop: Double = 0.05, tSlop: Double = 10.0,
                isPassthrough: Boolean = false)
    {
        this.position = position;
        this.rSlop = rSlop;
        this.tSlop = tSlop;
        this.maxPower = maxPower;
        this.isPassthrough = isPassthrough;
    }

    /**
     * @param position Target position
     * @param maxPower Maximum power that will be applied during motion
     * */
    constructor(position: Pose,maxPower:Double)
    {
        this.position = position;
        this.maxPower = maxPower;
    }

    /**
     * @param position Target position
     * */
    constructor(position: Pose)
    {
        this.position = position;
    }
}