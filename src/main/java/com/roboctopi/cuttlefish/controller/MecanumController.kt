package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.NullMotor
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.min


class MecanumController{
    var rfm:Motor = NullMotor();
    var rbm:Motor = NullMotor();
    var lfm:Motor = NullMotor();
    var lbm:Motor = NullMotor();
    var rPID = PID(PI * 0.5,1500.0,2.0);
    var rote:Double = 0.0;
    var mecanumControllerPowerRoteAntiStallThreshold = 0.11;
    var mecanumControllerSpeedRoteAntiStallThreshold = 0.5;
    var rPowerDebug = 0.0;

    constructor();

    constructor(rightFront: Motor, rightBack: Motor, leftFront: Motor, leftBack: Motor,
                rotePID: PID, mecanumControllerPowerRoteAntiStallThreshold: Double, mecanumControllerSpeedRoteAntiStallThreshold: Double)
    {
        rfm = rightFront;
        rbm = rightBack;
        lfm = leftFront;
        lbm = leftBack;
        rPID = rotePID;
        this.mecanumControllerPowerRoteAntiStallThreshold = mecanumControllerPowerRoteAntiStallThreshold;
        this.mecanumControllerSpeedRoteAntiStallThreshold = mecanumControllerSpeedRoteAntiStallThreshold;
        // System.out.println(rPID.i)
    }

    constructor(rightFront: Motor, rightBack: Motor, leftFront: Motor,leftBack: Motor)
    {
        System.out.println(rPID.i)
        rfm = rightFront;
        rbm = rightBack;
        lfm = leftFront;
        lbm = leftBack;
    }

    fun setVec(direction: Pose, power: Double = 1.0, holdRote:Boolean = false, maxRotationPriority:Double = 1.0, rotation: Double = 0.0)
    {
        if(!holdRote)
        {
            val scale:Double = min(power/(abs(direction.x)+abs(direction.y)+abs(direction.r)),1.0);
            lfm.setPower((-direction.y-direction.x+direction.r)*scale);
            rfm.setPower((-direction.y+direction.x-direction.r)*scale);
            lbm.setPower((-direction.y+direction.x+direction.r)*scale);
            rbm.setPower((-direction.y-direction.x-direction.r)*scale);
        }
        else
        {
            rote = direction.r;
            rPID.update(rotation,rote);
            var r = min(rPID.power,maxRotationPriority);
            if(abs(r) < mecanumControllerPowerRoteAntiStallThreshold) r = 0.0;

            rPowerDebug = r;

            val scale:Double = min(power/(abs(direction.x)+abs(direction.y)+abs(r)),1.0);
            lfm.setPower((-direction.y-direction.x+r)*scale);
            rfm.setPower((-direction.y+direction.x-r)*scale);
            lbm.setPower((-direction.y+direction.x+r)*scale);
            rbm.setPower((-direction.y-direction.x-r)*scale);
        }
    }
}