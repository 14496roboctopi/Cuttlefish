package com.roboctopi.cuttlefish.utils

/**
 * Basic noise filter.
 * Works by setting the state based on the following equation:
 *
 * S = S*a + (1-a) * d;
 *
 * S: State
 *
 * a: Smoothing coefficient
 *
 * d: New (noisy) data
 *
 * @param smoothing Smoothing coefficient from 0-1. 0 is no smoothing and 1 is flat(data isn't added)
 * */
class ExponentialFilter(val smoothing:Double){
    var state:Double = 0.0;
    /**
     * Update the filter with new data
     * @param data
     * */
    fun update(data:Double)
    {
        state = smoothing*state + (1-smoothing)*data;
    }
}