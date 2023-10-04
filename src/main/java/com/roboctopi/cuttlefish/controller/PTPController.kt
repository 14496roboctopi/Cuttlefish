package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.Localizer
import com.roboctopi.cuttlefish.utils.Line
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose
import kotlin.math.PI
import kotlin.math.abs

class  PTPController(var controller:MecanumController, var localizer: Localizer)
{
    //Var init
    var translational_PD_ctrlr:PID = PID(0.005, 0.0, 0.1);
    var rotational_PID_ctrlr:PID = PID(PI * 0.5,0.0,2.0);
    var movePowerAntistallThreshold = 0.2;
    var moveSpeedAntistallThreshold = 0.015;
    var rotatePowerAntistallThreshold = 0.0;
    var rotateSpeedAntistallThreshold = 0.0;
    var rotationPowerLimit = 1.0;

    var power:Double = 0.0
        private set


    fun gotoPointLoop(point:Waypoint, endPoint: Pose = Pose(0.0, 0.0, 0.0)): Boolean {
        //Find translation power
        val direction:Pose = point.position.clone();
        direction.setOrigin(localizer.pos, true);

        val dist: Double = direction.getXYLength();
        direction.normalize();

        power = -translational_PD_ctrlr.update(dist);

        direction.scale(power.coerceAtMost(point.maxPower),false);

        direction.r = rotational_PID_ctrlr.update(localizer.pos.r,point.position.r);
        direction.r = direction.r.coerceIn(-rotationPowerLimit,rotationPowerLimit);


        val motionStalled = abs(power) < movePowerAntistallThreshold && localizer.speed<moveSpeedAntistallThreshold;
        val rotationStalled = abs(direction.r) < rotatePowerAntistallThreshold && abs(localizer.rSpeed) < rotateSpeedAntistallThreshold;

        val rotationReached = abs(localizer.pos.r - direction.r) < point.rSlop;
        val positionReached = dist < point.tSlop;


        return if( (motionStalled && rotationStalled) || (rotationReached && positionReached) )
        {
            rotational_PID_ctrlr.i = 0.0;
            if(!point.isPassthrough) controller.setVec(Pose(0.0, 0.0, 0.0));
            true;
        }
        else
        {
            controller.setVec(direction);
            false;
        }
    }

    fun holdPosition(point: Pose,maxSpeed:Double,holdRotation: Boolean)
    {
        val direction:Pose = point.clone();
        direction.setOrigin(localizer.pos, true);
        direction.r = point.r;


        val dist: Double = direction.getXYLength();
        direction.normalize();

        power = -translational_PD_ctrlr.update(dist);

        direction.scale(Math.min(power,maxSpeed),false);
        controller.setVec(direction);
    }



    //TODO: THIS MUST BE REIMPLIMENTED - Logan 2023-09-10
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
        perpVec.scale(perpPID.power);
        paraVec.scale(power);
//        System.out.println("Perp1:" + perpVec)
//        System.out.println("Pos:" + localizer.pos);

        paraVec.add(perpVec,false);
        paraVec.rotate(-localizer.pos.r);
        paraVec.r = guideLine.getParaVec().r;

//        perpVec.scale(0.0);
//        paraVec.scale(0.0);

        controller.setVec(paraVec);

        //Left: -1.835103951321012, Y: -0.0, R:0.0
        //Right:   Perp:X: -2.720985176212346, Y: 0.0, R:0.0

        // Outer: -217.0684841069536
    }
    //TODO: THIS MUST BE REIMPLIMENTED - Logan 2023-09-10
    fun followLine(line: Line,power:Double,pPID: PID)
    {
        var guideLine = line.clone();
        guideLine.normalize();
        guideLine.subtract(localizer.pos);

        var paraVec    :Pose = guideLine.getParaVec();
        var perpVec    :Pose = guideLine.getPerpVec();
        perpVec.normalize();
        var perpDist = guideLine.getPerpDist();
        pPID.update(perpDist,0.0)


//        System.out.println("Perp0:" + perpVec);
        perpVec.scale(pPID.power);
        println("Dist: "+perpDist+", power:"+abs(pPID.power)+", PerpVec: "+perpVec);
        paraVec.scale(power);
//        System.out.println("Perp1:" + perpVec)
//        System.out.println("Pos:" + localizer.pos);

        paraVec.add(perpVec,false);
        paraVec.rotate(-localizer.pos.r);
        paraVec.r = guideLine.getParaVec().r;

//        rPID.update(-paraVec.r,localizer.pos.r);
//        paraVec.r = rPID.power;

//        perpVec.scale(0.0);
//        paraVec.scale(0.0);

        controller.setVec(paraVec);


        //Left: -1.835103951321012, Y: -0.0, R:0.0
        //Right:   Perp:X: -2.720985176212346, Y: 0.0, R:0.0

        // Outer: -217.0684841069536
    }
//    fun followLine(line: Line,power: Double,angle:Double)
//    {
//        line.normalize();
//        var relativeCoords = line.getRelativeCoords(localizer.pos);
//
//        var sidePower = perpPID.update(-relativeCoords.x,0.0);
//
//        var powerVec = line.getParaVec();
//        var perp = line.getPerpVec();
//
//        powerVec.scale(power);
//        perp.scale(sidePower);
//        powerVec.add(perp);
////        rPID.update(localizer.pos.r,angle);
//        powerVec.r = angle-localizer.pos.r;
//
//        controller.setVec(powerVec);
//    }
}