package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.controller.MecanumController
import com.roboctopi.cuttlefish.controller.PTPController
import com.roboctopi.cuttlefish.controller.Waypoint
import com.roboctopi.cuttlefish.utils.Pose

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

        controller.controller.setVec(Pose(0.0,0.0,goal),1.0,true);


        if(Math.abs(controller.localizer.pos.r-goal) >= 0.05)
        {
            println("b");
            complete = true;
        }

        if(complete)
        {
            controller.controller.setVec(Pose(0.0,0.0,0.0));
        }

        println("a");
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