package com.roboctopi.cuttlefish.utils

import kotlin.math.sign

class Line(var px:Double, var py:Double, var vx:Double, var vy:Double)
{
    fun length():Double
    {
        return Math.sqrt(vx*vx+vy*vy);
    }
    fun normalize()
    {
        var len = length();
        vx/=len;
        vy/=len;
    }
    fun getParaVec():Pose
    {
        return Pose(vx,vy,Math.atan2(-vx,vy));
    }
    fun getPosition(t:Double):Pose
    {
        return Pose(px+vx*t,py+vy*t,0.0);
    }
    fun getPerpVec():Pose
    {
        var t:Double = -(vx*px+vy*py)/(vx*vx+vy*vy);

        return getPosition(t);
    }
    fun getPerpDist():Double
    {
        var perp = getPerpVec();
        var dist = getPerpVec().getVecLen();
        var outerProd = perp.x*vy-perp.y*vx;
        println("Outer: "+outerProd);
        return dist * sign(outerProd);
    }
    fun clone():Line
    {
        return Line(px,py,vx,vy);
    }
    fun subtract(origin:Pose)
    {
        px -= origin.x;
        py -= origin.y;
    }
    fun setOrigin(origin: Pose)
    {

    }

}