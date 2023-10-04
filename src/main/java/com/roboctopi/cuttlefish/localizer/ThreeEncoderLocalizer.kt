package com.roboctopi.cuttlefish.localizer

import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.Pose
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * @param left Left encoder
 * @param side Side encoder
 * @param right Right encoder
 * @param wheelRad Radius of the encoder wheels
 * @param wheelDist Distance between the two forward wheels
 *
 */
class ThreeEncoderLocalizer(left: RotaryEncoder, side: RotaryEncoder, right: RotaryEncoder,
                            wheelRad: Double, wheelDist: Double,
                            rotaryCalibrationConstant: Double): Localizer
{
    //Var init
    val l: RotaryEncoder = left;
    val s: RotaryEncoder = side;
    val r: RotaryEncoder = right;

    val calibConst = rotaryCalibrationConstant;

    var rad: Double = wheelRad;
    var dist: Double = wheelDist;

    var localSpeed:Pose = Pose(0.0,0.0,0.0);

    //Position var
    override var pos: Pose = Pose(0.0,0.0,0.0);

    //Preivious Enc Position
    var pEnc: Pose = Pose(l.getRotation(),r.getRotation(),s.getRotation());

    //Privious position
    public  var pPos: Pose = Pose(0.0,0.0,0.0);

    //Previous time
    private var pTime: Long = System.currentTimeMillis();

    //Speed
    override var speed = 0.0;
    override var rSpeed = 0.0;

    var sidesum = 0.0;

    init {
        this.relocalize();
    }
    override fun reset()
    {
        pEnc = Pose(l.getRotation(),r.getRotation(),s.getRotation());
        pPos = Pose(0.0,0.0,0.0);
        pTime = System.currentTimeMillis();
        pos = Pose(0.0,0.0,0.0);
    }

    /**
     * Update robot position based on encoder data
     */
    override fun relocalize()
    {
        //Gets enc position
        val nEnc = Pose(l.getRotation(), r.getRotation(), s.getRotation());

        //Encoder step
        val encStep: Pose = nEnc.clone();
        encStep.subtract(this.pEnc, true);

        //Movement step
        val moveStep: Pose = this.calcMovementStep(encStep);

        //Blending steps
        val newPos = this.blendSteps(this.pos, moveStep);

        //Setting the position variable to the new position
        this.pos.x = newPos.x;
        this.pos.y = newPos.y;
        this.pos.r = newPos.r;

        //Gets time the last step took
        val t = System.currentTimeMillis();
        val dTime = t - pTime;

        //Calculates the amount the robot has moved
        val dMove = this.pos.clone();
        dMove.subtract(this.pPos,true);

        //Calculates speed
        speed = dMove.getXYLength() / dTime;
        localSpeed = moveStep;
        localSpeed.scale((1.0 / dTime),true);
        rSpeed = dMove.r / (dTime/1000.0);

        //Sets previous variables
        pTime = t;
        pPos = this.pos.clone();

        this.pEnc.x = nEnc.x;
        this.pEnc.y = nEnc.y;
        this.pEnc.r = nEnc.r;

    }

    //Magic black box
    private fun blendSteps(cPos: Pose, step: Pose): Pose
    {
        val newPos = Pose(cPos.x, cPos.y, cPos.r + step.r * calibConst);

        val transDir = atan2(step.y, step.x);

        val d = step.getXYLength();
        var a = step.r;
        if(a == 0.0) a = 0.000000000000001;
        val r = d / a;

        //Investigate first term *r
        val localArc = Pose(cos(0.5 * PI - a) * r, sin(0.5 * PI - a) * r - r);



        //Investigate newPos
        localArc.rotate(transDir + cPos.r);

        newPos.add(localArc);

        return newPos;
    }
//    private fun blendSteps(cPos: Pose, step: Pose): Pose
//    {
//        val newPos = Pose(cPos.x, cPos.y, cPos.r + step.r * calibConst);
//        newPos.add(step);
//
//        return newPos;
//    }

    private fun calcMovementStep(encStep: Pose): Pose
    {
        //Var init
        val step = Pose(0.0,0.0,0.0);

        //Converts rotation into distance traveled
        encStep.scale(this.rad, true);

        //Forward motion
        step.y = 0.5 * (encStep.x + encStep.y);

        //Rotational motion
        step.r = (encStep.y - encStep.x) / (this.dist);

        //Sideways motion
        step.x = encStep.r;

        sidesum += step.x;


        return step;
    }
}
