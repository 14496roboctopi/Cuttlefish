package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.Localizer
import com.roboctopi.cuttlefish.localizer.NullLocalizer
import com.roboctopi.cuttlefish.utils.Line
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose
import kotlin.math.abs
import kotlin.math.sign

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

    fun holdPosition(point: Pose,maxSpeed:Double,holdRotation: Boolean)
    {
        val direction:Pose = point.clone();
        direction.setOrigin(localizer.pos, true);
        direction.r = point.r;


        val dist: Double = direction.getVecLen();
        direction.normalize();

        power = -mPD.update(dist);

        direction.scale(Math.min(power,maxSpeed),false);
        controller.setVec(direction,  holdRotation, 1.0, localizer.pos.r);
    }

    var perpPID = PID(1.0/(100.0),0.0,0.0);
    fun followLine(line: Line,power:Double)
    {
        var guideLine = line.clone();
        guideLine.normalize();
        guideLine.subtract(localizer.pos);

        var paraVec    :Pose = guideLine.getParaVec();
        var perpVec    :Pose = guideLine.getPerpVec();
        perpVec.normalize();
        var perpDist = guideLine.getPerpDist();
        perpPID.update(perpDist,0.0)


//        System.out.println("Perp0:" + perpVec);
        perpVec.scale(abs(perpPID.power));
        paraVec.scale(power);
//        System.out.println("Perp1:" + perpVec)
//        System.out.println("Pos:" + localizer.pos);

        paraVec.add(perpVec,false);
        paraVec.rotate(-localizer.pos.r);
        paraVec.r = guideLine.getParaVec().r;

//        perpVec.scale(0.0);
//        paraVec.scale(0.0);

        controller.setVec(paraVec,true,0.4,localizer.pos.r);

        //Left: -1.835103951321012, Y: -0.0, R:0.0
        //Right:   Perp:X: -2.720985176212346, Y: 0.0, R:0.0

        // Outer: -217.0684841069536
    }
//    fun followLine(line: Line,power:Double,pPID: PID,rPID:PID)
//    {
//        var guideLine = line.clone();
//        guideLine.normalize();
//        guideLine.subtract(localizer.pos);
//
//        var paraVec    :Pose = guideLine.getParaVec();
//        var perpVec    :Pose = guideLine.getPerpVec();
//        perpVec.normalize();
//        var perpDist = guideLine.getPerpDist();
//        pPID.update(perpDist,0.0)
//
//
////        System.out.println("Perp0:" + perpVec);
//        perpVec.scale(abs(pPID.power));
//        paraVec.scale(power);
////        System.out.println("Perp1:" + perpVec)
////        System.out.println("Pos:" + localizer.pos);
//
//        paraVec.add(perpVec,false);
//        paraVec.rotate(-localizer.pos.r);
//        paraVec.r = guideLine.getParaVec().r;
//
//        rPID.update(-paraVec.r,localizer.pos.r);
//        paraVec.r = rPID.power;
//
////        perpVec.scale(0.0);
////        paraVec.scale(0.0);
//
//        controller.setVec(paraVec,false,0.4,localizer.pos.r);
//
//        //Left: -1.835103951321012, Y: -0.0, R:0.0
//        //Right:   Perp:X: -2.720985176212346, Y: 0.0, R:0.0
//
//        // Outer: -217.0684841069536
//    }
    fun followLine(line: Line,power: Double,angle:Double,pPID: PID,rPID:PID)
    {
        line.normalize();
        var relativeCoords = line.getRelativeCoords(localizer.pos);

        var sidePower = pPID.update(relativeCoords.x,0.0);

        var powerVec = line.getParaVec();
        var perp = line.getPerpVec();

        powerVec.scale(power);
        perp.scale(sidePower);
        powerVec.add(perp);
        rPID.update(localizer.pos.r,angle);
        powerVec.r = rPID.power;

        controller.setVec(powerVec);
    }
}