package com.roboctopi.cuttlefish.localizer

import com.roboctopi.cuttlefish.utils.Pose

class NullLocalizer:Localizer {
    override var pos = Pose(0.0, 0.0, 0.0);
    override fun relocalize() {};
}