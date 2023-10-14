# Module Cuttlefish


## Summary

## Components
All components in the Cuttlefish library such as Motors, Encoders, and Servos are interfaces allowing for the use of Cuttlefish on different platforms. This means that a small amount of code needs to be written to bridge Cuttlefish with the platform SDK. We provide the <a href="/CuttlefishFTCBridge/index.html">CuttlefishFTCBridge</a> library to do this as well as to provide additional functionality. If you wish to use Cuttlefish without the CuttlefishFTCBridge library you will have to write your own implementations for the <a href="/cuttlefish/com.roboctopi.cuttlefish.components/index.html">Cuttlefish components interfaces</a>.

***All examples provided will be utilizing the CuttlefishFTCBridge library.***

## Coordinate system and units
The robot starts at (0,0,0) <br>
Forward is positive Y <br>
Backward is negative Y <br>
Right is positive X <br>
Left is negative X <br>
Clockwise is negative R <br> 
Counter-clockwise is positive R <br> <br>
Translation units are mm <br>
Rotation units are radians 

## Getting Started
There are four primary systems in Cuttlefish that you will need / want to initialize:
- [Mecanum Controller][com.roboctopi.cuttlefish.controller.MecanumController]
- [Three Encoder Localizer][com.roboctopi.cuttlefish.localizer.ThreeEncoderLocalizer]
- [Point to Point Controller][com.roboctopi.cuttlefish.controller.PTPController]
- [Task Queue][com.roboctopi.cuttlefish.queue.TaskQueue]

All four of these systems should be initialized in your <a href = "/CuttlefishFTCBridge/index.html#initialized-opmode">Initialized opmode</a>. Here is a brief summary of that these systems do and how to initialize them.

### Mecanum Controller
The mecanum controller is the class that allows you to control your chassis. 
In order to initialize the mecanum controller you need to get all four of the chassis driver motors and pass them into the constructor: 
```java
CuttleMotor leftFrontMotor  = ctrlHub.getMotor(3);
CuttleMotor rightFrontMotor = ctrlHub.getMotor(2);
CuttleMotor rightBackMotor  = expHub .getMotor(2);
CuttleMotor leftBackMotor   = expHub .getMotor(3);

leftBackMotor .setDirection(Direction.REVERSE);
leftFrontMotor.setDirection(Direction.REVERSE);

MecanumController ctrlr = new MecanumController(rightFrontMotor,rightBackMotor,leftFrontMotor,leftBackMotor);
```
Make sure to set the direction of motors such that the robot will move forward when the power of each motor is positive. <br>
The mecanum controller can be used to control the robot using the setVec function. The setVec function takes a [Pose][com.roboctopi.cuttlefish.utils.Pose] object as an argument to describe the direction of motion. The Y value of the pose describes the power in the forward/backward direction, the X value of the pose describes the power in thn side to side direction, and the R value of the pose decribes the rotational power. Here is an example of how robot-centric driver control works using this system:
```java
public void mainLoop()
{
    chassis.setVec(new Pose(gamepad1.left_stick_x,-gamepad1.left_stick_y,gamepad1.right_stick_x));
}
```
Left stick Y is negative because positive Y is downward on the controller.

### Three Encoder Localizer
The three encoder localizer is used to determine the position of the robot using <a href="https://gm0.org/en/latest/docs/common-mechanisms/dead-wheels.html">deadwheel odometry</a>. To configure the odometry system you will need to know the the radius of the encoder wheel, and the distance between the two forward facing wheels. Here is an example of initialization:
```java
CuttleEncoder leftEncoder  = expHub .getEncoder(3,720*4);
CuttleEncoder sideEncoder  = ctrlHub.getEncoder(0,720*4);
CuttleEncoder rightEncoder = ctrlHub.getEncoder(3,720*4);
leftEncoder.setDirection(Direction.REVERSE);

ThreeEncoderLocalizer encoderLocalizer = new ThreeEncoderLocalizer(
        leftEncoder  ,
        sideEncoder  ,
        rightEncoder ,
        29, // Radius of the wheel in mm
        130.5, // Distance between the two forward facing wheels in mm
        1.0 //Calibration constant (see below)
);
```

