# Module cuttlefish
# Getting Started

## Installation

### Basic

### Advanced
Here is how to add cuttlefish to your project directly as a repository. This is useful if you are planning to modify the library yourself

Clone Cuttlefish into the top level folder of your project. This can be done with the following command in git bash:
```
git clone https://github.com/14496roboctopi/cuttlefish
```
If you have forked the library then you can use the URL to your library instead. Note: Copy and pasting into the windows git bash is sometimes buggy, meaning that if you copy and paste this into the windows git bash it might throw an error if you don't retype certain parts of it.

Another option instead of cloning the library in directly is to add it as a submodule. This is useful if you are using git for your project as it tells git to retrive the project from a seperate repository rather than including it in your main repoisitory. It can be added as a submodule using the following command:
```
git submodule add https://github.com/14496roboctopi/cuttlefish
```
DO NOT RUN THIS COMMAND IN ADDITION TO THE FIRST COMMAND. 
If you choose to go with this option then you will need to push and pull the submodule seperately from the rest of your git. You will also need to run the commands git submodule init, and git submodule update whenever you set up a new copy of the repo on a different computer in order to pull the submodule into your project.

Next, look for your project level build.gradle file. It can be found under gradle scripts and it should say (Project: the_name_of_your_project) in parentheses after build.gradle. In this file in the dependencies block add the following line:
```gradle
classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10'
```
This tells gradle to include the kotlin plugin in your project.

Now open settings.gradle which can also be found under gradle scripts and add the following line:
```gradle
include ':cuttlefish'
```
This tells gradle that the cuttlefish folder is a module in your project.

Finally, locate the TeamCode build.gradle file. This is the build.gradle file that says (Moduke: TeamCode) in parentheses. In the dependencies block of this file add the following line:
```gradle
implementation project(path: ':cuttlefish')
```
This adds cuttlefish as a dependency of your teamcode module allowing it to be used in teamcode.

## Device System
### Overview
Throughout our FTC journey we have noticed certain shortcomings in the stock Rev device system. First, while the config file seems like a decent idea in theory, in practice it can be rather inconvenient as the config must be changed in the app and cannot be adjusted directly in code. Another problem is that there are often performance issues when using lots of sensors as each one has to poll the hub individually. In order to combat these issues, we have created our own device system that accesses features directly through LynxCommands rather than using the typical device classes.

This allows devices to be retrived directly by port number rather than by config file name, allows sensors to automatically use bulk data instead of direct polling increasing loop performance, and provides all of the custom functionality found in libraries like rev-extensions-2 conveniently integrated directly into the device objects.

### CuttleRevHub
The basis of the Cuttlefish device system it the CuttleRevHub object. It is an object that provides all extended functionality having to do with the rev hub (expansion or control) such as adjusting LED color and measuring battery voltage, and it is used to obtain other devices such as motors and servos. If you are using two hubs they will both have to be obtained seperately. The hubs can be obtained as follows:
```java
CuttleRevHub controlHub = new CuttleRevHub(hardwareMap,CuttleRevHub.HubTypes.CONTROL_HUB);
CuttleRevHub expansionHub = new CuttleRevHub(hardwareMap,CuttleRevHub.HubTypes.EXPANSION_HUB);
```
If the doesn't work, you can also obtain hubs directly by name. The name of each hub can be found in the robot config file. Here is an example of retrieving a hub by name:
```java
CuttleRevHub exHub = new CuttleRevHub(hardwareMap,"Expansion Hub 2");
```
Make sure to define the hub in init as when the constructor is called it will get the control hub using hardwareMap.
The CuttleRevHub can be used to access hub spesific features such as reading battery voltage, and to retrieve other devices such as motors and servos. A detailed description of availible features can be found in the Cuttlefish reference documentation. 

Devices can be obtains as follows:
Once a CuttleRevHub has been obtained it can be used to obtain devices. This can be done simply as follows:
```java
CuttleMotor motor = controlHub.getMotor(1 /*Motor Port Number*/ );
CuttleEncoder encoder = hub.getEncoder(3 /*Encoder Motor Port Number*/, 512 /*Counter Per Revolution*/ );
CuttleServo servo = hub.getServo(2 /*Servo Port Number*/) ;
CuttleAnalog analog_sensor = hub.getAnalog(2 /*Analog Port Number*/ );
CuttleDigital digital_sensor = hub.getDigital(3 /*Digital Port Number*/ );
```
Where the number being passed into the function is the port number of the device. **If you are using any sensors obtained from the hub, you must call the pullBulkData function of the hub every loop cycle in order for the sensors to function.** This is because the sensors automatically use cached bulk data from the hub instead of using
