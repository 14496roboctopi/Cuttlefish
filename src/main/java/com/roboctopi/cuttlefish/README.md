# Cuttlefish Documentation

## `com.roboctopi.cuttlefish`

### `components`

#### `Motor.Motor`
A general interface for motors.
> :information_source: If you are using the FTC variant of Cuttlefish, please use
> `org.firstinspires.ftc.teamcode.wrappers.CuttlefishMotor`. If you are using a different variant,
> please consult it's documentation (also, since when do those exist?).

##### `setPower(power: Double)`
Set the speed and direction of the motor.
   * `power`: Should be between -1 and 1. Positive values go forward and negative values go
     backward. The value 0 goes nowhere. `abs(power)` (between 0 and 1) is the speed.

#### `NullMotor.NullMotor`
A motor that does nothing. An implementation of [`Motor.Motor`](#`Motor.Motor`).

#### `RotaryEncoder.RotaryEncoder`
A general interface for rotary encoders.
> :information_source: If you are using the FTC variant of Cuttlefish, please use
> `org.firstinspires.ftc.teamcode.wrappers.CuttlefishEncoder`. If you are using a different variant,
> please consult it's documentation (also, I'm still pretty sure those don't exist...).

##### `getRotation(): Double`
Get the current rotation of the encoder.

Returns the current rotation as a Double.

#### `Servo.Servo`
A general interface for servos.
> :information_source: If you are using the FTC variant of Cuttlefish, please use
> `org.firstinspires.ftc.teamcode.wrappers.CuttlefishServo`. If you are using a different variant,
> please consult it's documentation (also, please tell me if there really *is* any such thing).

##### `setRotation(position: Double)`
Set the rotation of the servo.
   * `position`: Should be between 0 and 1. The position to set the servo to.

### `controller`

#### `MecanumController.MecanumController`
A class that controls a 4-wheel mecanum drive system.

##### `constructor(rightFront: Motor, rightBack: Motor, leftFront: Motor, leftBack: Motor, rotePID: PID, roteAntiStallThreshold: Double)`
   * `rightFront` (optional but recommended): The front-right mecanum wheel. Defaults to
     `NullMotor()`.
   * `rightBack` (optional but recommended): The back-right mecanum wheel. Defaults to
     `NullMotor()`.
   * `leftFront` (optional but recommended): The front-left mecanum wheel. Defaults to
     `NullMotor()`.
   * `leftBack` (optional but recommended): The back-left mecanum wheel. Defaults to
     `NullMotor()`.
   * `rotePID` (optional): The PID system to use. Defaults to `PID(PI * 0.5, 0.15, 2.0)`.
     Probably shouldn't touch.
   * `roteAntiStallThreshold` (optional): I don't even know. Ask Logan or something. Defaults to
     `0.11`. Probably shouldn't touch this either.

##### `setVec(direction: Pose, power: Double = 1.0, holdRote: Boolean = false, maxRotationPriority: Double = 1.0, rotation: Double = 0.0)`
Sets a vector for the robot to travel.
   * `direction`: The (x, y) position and rotation to travel to.
   * `power` (optional): Should be between 0 and 1. How much power to give to the motors. Defaults
     to `1.0`.
   * `holdRote` (optional): Whether to attempt to keep the current rotation or not. Defaults to
     `false`.
   * `maxRotationPriority` (optional): Logan needs to tell me... :warning: DON'T TOUCH!!! Defaults
     to `1.0`.
   * `rotation` (optional): :confused: Isn't this already specified in `direction`? Defaults to
     `0.0`.

#### `MotorController.MotorController`
A class that controls a motor.

##### `constructor(motor: Motor, encoder: RotaryEncoder, vPID: PID)`
   * `motor`: The motor to control.
   * `encoder`: The encoder on the motor.
   * `vPID`: The PID system to use.

##### `updateVelocity(target: Double, speedCap: Double)`
   * `target`: The target velocity
   * `speedCap`: :construction: Not Implemented
