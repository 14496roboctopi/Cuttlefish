package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.utils.PID

class VelocityController(p: Double, i: Double, d: Double,var feedForwardM:Double,var feedForwardB:Double)
{
    private var pPos: Double = 0.0;
    private var pT: Long = System.nanoTime();
    var pid: PID = PID(p,i,d)
    var goal: Double = 0.0;
    private var speed:Double = 0.0;
    private var power:Double = 0.0;

    fun getSpeed():Double
    {
        return speed;
    }
    fun getPower():Double
    {
        return power;
    }


    fun update(pos:Double):Double
    {
        var f = Math.signum(goal)*(Math.abs(goal)-feedForwardB)/feedForwardM;
        if(goal == 0.0)
        {
            f = 0.0;
        }

        var t = System.nanoTime();

        var dPos = pos - pPos;
        var dTNano = t - pT;

        pPos = pos;
        pT = t;

        var dT: Double = dTNano.toDouble()/(1000.0*1000.0*1000.0);
        speed = dPos/dT;

        pid.update(speed,goal);

        power = pid.power+f

        return power;
    }
}