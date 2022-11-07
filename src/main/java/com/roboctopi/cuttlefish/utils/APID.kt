package com.roboctopi.cuttlefish.utils

import kotlin.math.max
import kotlin.math.min

class APID(var pGain: Double, var iGain: Double, var  dGain: Double, initial: Double = 0.0)
{
    private var pErr: Double = initial;
    var p: Double = 0.0;
    var i: Double = 0.0;
    var d: Double = 0.0;

    var iLimit:Double = 1.0
    var iGrowthLimit:Double = 1.0

    var dFilterCoeffiecient = 0.0;
    var dLimit = 1.0;


    var reiniting = false;

    public var power: Double = 0.0;
    private var pTime: Long = System.currentTimeMillis();

    init {
        //System.out.println(i)
        //System.out.println(iGain)
    }
    //TODO: Set up custom anti-wind
    fun update(state: Double, goal: Double = 0.0): Double
    {
        p = goal - state;

        if(reiniting)
        {
            System.out.println("Reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeinit")
            reiniting = false
            pErr=p;
        }

        var t = System.currentTimeMillis();

        var iSpeed = Math.max(Math.min(p,iGrowthLimit),-iGrowthLimit) ;
        iSpeed *= ((t - pTime).toDouble() / 1000);

        i += iGain*iSpeed;
        i = max(min(i, iLimit), -iLimit);

        var pureD = dGain*(p - pErr) / (t - pTime);
        pureD = Math.min(Math.max(pureD,-dLimit),dLimit)
        d = (1.0-dFilterCoeffiecient)*pureD+dFilterCoeffiecient*d;


        power = pGain * p + i + d;

        // @Note(sean) what the-???
        /*if(iGain==100.0)
        {
            println(i);
        }*/

        pErr = p;
        pTime = t;
        return power;
    }

    fun reInit()
    {
        i=0.0;
        reiniting = true;
        pTime = System.currentTimeMillis();
    }

    //TODO: This can be removed
    fun reset(goal: Double = 0.0, initial: Double = 0.0)
    {
        i = 0.0;
        pErr = goal - initial;
    }
}