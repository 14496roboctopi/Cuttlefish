package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.DummyMotor
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose
import com.roboctopi.cuttlefish.utils.rotationDiff
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Class that stores chassis motors and can be used to steer the robot
 * @param rf_motor Right Front Motor
 * @param rb_motor Right Back Motor
 * @param lf_motor Left Front Motor
 * @param lb_motor Left Back Motor
 */
class MecanumBasic (var rf_motor: Motor, var rb_motor: Motor, var lf_motor: Motor, var lb_motor: Motor){
    /**
     * Set the direction that the robot is moving
     * @param direction Vector to apply power along
     * */
    fun setVec(direction: Pose)
    {
        val scale:Double = min(1.0/(abs(direction.x)+abs(direction.y)+abs(direction.r)),1.0);
        lf_motor.setPower((-direction.y-direction.x+direction.r)*scale);
        rf_motor.setPower((-direction.y+direction.x-direction.r)*scale);
        lb_motor.setPower((-direction.y+direction.x+direction.r)*scale);
        rb_motor.setPower((-direction.y-direction.x-direction.r)*scale);
    }
}