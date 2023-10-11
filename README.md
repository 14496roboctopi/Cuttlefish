# Module Cuttlefish


## Summary

## Components
All componenets in the Cuttlefish library such as Motors, Encoders, and Servos are interfaces allowing for the use of Cuttlefish on different platforms. This means that a small amount of code needs to be written to bridge Cuttlefish with the platform SDK. We provide the <a href="/CuttlefishFTCBridge/index.html">CuttlefishFTCBridge</a> library to do this this which also provides additional functionality. If you wish to use Cuttlefish without the CuttlefishFTCBridge library you will have to write your own implementations for the <a href="/cuttlefish/com.roboctopi.cuttlefish.components/index.html">Cuttlefish componenets interfaces</a>.

***All examples provided will be utilizing the CuttlefishFTCBridge library.***

## Coordinate system
The robot starts at (0,0,0) <br>
Forward is positive Y <br>
Backward is negative Y <br>
Right is positive X <br>
Left is negative X <br>
Clockwise is negative R <br> 
Counter-clockwise is positive R <br> 

## Primary systems
There are four primary Cuttlefish systems:
- <a href="/cuttlefish/com.roboctopi.cuttlefish.controller/-mecanum-controller/index.html">Mecanum Controller</a>
- <a href="/cuttlefish/com.roboctopi.cuttlefish.localizer/-three-encoder-localizer/index.html">Three Encoder Localizer</a>
- <a href="/cuttlefish/com.roboctopi.cuttlefish.controller/-p-t-p-controller/index.html">Point to Point Controller</a>
- <a href="/cuttlefish/com.roboctopi.cuttlefish.queue/-task-queue/index.html">Task Queue</a>

All four of these systems should be initialized in your <a href = "/CuttlefishFTCBridge/index.html#initialized-opmode">Initialized opmode</a>

### Mecanum Controller

In order to initialize the mecanum controller you need to get all four of the chassis driver motors and pass them into the constructor
```java
CuttleMotor leftFrontMotor  = ctrlHub.getMotor(3);
CuttleMotor rightFrontMotor = ctrlHub.getMotor(2);
CuttleMotor rightBackMotor  = expHub .getMotor(2);
CuttleMotor leftBackMotor   = expHub .getMotor(3);

MecanumController ctrlr = new MecanumController(rightFrontMotor,rightBackMotor,leftFrontMotor,leftBackMotor);
```
The mecanum controller can be used to control the robot using the setVec function. The set vec function takes a <a href="/cuttlefish/com.roboctopi.cuttlefish.utils/-pose/index.html">Pose</a> object as an argument to describe the direction of motion. The Y value of the pose describes the power in the forward/backward direction, the X value of the pose describes the power in thn side to side direction, and the R value of the pose decribes the rotational power. Here is an example of how robot-centric driver control works using this system:
```java
public void mainLoop()
{
    chassis.setVec(new Pose(gamepad1.left_stick_x,-gamepad1.left_stick_y,gamepad1.right_stick_x));
}
```
Left stick Y is negative because positive Y is downward on the controller.

### Three Encoder Localizer

The three encoder localizer is used for encoder odometry

### Point to Point Controller

### Task Queue