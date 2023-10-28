# Module Cuttlefish


## Summary

## Installation

### Basic

### Advanced
Here is how to add cuttlefish to your project directly as a repository. This is useful if you are planning to modify the library yourself

Clone Cuttlefish into the top level folder of your project. This can be done with the following command in git bash:
```bash
git clone https://github.com/14496roboctopi/CuttlefishFTCBridge
```
If you have forked the library then you can use the URL to your library instead. Note: Copy and pasting into the windows git bash is sometimes buggy, meaning that if you copy and paste this into the windows git bash it might throw an error if you don't retype certain parts of it.

Another option instead of cloning the library in directly is to add it as a submodule. This is useful if you are using git for your project as it tells git to retrive the project from a seperate repository rather than including it in your main repoisitory. It can be added as a submodule using the following command:
```bash
git submodule add https://github.com/14496roboctopi/CuttlefishFTCBridge
```
DO NOT RUN THIS COMMAND IN ADDITION TO THE FIRST COMMAND. 
If you choose to go with this option then you will need to push and pull the submodule seperately from the rest of your git. You will also need to run the commands git submodule init, and git submodule update whenever you set up a new copy of the repo on a different computer in order to pull the submodule into your project.

Next, look for your project level build.gradle file. It can be found under gradle scripts and it should say (Project: the_name_of_your_project) in parentheses after build.gradle. In this file in the dependencies block add the following line:
```groovy
classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10'
```
This tells gradle to include the kotlin plugin in your project.

Now open settings.gradle which can also be found under gradle scripts and add the following line:
```groovy
include ':cuttlefish'
```
This tells gradle that the cuttlefish folder is a module in your project.

Finally, locate the TeamCode build.gradle file. This is the build.gradle file that says (Moduke: TeamCode) in parentheses. In the dependencies block of this file add the following line:
```groovy
implementation project(path: ':cuttlefish')
```
This adds cuttlefish as a dependency of your teamcode module allowing it to be used in teamcode.
Make sure that after making changes to the gradle scripts that you click `sync now` in the top right of your code window.

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
public CuttleMotor leftFrontMotor ;
public CuttleMotor rightFrontMotor;
public CuttleMotor rightBackMotor ;
public CuttleMotor leftBackMotor  ;
MecanumController chassis;

@Override
public void onInit()
{
        leftFrontMotor  = ctrlHub.getMotor(3);
        rightFrontMotor = ctrlHub.getMotor(2);
        rightBackMotor  = expHub .getMotor(2);
        leftBackMotor   = expHub .getMotor(3);

        leftBackMotor .setDirection(Direction.REVERSE);
        leftFrontMotor.setDirection(Direction.REVERSE);

        chassis = new MecanumController(rightFrontMotor,rightBackMotor,leftFrontMotor,leftBackMotor);
}

```
Make sure to set the direction of motors such that the robot will move forward when the power of each motor is positive. <br>
The mecanum controller can be used to control the robot using the setVec function. The setVec function takes a [Pose][com.roboctopi.cuttlefish.utils.Pose] object as an argument to describe the direction of motion. The Y value of the pose describes the power in the forward/backward direction, the X value of the pose describes the power in thn side to side direction, and the R value of the pose decribes the rotational power. Here is an example of how robot-centric driver control works using this system:
```java
public void mainLoop()
{
    chassis.setVec(new Pose(gamepad1.left_stick_x,-gamepad1.left_stick_y,-gamepad1.right_stick_x));
}
```
Left stick Y is negative because positive Y is downward on the controller.

### Three Encoder Localizer
The three encoder localizer is used to determine the position of the robot using <a href="https://gm0.org/en/latest/docs/common-mechanisms/dead-wheels.html">deadwheel odometry</a>. To configure the odometry system you will need to know the the radius of the encoder wheel, and the distance between the two forward facing wheels. Here is an example of initialization:
```java
CuttleEncoder leftEncoder ;
CuttleEncoder sideEncoder ;
CuttleEncoder rightEncoder;

ThreeEncoderLocalizer encoderLocalizer;

