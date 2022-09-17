package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.controller.PTPController
import com.roboctopi.cuttlefish.controller.Waypoint
import com.roboctopi.cuttlefish.utils.Pose

class PointTask(val goal: Waypoint, val controller: PTPController): Task
{
    override val persistant = false;
    private var killed = false;
    override fun onBegin(): Boolean {
        controller.controller.rPID.reInit();
        return true;
    }
    override fun loop(): Boolean
    {
        if(killed)
        {
            return true;
        }
        else
        {
            return controller.gotoPointLoop(goal);
        }
    }
    override fun kill()
    {
        controller.controller.setVec(Pose(0.0,0.0,0.0));
        killed = true;
    }
    fun unkill()
    {
        killed = false;
    }
}