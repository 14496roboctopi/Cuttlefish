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

    /**
     * Change the reference frame of the to another pose.
     * Equivalent to subtract operation but with an added rotation transform.
     * @param origin New origin
     * @param rotate If set to true, the pose will be rotated around the origin by the origins R value
     * */
    fun setOrigin(origin: Pose, rotate: Boolean = true)
    {
        this.x -= origin.x;
        this.y -= origin.y;
        if(rotate)
        {
            this.rotate(-origin.r);
            this.r -= origin.r;
        }
    }

    /**
     * Subtract a this pose by another pose.
     * @param input Pose to subtract from this pose
     * @param rotate If false the rotation value will remain unaffected
     * */
    fun subtract(input: Pose, rotate: Boolean = true)
    {
        this.x -= input.x;
        this.y -= input.y;
        if(rotate)
        {
            this.r -= input.r;
        }
    }

    /**
     * Scale the pose
     * @param scalar
     * @param rotate if set to false rotation will not be affected
     * */
    fun scale(scalar: Double, rotate: Boolean = false)
    {
        this.x *= scalar;
        this.y *= scalar;

        if(rotate)
        {
            this.r *= scalar;
        }
    }

    /**
     * Normalize the position of the pose
     * */
    fun normalize()
    {
        var len = this.getXYLength();
        if(len == 0.0) len = 0.000001;
        this.x /= len;
        this.y /= len;
    }

    /**
     * Rotate the position of the pose around a pivot.
     * NOTE: This does not affect the R value of the pose
     * @param amount Amount to rotate
     * @param pivot position to pivot around
     * */
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

