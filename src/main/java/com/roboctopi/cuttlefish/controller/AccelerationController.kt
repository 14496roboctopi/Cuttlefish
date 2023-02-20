package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.ThreeEncoderLocalizer
import com.roboctopi.cuttlefish.utils.Pose
import com.roboctopi.cuttlefish.utils.Vector2
import com.roboctopi.cuttlefish.utils.Vector3
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

class AccelerationController (){
    var friction: Double = 0.0;
    var speedConst: Double = 1.507;
    var accelConst: Double = 2.0;
    var profile : Vector2 = Vector2(1.2429182492,0.108877410042);

    var filterVelocity: Double = 0.0;
    var maxVelocity: Double = 1.0;
    var maxAccel:Double = 2.0;
    var accelScale:Double = 2.5;

    fun update(velocity:Double)
    {
        var power = Pose(0.0,0.0,0.0);
        var speedDiff = velocity-filterVelocity;
        if(!velocity.isNaN())
        {
            filterVelocity += speedDiff*0.4;
        }
    }


    private fun getPowerFromTarget(target: Double,): Double {
        var fSign = sign(filterVelocity);
        return (target + fSign * friction) / speedConst;
    }

    private fun getPowerAccel(accel: Double): Double {
        return getPowerFromTarget(accel / accelConst + filterVelocity);
    }

    public fun getPowerToGoal(position:Double,goal:Double): Double
    {
        var err = goal-position;
        var absErr = abs(err);
        var targetV =  profile.x * Math.pow(absErr, 1.0 / 2.0) + profile.y * absErr;
        println("TARGETVUNMIN: "+targetV)
        targetV = min(targetV,maxVelocity);
        val goalAccel: Double = Math.min(accelScale*(targetV - filterVelocity), maxAccel)* sign(err);
        println("TARGETV: "+targetV)
        println("REALV: "+filterVelocity)
        println("GOAL ACCEL: "+goalAccel);
        println("Error: "+err);
        if(filterVelocity<targetV*sign(err))
        {
            var power = getPowerAccel(goalAccel);
            println("POWER: "+power);
            return power;
        }
        else if(filterVelocity/targetV>1.4)
        {
            return 0.0;
        }
        else
        {
            return Math.signum(filterVelocity)*0.03;
        }
    }



}