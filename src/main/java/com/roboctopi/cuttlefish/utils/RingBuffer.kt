package com.roboctopi.cuttlefish.utils
class RingBuffer (val length:Int) {
    private var arr:Array<Double> = Array<Double>(length,{i->0.0});
    private var start:Int = 0;
    fun get(index:Int):Double
    {
        return arr[(index+start)%length];
    }
    fun set(value:Double, index: Int)
    {
        arr[(index+start)%length] = value;
    }
    fun add(value:Double)
    {
        arr[start]=value;
        start++;
        start%=length;
    }
}