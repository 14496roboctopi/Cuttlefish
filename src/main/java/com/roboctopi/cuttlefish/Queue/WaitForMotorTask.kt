package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.utils.MotorPositionController

class WaitForMotorTask(val motorCtrlr: MotorPositionController,var epsilon:Float): Task {

    override fun loop(): Boolean {
        return motorCtrlr.isAtGoal(epsilon);
    }
}