@Override
public void onInit()
{
        leftEncoder  = expHub .getEncoder(3,720*4);
        sideEncoder  = ctrlHub.getEncoder(0,720*4);
        rightEncoder = ctrlHub.getEncoder(3,720*4);
        leftEncoder.setDirection(Direction.REVERSE);

        encoderLocalizer = new ThreeEncoderLocalizer(
                leftEncoder  ,
                sideEncoder  ,
                rightEncoder ,
                29, // Radius of the wheel in mm
                130.5, // Distance between the two forward facing wheels in mm
                1.0 //Calibration constant (see below)
        );
}
```
In order to use the ThreeEncoderLocalizer you will need to call the relocalize function in your main loop. You can get the positon of the robot as a [Pose][com.roboctopi.cuttlefish.utils.Pose] using the getPos function. 

```java
@Override
public void mainLoop()
{
        encoderLocalizer.relocalize();
        System.out.println(encoderLocalizer.getPos());
        telemetry.addData("Localizer X:",encoderLocalizer.getPos().getX());
        telemetry.addData("Localizer Y:",encoderLocalizer.getPos().getY());
        telemetry.addData("Localizer R:",encoderLocalizer.getPos().getR());
        telemetry.update();
}

```
#### Testing
One you have initialized your localizer you will need to test it to make sure everything is configured correctly. To do this, you will need to set up a way to log the localizer position. You can do this through telemetry as shown above, you can do it through [FTCDashboard](https://acmerobotics.github.io/ftc-dashboard/features#telemetry) if you have it installed, or you can do it through System.out.println if you are connected over wireless ADB. Once you have some way of logging the localizer position, you should run the following tests:

1. Restart the code and then push the robot directly forward and observe the localizer position. The Y coordinate should increase, and the X and R coordinates should remain near zero. If the Y coordinate decreases (goes negative), while X and R stay near zero, reverse both of your forward facing encoders using the [setDirection](/CuttlefishFTCBridge/com.roboctopi.cuttlefishftcbridge.devices/-cuttle-encoder/set-direction.html) function.  If the R coordinate increases instead of Y, try reversing just one of the forward facing encoders. If this doesn't work, either your encoder ports are configured incorrectly, or one or more of your encoders are disconnected. You can debug this by logging the angle of each encoder and then spinning the wheels by hand to make sure they are all connected and match up.
2. Restart the code and push the bot directly to the right and observe the localizer position. The X coordinate should increase. If the X coordinate decreases, reverse the sideways encoder. If the X coordinate doesnt change, or the other coordinates change significantly, your encoders are wired incorrectly.
3. Restart the code and rotate the robot counter-clockwise by 90 degrees. The R coordinate of the localizer should increase by approximately 1.57 (90 degrees in radians). If it instead decreases, make the calibration constant negative to reverse the direction of rotation.
4. Restart the code and spin your robot around 10 times. Make sure that that you do this precisly and align the robot to something with a fixed rotation such as a tile before and after rotating it. The R coordinate of your localizer should be equal to 20*pi. If it varies from this significantly (more than ~0.2-0.5) you should calibrate rotation (see below)
5. Restart the code and push the robot forward a set distance (e.g. 1 meter) and make sure that the distance reported by the localizer is the same as the distance the robot actually moved. Repeat this with the robot moving sideways.
6. Place the robot at the edge of a field tile and restart the code. Push the robot directly forward by a meter or two, rotate the bot 90 degrees, and then push the robot back to its starting position. Once it is back where it started, rotate the bot 90 degrees back to its original orientation and line it up with the field tile. The localizer should report that the position is at (0X, 0Y, 0R) +- a few centimeters or +- a few 0.01 radians.



#### Rotation Calibration
If you wish to improve rotational accuracy you can calibrate the odometry rotationally to account for any inaccuracy in your distance measurement between the two forward facing odometry wheels. This can be done by adjusting the rotaryCalibrationConstant which scales rotation. The rotaryCalibrationConstant can be determined by spining the robot a fixed number of times (e.g. 20 times) dividing that number(n) by the number of times that the robot thinks that it rotated(r). Remember that the localizer gives an output of radians so you will have to convert those to rotations by dividing by 2pi.
```
n: Actual number of rotations
a: Robot angle as determined by the localizer
r: Robot rotation as measured by localizer
C: Calibration constant

