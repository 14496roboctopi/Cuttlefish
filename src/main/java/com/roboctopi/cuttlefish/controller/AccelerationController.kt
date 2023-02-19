package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.ThreeEncoderLocalizer
import com.roboctopi.cuttlefish.utils.Pose
import java.nio.DoubleBuffer

class AccelerationController (val localizer: ThreeEncoderLocalizer){
    var friction: Pose = Pose(0.12,0.054,0.0);
    var speedConst: Pose = Pose(1.3025,1.507,0.0);
    var accelConst: Pose = Pose(4.0,4.0,0.0);
    var smoothSpeed: Pose = Pose(0.0,0.0,0.0);

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
            speedDiff.scale(0.1);
            smoothSpeed.add(speedDiff);
        }

        power.x = getPowerAccel(targetAccel.x,smoothSpeed.x,accelConst.x,speedConst.x,friction.x);
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
        return getPowerFromTarget(accel / aConst + speed,Math.signum(speed),sConst,fConst);
    }


}