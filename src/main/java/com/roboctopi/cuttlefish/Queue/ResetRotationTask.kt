package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.localizer.Localizer


class ResetRotationTask(var rotation: Double, var localizer: Localizer): Task
{
    override val persistant = false;

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