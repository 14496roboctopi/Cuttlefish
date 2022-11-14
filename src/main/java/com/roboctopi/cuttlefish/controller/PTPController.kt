package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.Localizer
import com.roboctopi.cuttlefish.localizer.NullLocalizer
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose
import kotlin.math.abs

class  PTPController
{
    //Var init
    var controller = MecanumController();
    var localizer:Localizer = NullLocalizer();

    var mPD:PID = PID(0.005, 0.0, 0.1);
    var dir:Pose = Pose(0.0, 0.0, 0.0);
    var rPos:Pose = Pose(0.0, 0.0, 0.0);
    var pPos:Pose = Pose(0.0, 0.0, 0.0);
    var distance:Double = 0.0;
    var power:Double = 0.0;

    var movePowerThreshold = 0.2;
    var moveSpeedThreshold = 0.015;

    constructor(mecController: MecanumController,
                localizer: Localizer)
    {
        controller = mecController;
        this.localizer = localizer;
    }

    constructor(mecController: MecanumController,
                localizer: Localizer,movePD:PID,
                movePowerThreshold:Double,
                moveSpeedThreshold:Double)
    {
        controller = mecController;
        this.localizer = localizer;
        mPD = movePD;
        this.movePowerThreshold = movePowerThreshold;
        this.moveSpeedThreshold = moveSpeedThreshold;
    }

    fun gotoPointLoop(point:Waypoint, endPoint: Pose = Pose(0.0, 0.0, 0.0)): Boolean {
        val direction:Pose = point.position.clone();
        direction.setOrigin(localizer.pos, true);
        direction.r = point.position.r;
        rPos = localizer.pos;
        pPos = point.position;


        val dist: Double;
        dist = direction.getVecLen();
        direction.normalize();

        power = -mPD.update(dist);

        direction.scale(Math.min(power,point.maxSpeed),false);

        //return if((Math.abs(power) > 0.2||localizer.speed>0.015) &&(Math.abs(localizer.pos.r-direction.r)>point.rSlop||dist>point.tSlop))



        //TODO: Antistall is being bad: mecanumControllerSpeedRoteAntiStallThreshold is used twice
        return if((abs(power) > movePowerThreshold
                        || localizer.speed>moveSpeedThreshold
                        || abs(controller.rPID.power) > controller.mecanumControllerSpeedRoteAntiStallThreshold
                        || abs(localizer.rSpeed) > controller.mecanumControllerSpeedRoteAntiStallThreshold)
                && (abs(localizer.pos.r - direction.r) > point.rSlop
                        || dist > point.tSlop))
        {
            controller.setVec(direction,  true, 3.0, localizer.pos.r);
            false;
        }
        else
        {
            controller.rPID.i = 0.0;
            if(!point.isPassthrough) controller.setVec(Pose(0.0, 0.0, 0.0));
            true;
        }
    }
}