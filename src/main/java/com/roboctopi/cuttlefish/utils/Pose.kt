package com.roboctopi.cuttlefish.utils

import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin


class Pose(var x: Double = 0.0, var y: Double = 0.0, var r: Double = 0.0)
{
    fun getVecLen(): Double
    {
        return hypot(x, y);
    }

    fun clone(): Pose
    {
        return Pose(this.x, this.y, this.r);
    }

    fun add(input: Pose, rotate: Boolean = false)
    {
        this.x += input.x;
        this.y += input.y;
        if(rotate)
        {
            this.r += input.r;
        }
    }

    fun stepAlongR(amount: Double, offset: Double = 0.0)
    {
        this.x += cos(this.r + offset) * amount;
        this.y += sin(this.r + offset) * amount;
    }

    fun setOrigin(input: Pose, rotate: Boolean = true)
    {
        this.x -= input.x;
        this.y -= input.y;
        if(rotate)
        {
            this.rotate(-input.r);
            this.r -= input.r;
        }
    }

    fun subtract(input: Pose, rotate: Boolean = true)
    {
        this.x -= input.x;
        this.y -= input.y;
        if(rotate)
        {
            this.r -= input.r;
        }
    }

    fun scale(amount: Double, rotate: Boolean = false)
    {
        this.x *= amount;
        this.y *= amount;

        if(rotate)
        {
            this.r *= amount;
        }
    }

    fun normalize()
    {
        var len = this.getVecLen();
        if(len == 0.0) len = 0.000001;
        this.x /= len;
        this.y /= len;
    }

    fun rotate(amount:Double,pivot:Pose = Pose(0.0,0.0,0.0))
    {
        //Creates translated and normalized point
        val tPoint = Pose(this.x - pivot.x, this.y - pivot.y, this.r);
        var len = tPoint.getVecLen();
        if (len == 0.0) len = 0.000001;
        tPoint.normalize();

        //Rotates tPoint and scales it back up
        this.x = (tPoint.x * cos(amount) - tPoint.y * sin(amount)) * len + pivot.x;
        this.y = (tPoint.y * cos(amount) + tPoint.x * sin(amount)) * len + pivot.y;
    }
}
/*
int a = 5;
int *pointer = &a;
(*pointer) = 6;

printf("%p\n",pointer);





* */

