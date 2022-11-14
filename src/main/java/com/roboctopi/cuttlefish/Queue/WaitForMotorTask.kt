package com.roboctopi.cuttlefish.Queue

class WaitForMotorTask(val motorCtrlr: MotorPositionTask,var epsilon:Float): Task {
    override val persistant: Boolean = false;

    override fun loop(): Boolean {
        return motorCtrlr.isAtGoal(epsilon);
    }
}