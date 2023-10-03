package com.roboctopi.cuttlefish.utils

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.PID


/**
 * Utility that uses a PID controller to control the position of a motor similarly to a servo.
 *
 * The default PID controller can be overridden using the setPid function.
 *
 * Using the scale variable motor positions can be set in units other than radians.
 * For example, the position of a lift could be set in mm of movement rather than radians of motor rotation.
 * Whenever the term "position" is used in this context it refers to the value with this scale applied.
 * Raw angle with be referred to as "angle".
 *
 * Positions are set relative to a "home" position. This home position can be set using the setHome function.
 *
 * @param goal Initial goal
 * @param motor Motor to be controller
 * @param enc Motor encoder
 * */
class MotorPositionController(var goal:Double, var motor: Motor, var enc:RotaryEncoder)
{
    public var pid:PID = PID(1.5,0.3,0.0);
    private var initial = 0.0;
    private var enabled = true;

    /**
     * Amount that position will be scaled to convert it to radians.
     * Useful for mechanisms like a lift where position should be set in mm instead of angle.
     * */
    var scale = 1.0;


    /**
     * If lowerLimitEnabled is set to true the position will not be set lower than this value.
     * This is applied post scaling meaning that if position is in mm this will also be in mm.
     * IMPORTANT: THIS WILL NOT BE APPLIED IF lowerLimitEnabled IS SET TO FALSE.
     * */
    var lowerLimit = 0.0;

    /**
     * If upperLimitEnabled is set to true the position will not be set higher than this value.
     * This is applied post scaling meaning that if position is in mm this will also be in mm.
     * IMPORTANT: THIS WILL NOT BE APPLIED IF upperLimitEnabled IS SET TO FALSE.
     * */
    var upperLimit = 0.0;

    /**
     * If set to true position will be clamped at maximum value of upperLimit
     * */
    var upperLimitEnabled = false;


    /**
     * If set to true position will be clamped at minimum value of lowerLimit
     * */
    var lowerLimitEnabled = false;

    /**
     * Update motor power based on new encoder data
     * */
    fun loop() {
        if(enabled)
        {
            motor.setPower(pid.update(enc.getRotation(),goal+initial));
        }
    }
    /**
     * Enable the position controller
     * */
    fun enable()
    {
        if(!enabled)
        {
            enabled = true;
            pid.reInit();
        }
    }
    /**
     * Disable the position controller
     * */
    fun disable()
    {
        enabled = false;
    }
    /**
     * Set target angle. As this directly controller the angle of the motor the setPosition is preferred.
     *
     * IMPORTANT: THIS WILL BYPASS THE POSITION LIMITS
     * */
    fun setAngle(angle:Double)
    {
        goal = angle;
    }

    /**
     * Set the motor target position. The scale variable can be used to set the units of position.
     * @see scale
     * */
    fun setPosition(position:Double)
    {
        goal = position*scale;
        if(position>upperLimit*scale&&upperLimitEnabled)
        {
            goal = upperLimit*scale;
        }
        if(goal<lowerLimit*scale&&lowerLimitEnabled)
        {
            goal = lowerLimit*scale;
        }
    }
    /**
     * Get the position of the arm (not angle)
     * */
    fun getPosition():Double
    {
        return (enc.getRotation() - initial)/scale;
    }

    /**
     * Set the home position (position = 0) of the motor to its current position. Useful in conjunction with a homing touch sensor.
     * */
    fun setHome()
    {
        initial = enc.getRotation();
    }
    /**
     * Offset the home position of the motor
     * @param offset Amount to offset
     * */
    fun offsetHome(offset:Double)
    {
        initial += scale*offset;
    }

    /**
     * Get the home position of the motor
     * */
    fun getHomePosition():Double
    {
        return initial/scale;
    }
    /**
     * Check if the motor is at its target.
     * @param epsilon Maximum offset from the goal which is still counted as being at the goal.
     * */
    fun isAtGoal(epsilon: Float): Boolean
    {
        //println(Math.abs(goal - (enc.getRotation() - initial)));
        return Math.abs(goal - (enc.getRotation() - initial)) < epsilon*scale;
    }
    /**
     * Get position of the encoder relative to the home position.
     * */
    fun getHomedEncoderPosition():Double
    {
        return (enc.getRotation() - initial);
    }
}