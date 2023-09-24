package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.controller.PTPController
import com.roboctopi.cuttlefish.utils.Pose
import kotlin.math.abs


/**Rotate the robot to a set angle or by a set amount
 * @param goal Target angle in radians
 * @param relative If true robot will rotate relative to its current rotation and if false it will rotate to the goal orientation
 * @param controller Point to point controller
 * */
class RotateTask(var goal: Double, val relative:Boolean, val controller: PTPController): Task
{
    var complete = false;
    override fun onBegin(): Boolean
    {
        if(relative)
        {
            goal += controller.localizer.pos.r;
        }
        return true;
    }
    //TODO:Stuff is commented
    override fun loop(): Boolean
    {

//        controller.controller.setVec(Pose(0.0,0.0,goal),true,0.45,controller.localizer.pos.r);


//        if
//        (
//            Math.abs(controller.localizer.pos.r-goal) <= 0.04 &&
//            (
//                abs(controller.controller.rPID.power) < controller.controller.mecanumControllerPowerRoteAntiStallThreshold &&
//                abs(controller.localizer.rSpeed) < controller.controller.mecanumControllerSpeedRoteAntiStallThreshold
//            )
//        )
//        {
////            println("RotationComplete pos: "+controller.localizer.pos.r+" goal:"+goal);
//            complete = true;
//        }
//
//        if(complete)
//        {
//            controller.controller.setVec(Pose(0.0,0.0,0.0));
//        }

//        println("RotationIncomplete pos:"+controller.localizer.pos.r+" goal:"+goal);
        return complete;
    }
    override fun kill()

    {
        controller.controller.setVec(Pose(0.0,0.0,0.0));
        complete = true;
    }
    /**Allow task to continue after being killed. Must be re-added to queue for this to work*/
    fun unkill()
    {
        complete = false;
    }
}