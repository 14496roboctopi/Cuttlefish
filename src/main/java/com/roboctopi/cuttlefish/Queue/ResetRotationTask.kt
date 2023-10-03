package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.localizer.Localizer

/**
 * Set the localizer rotation.
 * Can be used to reset the localizer angle from an external source such as vision.
 * @param rotation
 * @param localizer
 * */
class ResetRotationTask(var rotation: Double, var localizer: Localizer): Task
{
    override fun onBegin(): Boolean
    {
        return true;
    }

    override fun loop(): Boolean
    {
        localizer.pos.r = rotation;
        return true;
    }
}