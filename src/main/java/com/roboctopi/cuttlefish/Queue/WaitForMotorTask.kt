package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.utils.MotorPositionController

class WaitForMotorTask(val motorCtrlr: MotorPositionController,var epsilon:Float): Task {
    override val persistant: Boolean = false;

    override fun loop(): Boolean {
        return motorCtrlr.isAtGoal(epsilon);
    }
}