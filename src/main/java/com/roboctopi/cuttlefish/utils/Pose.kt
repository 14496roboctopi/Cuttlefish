package com.roboctopi.cuttlefish.utils

import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Class describing a position and rotation in 2D.
 * This class can also be used to describe things like 2D velocity or chassis power.
 * @param x X Position
 * @param y Y Position
 * @param r Rotation in radians
 * */
class Pose(var x: Double = 0.0, var y: Double = 0.0, var r: Double = 0.0)
{

    /**
     * Get the euclidean length of the XY component of the pose
     * */
    fun getXYLength(): Double
    {
        return hypot(x, y);
    }

    /**
     * Clone the pose
     * */
    fun clone(): Pose
    {
        return Pose(this.x, this.y, this.r);
    }

    /**
     * Add another pose to this pose
     * @param input Pose to add
     * @param rotate If set to true the rotation component will be added
     * */
    fun add(input: Pose, rotate: Boolean = false)
    {
        this.x += input.x;
        this.y += input.y;
        if(rotate)
        {
            this.r += input.r;
        }
    }

    /**
     * Move the XY of the pose in the direction of R
     * @param distance distance to move
     * @param offset Offset from R
     * */
    fun stepAlongR(distance: Double, offset: Double = 0.0)
    {
        this.x += cos(this.r + offset) * distance;
        this.y += sin(this.r + offset) * distance;
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
        var len = this.getXYLength();
        if(len == 0.0) len = 0.000001;
        this.x /= len;
        this.y /= len;
    }

    fun rotate(amount:Double,pivot:Pose = Pose(0.0,0.0,0.0))
    {
        //Creates translated and normalized point
        val tPoint = Pose(this.x - pivot.x, this.y - pivot.y, this.r);
        var len = tPoint.getXYLength();
        if (len == 0.0) len = 0.000001;
        tPoint.normalize();

        //Rotates tPoint and scales it back up
        this.x = (tPoint.x * cos(amount) - tPoint.y * sin(amount)) * len + pivot.x;
        this.y = (tPoint.y * cos(amount) + tPoint.x * sin(amount)) * len + pivot.y;
    }

    override fun toString():String
    {
        return "X: "+x+", Y: "+y+", R:"+r;
    }

}
/*
int a = 5;
int *pointer = &a;
(*pointer) = 6;

printf("%p\n",pointer);





* */

