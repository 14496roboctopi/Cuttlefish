package com.roboctopi.cuttlefish.utils


fun rotationDiff(r0: Double, r1: Double): Double {
    return rFullToHalf(rHalfToFull(r1) - rHalfToFull(r0))
}

fun mod(a: Double, b: Double): Double {
    return if (a >= 0) {
        a % b
    } else {
        b + a % b
    }
}

fun rHalfToFull(angle: Double): Double {
    return mod(angle, 2 * Math.PI)
}

fun rFullToHalf(angle: Double): Double {
    var modAngle = mod(angle, 2 * Math.PI);
    return if (modAngle >= Math.PI) {
        modAngle - 2 * Math.PI
    } else {
        modAngle
    }
}