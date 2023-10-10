# Module Cuttlefish


## Summary

## Components
All componenets in the Cuttlefish library such as Motors, Encoders, and Servos are interfaces allowing for the use of Cuttlefish on different platforms. This means that a small amount of code needs to be written to bridge Cuttlefish with the platform SDK. We provide the CuttlefishFTCBridge library to this this which also provides additional functionality. For more info see <a href="/CuttlefishFTCBridge/index.html">CuttlefishFTCBridge</a>. If you wish to use Cuttlefish without the CuttlefishFTCBridge library you will have to write your own implementations for the <a href="/cuttlefish/com.roboctopi.cuttlefish.components/index.html">Cuttlefish componenets interfaces</a>.

***All examples provided will be utilizing the CuttlefishFTCBridge library.***



## Primary systems
There are four primary Cuttlefish systems:
- <a href="/cuttlefish/com.roboctopi.cuttlefish.controller/-mecanum-controller/index.html">Mecanum Controller</a>
- <a href="/cuttlefish/com.roboctopi.cuttlefish.localizer/-three-encoder-localizer/index.html">Three Encoder Localizer</a>
- <a href="/cuttlefish/com.roboctopi.cuttlefish.controller/-p-t-p-controller/index.html">Point to Point Controller</a>
- <a href="/cuttlefish/com.roboctopi.cuttlefish.queue/-task-queue/index.html">Task Queue</a>

All four of these systems should be initialized in your Initialized opmode

### Mecanum Controller

### Three Encoder Localizer

### Point to Point Controller

### Task Queue