#### Rotation Calibration
If you wish to improve rotational accuracy you can calibrate the odometry rotationally to account for any inaccuracy in your distance measurement between the two forward facing odometry wheels. This can be done by adjusting the rotaryCalibrationConstant which scales rotation. The rotaryCalibrationConstant can be determined by spining the robot a fixed number of times (e.g. 20 times) dividing the actual number of rotation by the number of times that the robot thinks that it rotated. Formula:
```
n: Actual number of rotations
r: Robot rotation as measured by localizer
C: Calibration constant

C = 2πn/r
```

### Point to Point Controller
The point to point controller is used to move the robot to waypoints on the playing field. It needs a mecanum controller and a localizer to be intialized:
```java
PTPController ptpController = new PTPController(chassis, encoderLocalizer);
```
The PTPController is rarely used on its own and is rather used alongside the queue using [PointTasks](com.roboctopi.cuttlefish.queue.PointTask):


### Task Queue
The task queue is a scheduling system that allows for the execution of a tree of tasks that are synchronized with the main loop. Many processes, such as moving the robot to a certain position are occur over an extended period of time, and require code to be run with every cycle of the main loop. When multiple processes like this have to occur in series and/or paralell it can be problematic. To solve this problem we have created the task queue system. <br>
The task queue as you might guess based on the name is a list of tasks. Each task is an object that implements the [Task][com.roboctopi.cuttlefish.queue.Task] interface. The task interface specifies that all tasks must have a loop function. The loop function of the task at the front of the queue will be executed every time the queue [update][com.roboctopi.cuttlefish.queue.TaskQueue.update] function is called, until it returns true, at which point it is discarded from the queue. The queue is first in, first out, meaning that tasks will be executed in the same order as they are added to the queue. <br>
The task queue has similar functionality to a state machine, but with several key advantages.:
- It has modular tasks meaning that common actions can easily be reused
- It is capable of executing multiple tasks concurrently 
- It structured linearly such that it is unlikely for unforseen behieviour to arise, while still allowing arbitrarily complex instructions
<br><br>

Here is an example of how you could use the task queue to move forward 1 meter, go sideways 1 meter while turning 90 degrees, open a servo to drop an element off, and then return to the robots original position diagonally:
```java
package org.firstinspires.ftc.teamcode.data_collection;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.roboctopi.cuttlefish.utils.Pose;
import org.firstinspires.ftc.teamcode.InitializedOpmode;

@TeleOp(name="Queue Example", group="Example")
public class QueueExample extends InitializedOpmode {
    
    public void onInit()
    {
        super.onInit();
    }
    public void main()
    {
        super.main();

        // Go forward 1000mm
        queue.addTask(new PointTask(
                new Waypoint(
                        new Pose(1000,0,0),
                        0.5
                ),
                ptpController
        )); 

        // Go sideways 1000mm and turn 0.5PI Radians (90 degrees)
        queue.addTask(new PointTask(
                new Waypoint(
                        new Pose(1000,1000,Math.PI/2),
                        0.5
                ),
                ptpController
        )); 

        // Open the claw 
        queue.addTask(new ServoTask(claw_servo,CLAW_OPEN));
        
        //Delay to make sure the servo has time to move
        queue.addTask(new DelayTask(400));

        //Drive back to the starting position
        queue.addTask(new PointTask(
                new Waypoint(
                        new Pose(0,0,0),
                        0.5
                ),
                ptpController
        ));
        
    }
    public void mainLoop()
    {
        super.mainLoop();
    }
}
```
If you want to use the queue with your own code you can either use a [CustomTask][com.roboctopi.cuttlefish.tasks.CustomTask] or create your own task. CustomTasks uses a lambda for the loop function as shown below :
```java
queue.addTask(new CustomTask(()->{
    // Custom code here
    return done;
}));
```
CustomTasks are useful in cases where you have some code that you want to run that you arent going to reuse, such setting the value of a spesific variable in your auto or . If there is something you will want to use more than once you make want 



#### Why not just use a while loop for each task?
The main problem with using while loops is that is complicates conncurrent execution. If you just put a while loop in the same thread as your main loop, you will halt the main loop while the secondary while loop is running. If you put it in a different thread then it will not be synchronized with the main thread meaning that you may be sending motor power multiple times before getting new data or geting new data multiple times without sending motor power significantly reducing the effective loop speed. 