package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.ThreeEncoderLocalizer
import com.roboctopi.cuttlefish.utils.Pose
import com.roboctopi.cuttlefish.utils.Vector3
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

class AccelerationControllerBulky (val localizer: ThreeEncoderLocalizer){
    var friction: Pose = Pose(0.12,0.054*0.0,0.0);
    var speedConst: Pose = Pose(1.2,1.507,0.0);
    var accelConst: Pose = Pose(3000.0,2200.0,0.0);
    var smoothSpeed: Pose = Pose(0.0,0.0,0.0);

    var yConfig :Vector3 = Vector3(43.0171,39.9144,2461.56)

    fun reinit()
    {
        smoothSpeed = localizer.localSpeed;
    }
    fun update(targetAccel:Pose):Pose
    {
        var power = Pose(0.0,0.0,0.0);
        var speedDiff = localizer.localSpeed;
        if(!speedDiff.x.isNaN()&&!speedDiff.y.isNaN())
        {
            speedDiff.subtract(smoothSpeed,true);
            speedDiff.scale(0.4);
            smoothSpeed.add(speedDiff);
        }

//        power.x = getPowerAccel(targetAccel.x,smoothSpeed.x,accelConst.x,speedConst.x,friction.x);
        power.y = getPowerAccel(targetAccel.y,smoothSpeed.y,accelConst.y,speedConst.y,friction.y);
        System.out.println(power)
        System.out.println("Smooth: "+smoothSpeed)
        return power;
    }

    private fun getPowerFromTarget(target: Double,fSign:Double, sConst:Double, fConst: Double): Double {
        val friction = fConst;
        return (target + fSign * friction) / sConst;
    }

    private fun getPowerAccel(accel: Double,speed: Double,aConst: Double, sConst:Double, fConst: Double): Double {
        var fSign = Math.signum(speed);
        return getPowerFromTarget(accel / aConst + speed,fSign,sConst,fConst);
    }

    public fun getPowerToGoal()
    {

    }

    public fun getAccel(velocity:Double,position:Double,goal:Double,maxVelocity:Double,maxAccel:Double,accelScale:Double,profile:Vector3):Double
    {
        var err = goal-position;
        var absErr = abs(err);
        var targetV = 1.0 / profile.x * Math.pow(absErr, 1.0 / 3.0) + 1.0 / profile.y * Math.pow(absErr, 1.0 / 2.0) + 1.0 / profile.z * absErr;
        targetV = min(targetV,maxVelocity);
        val goalAccel: Double = Math.min(accelScale*(targetV - velocity), maxAccel)* sign(err);
        return goalAccel;
    }



}