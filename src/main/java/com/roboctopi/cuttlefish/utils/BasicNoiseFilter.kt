package com.roboctopi.cuttlefish.utils

class BasicNoiseFilter(val smoothing:Double){
    var state:Double = 0.0;
    fun update(newState:Double)
    {
        state = smoothing*state + (1-smoothing)*newState;
    }
}