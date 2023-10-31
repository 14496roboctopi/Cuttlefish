package com.roboctopi.cuttlefish.queue

import com.roboctopi.cuttlefish.controller.MotorPositionController

/**
 * Wait for a motor position controller to reach its target position
 * @param motorCtrlr
 * @param epsilon Size of the region around the goal that is considered at the target
 */
class WaitForMotorTask(val motorCtrlr: MotorPositionController, var epsilon:Float): Task {

    override fun loop(): Boolean {
        return motorCtrlr.isAtGoal(epsilon);
    }
}