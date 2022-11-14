package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.controller.MecanumController
import com.roboctopi.cuttlefish.controller.PTPController
import com.roboctopi.cuttlefish.controller.Waypoint
import com.roboctopi.cuttlefish.utils.Pose
import kotlin.math.abs

class RotateTask(var goal: Double, val relative:Boolean, val controller: PTPController): Task
{
    override val persistant = false;
    var complete = false;
    override fun onBegin(): Boolean
    {
        if(relative)
        {
            goal += controller.localizer.pos.r;
        }
        return true;
    }
    override fun loop(): Boolean
    {

        controller.controller.setVec(Pose(0.0,0.0,goal),true,0.45,controller.localizer.pos.r);


        if
        (
            Math.abs(controller.localizer.pos.r-goal) <= 0.015 ||
            (
                abs(controller.controller.rPID.power) < controller.controller.mecanumControllerPowerRoteAntiStallThreshold &&
                abs(controller.localizer.rSpeed) < controller.controller.mecanumControllerSpeedRoteAntiStallThreshold
            )
        )
        {
            println("RotationComplete pos: "+controller.localizer.pos.r+" goal:"+goal);
            complete = true;
        }

        if(complete)
        {
            controller.controller.setVec(Pose(0.0,0.0,0.0));
        }

        println("RotationIncomplete pos:"+controller.localizer.pos.r+" goal:"+goal);
        return complete;
    }
    override fun kill()

    {
        controller.controller.setVec(Pose(0.0,0.0,0.0));
        complete = true;
    }
    fun unkill()
    {
        complete = false;
    }
}