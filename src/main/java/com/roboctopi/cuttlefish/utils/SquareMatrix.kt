package com.roboctopi.cuttlefish.utils
import kotlin.math.abs

class SquareMatrix(val size:Int):Matrix(size,size,
    fun(i):Double{
        if(i%size == (i/size)) {
            return 1.0
        }
        return 0.0
    }) {
    fun identity()
    {
        runForEach(fun(x,y,v):Double{
            if(x==y) {
                return 1.0;
            }
            return 0.0;
        });
    }

    private lateinit var invert_buffer:SquareMatrix;
    fun invert(out:SquareMatrix):Boolean
    {
        if(!::invert_buffer.isInitialized)
        {
            invert_buffer = SquareMatrix(size);
        }

        if(this.size != out.size)
        {
            throw IllegalArgumentException("Matrix Output Size Mismatch");
        }
        out.identity();
        copy(invert_buffer);
        for(x in 0 until this.width)
        {
            var bestVal = 0.0;
            var bestIndex = 0;
            for(y in x until this.height)
            {
                val value = (invert_buffer.getItem(x,y));
                if(abs(value) > abs(bestVal))
                {
                    bestVal = value;
                    bestIndex = y;
                }
            }
            if(bestVal == 0.0)
            {
                return false
            }
            var currentVal = invert_buffer.getItem(x,x);
            invert_buffer.addScaleRow(bestIndex,x,(1-currentVal)/bestVal);
            out.addScaleRow(bestIndex,x,(1-currentVal)/bestVal);

            for (y in x + 1 until this.height) {
                val belowVal = invert_buffer.getItem(x, y);
                invert_buffer.addScaleRow(x, y, -belowVal);
                out.addScaleRow(x, y, -belowVal);
            }
        }

        for (x in 0 until this.width) {
            for(y in 0 until x) {
                val aboveVal = invert_buffer.getItem(x,y);
                invert_buffer.addScaleRow(x,y,-aboveVal);
                out.addScaleRow(x,y,-aboveVal);
            }
        }
        return true;
    }
}