r = a/(2π)
C = n/r = 2πn/a
```

### Point to Point Controller
The point to point controller is used to move the robot to waypoints on the playing field. It needs a mecanum controller and a localizer to be intialized:
```java
PTPController ptpController = new PTPController(chassis, encoderLocalizer);
```
The PTPController is rarely used on its own and is rather used alongside the queue using [PointTasks](com.roboctopi.cuttlefish.queue.PointTask):
```java
queue.addTask(new PointTask(
        new Waypoint(
                new Pose(1000,0,0),
                0.5 // Maximum power
        ),
        ptpController
)); 
```
Point tasks allow you to move the robot to a position on the playing field and then to move on to another task.

#### Tuning
There are several parameters that can be tuned in the Point to Point controller. These are the PID Controllers, and the antistall system. There is two PID controllers. First is the rotational PID controller, which controls the angle of the bot, and second the translational PD controller which controls the position of the bot. The translational PD controlled does not have a signed error meaning that ***the I gain must be set to zero***. Here is how you set the controller coefficients:
```java
ptpController.setRotational_PID_ctrlr(new PID(rotational_p_gain,rotational_i_gain,rotational_d_gain));
ptpController.setTranslational_PD_ctrlr(new PID(translation_p_gain,0,translation_d_gain));
```
The second tunable is the anti-stall system. The PTPController that detects if the robot is stalling, and if it is stalling, moves on the the next point. This prevents the robot from getting hung up trying to reach a target in auto. The robot is considered stalled if the PD / PID power is below a minimum threshold AND the bots positional and rotational speed is below a certain threshold. These thresholds can be manually tuned for your bot:
```java
ptpController.getAntistallParams().setMovePowerAntistallThreshold(0.15); // Maxmimum translational power where the bot is still stalled
ptpController.getAntistallParams().setRotatePowerAntistallThreshold(0.15); // Maxmimum rotation power where the bot is still stalled
ptpController.getAntistallParams().setMoveSpeedAntistallThreshold(0.015);  // Maximum speed in m/s for the bot to be considered stalled
ptpController.getAntistallParams().setRotateSpeedAntistallThreshold(0.3); // Maximum rotation speed in rad/s for the bot to be considered stalled
```
Remember that ALL of these conditions must be met for the bot to be considered stalled.

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
CustomTasks are useful in cases where you have some code that you want to run that you arent going to reuse, such setting the value of a spesific variable in your auto. If there is something you will want to use more than once you may want to instead create a new task type by creating a class which implements the Task interface:
```java
class ServoPresetTask(val servo: CuttleServo, val preset: Int): Task
{
    override fun loop(): Boolean
    {
        servo.goToPreset(preset);
        return true;
    }
}
```


#### Conditionals
Conditionals can be implemented through the use of task lists and custom tasks. TaskLists are essentially TaskQueues that can be added as tasks. They will update the same way a normal queue would when their loop function is called and are complete when they are empty. TaskLists are useful in this case as they allow you to insert tasks in the middle of the queue. Here is an example of a conditional:
```java
 TaskList conditional_list = new TaskList();
queue.addTask(new CustomTask(()->{
        if(CurrentAlliance.alliance == CurrentAlliance.AllianceTypes.RED)
        {
                conditional_list.addTask(new LogTask("Red Alliance"));
                conditional_list.addTask(new DelayTask(1000));
                conditional_list.addTask(new ServoTask(self_destruct_trigger,TRIGGER_ACTIVE_POSITION));
        }
        else
        {
                conditional_list.addTask(new LogTask("Blue Alliance"));
                conditional_list.addTask(new DelayTask(1500));
                conditional_list.addTask(new ServoTask(self_destruct_trigger,TRIGGER_INACTIVE_POSITION));
        }
        return true;
}));
queue.addTask(conditional_list);
queue.addTask(new MotorPowerTask(1.0,launcher_motor));
```

#### Why not just use a while loop for each task?
The main problem with using while loops is that is complicates conncurrent execution. If you just put a while loop in the same thread as your main loop, you will halt the main loop while the secondary while loop is running. If you put it in a different thread then it will not be synchronized with the main thread meaning that you may be sending motor power multiple times before getting new data or geting new data multiple times without sending motor power significantly reducing the effective loop speed. 