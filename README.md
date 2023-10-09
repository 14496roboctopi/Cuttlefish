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
```
classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10'
```
This tells gradle to include the kotlin plugin in your project.

Now open settings.gradle which can also be found under gradle scripts and add the following line:
```
include ':cuttlefish'
```
This tells gradle that the cuttlefish folder is a module in your project.

Finally, locate the TeamCode build.gradle file. This is the build.gradle file that says (Moduke: TeamCode) in parentheses. In the dependencies block of this file add the following line:
```
implementation project(path: ':cuttlefish')
```
This adds cuttlefish as a dependency of your teamcode module allowing it to be used in teamcode.

## Device System
### CuttleRevHub
The basis of the Cuttlefish device system it the CuttleRevHub object. It is an object that provides all extended functionality having to do with the rev hub (expansion or control) such as adjusting LED color and measuring battery voltage, and it is used to obtain other devices such as motors and servos. If you are using two hubs they will both have to be obtained seperately. The hubs can be obtained as follows:
```
CuttleRevHub ctrlHub = new CuttleRevHub(
