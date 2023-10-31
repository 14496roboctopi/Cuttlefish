package com.roboctopi.cuttlefish.localizer

import com.roboctopi.cuttlefish.utils.Pose

class NullLocalizer:Localizer {
    override var pos = Pose(0.0, 0.0, 0.0);
    override var velocity: Pose = Pose(0.0, 0.0, 0.0);
    override var localVelocity: Pose = Pose(0.0, 0.0, 0.0);
    override fun update() {};
}