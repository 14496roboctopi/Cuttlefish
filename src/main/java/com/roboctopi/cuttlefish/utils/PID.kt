package com.roboctopi.cuttlefish.utils

import kotlin.math.max
import kotlin.math.min


/**
 * PID Controller
 * @param pGain Proportional gain
 * @param iGain Integral gain. Integral increases at a rate of (iGain * error) power per second.
 * @param dGain Derivative gain. Power is equal to dGain times error change per second.
 * */
open class PID(open var pGain: Double, open var iGain: Double, open var  dGain: Double, initial: Double = 0.0, public open var iLimit:Double = 1.0)
{
    private var pErr: Double = initial;
    open var p: Double = 0.0;
    open var i: Double = 0.0;
    open var d: Double = 0.0;
    open var f: Double = 0.0;

    open var reiniting = false;

    open var power: Double = 0.0;

    private var pTime: Long = System.nanoTime();

    /**
     * Update PID control loop
     * @param state New state
     * @param goal
     * */
    open fun update(state: Double, goal: Double = 0.0): Double
    {
        p = goal - state;

        if(reiniting)
        {
            reiniting = false
            pErr=p;
        }

        var tNano = System.nanoTime();

        var dT = (tNano - pTime).toDouble() / (1000.0*1000.0*1000.0);

        i += iGain * p * dT;
        i = max(min(i, iLimit), -iLimit);
        d = (p - pErr) / dT;
        power = pGain * p + i + d * dGain;

        pErr = p;
        pTime = tNano;
        return power;
    }
    /**
     * Update PID control loop
     * @param state New state
     * @param velocity Current rate of change of the state
     * @param goal
     * */
    open fun update(state: Double, velocity:Double,goal: Double = 0.0): Double
    {
        p = goal - state;

        if(reiniting)
        {
            reiniting = false
            pErr=p;
        }

        var tNano = System.nanoTime();

        var dT = (tNano - pTime).toDouble() / (1000.0*1000.0*1000.0);

        i += iGain * p * dT;
        i = max(min(i, iLimit), -iLimit);
        d = -velocity;
        power = pGain * p + i + d * dGain;

        pTime = tNano;
        return power;
    }

    /**
     * Clears the Integral and derivative values.
     * This should be used if a PID controller has not been in use for an extend period of time (more than a loop cycle) to avoid extreme integral build up.
     * */
    open fun reInit()
    {
        i=0.0;
        reiniting = true;
        pTime = System.nanoTime();
    }

}