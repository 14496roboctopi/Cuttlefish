package com.roboctopi.cuttlefish.utils

import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

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
    fun getPerpVec():Pose
    {
        return Pose(-vy,vx,0.0);
    }
    fun getPosition(t:Double):Pose
    {
        return Pose(px+vx*t,py+vy*t,0.0);
    }
    fun getPerpDist():Double
    {
        var perp = getPerpVec();
        var dist = getPerpVec().getVecLen();
        var outerProd = perp.x*vy-perp.y*vx;
//        println("Outer: "+outerProd);
        return dist * sign(outerProd);
    }
    fun getParaDist():Double
    {
        this.normalize();
        return px*vx+py*vy;
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
        vx = vx*cos(-origin.r)-vy*sin(-origin.r);
        vy = vy*cos(-origin.r)+vx*sin(-origin.r);

        px = px*cos(-origin.r)-py*sin(-origin.r);
        py = py*cos(-origin.r)+px*sin(-origin.r);

        px -= origin.x;
        py -= origin.y;
    }
    fun getRelativeCoords(origin: Pose):Pose
    {
        var transLine = this.clone();
        transLine.normalize();
        transLine.setOrigin(origin);

        return Pose(-(transLine.px*transLine.vy-transLine.py*transLine.vx),-(transLine.vx*transLine.px+transLine.vy*transLine.py),0.0);
    }
    fun getDist(position: Pose):Double
    {
        var transLine = this.clone();
        transLine.normalize();

        return -transLine.vy*(position.x-transLine.px)+transLine.vx*(position.y-transLine.py)
    }



}