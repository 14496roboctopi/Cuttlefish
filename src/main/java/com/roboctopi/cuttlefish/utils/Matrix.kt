package com.roboctopi.cuttlefish.utils
open class Matrix(var width: Int, var height: Int, init: (Int) -> Double = { i -> 0.0})
{
    var mat = Array(width * height, init);

    fun mul(input: Matrix,out: Matrix)
    {
        if(this.width != input.height)
        {
            throw IllegalArgumentException("Matrix Size Incorrect");
        }
        if(this.height*input.width != out.width*out.height)
        {
            throw IllegalArgumentException("Output Size Mismatch");
        }
//        val buffer = Matrix(input.width, this.height);
        for(y in 0 until this.height)
        {
            for(x in 0 until input.width)
            {
                var item = 0.0;
                for(i in 0 until input.height)
                {
                    item += this.getItem(i,y) * input.getItem(x,i);
                }
                out.setItem(x, y, item);
            }
        }
    }
    fun addToItem(x:Int,y:Int,value: Double)
    {
        setItem(x,y,getItem(x,y)+ value);
    }

    fun add(input: Matrix)
    {
        if(this.width != input.width || this.height != input.height)
        {
            throw IllegalArgumentException("Matrix Size Incorrect");
        }
        for(y in 0 until this.height)
        {
            for(x in 0 until this.width)
            {
                this.setItem(x, y, this.getItem(x, y) + input.getItem(x, y));
            }
        }
    }

    fun subtract(input: Matrix)
    {
        if(this.width != input.width || this.height != input.height)
        {
            throw IllegalArgumentException("Matrix Size Incorrect");
        }
        for(y in 0 until this.height)
        {
            for(x in 0 until this.width)
            {
                this.setItem(x, y, this.getItem(x, y) - input.getItem(x, y));
            }
        }
    }

    fun getItem(x: Int,y: Int): Double
    {
        return mat[x + y * this.width];
    }

    fun setItem(x: Int,y: Int,value: Double)
    {
        mat[x + y * this.width] = value;
    }
    fun set(value:Array<Double>)
    {
        this.mat = value;
    }
    fun swapRows(row1:Int,row2:Int)
    {
        for (x in 0 until this.width)
        {
            var buffer = getItem(x,row2);
            setItem(x,row2,getItem(x,row1));
            setItem(x,row1,buffer);
        }
    }
    fun scaleRow(row:Int,scale:Double)
    {
        for (x in 0 until this.width)
        {
            setItem(x,row,
                getItem(x,row)*scale
            );
        }
    }
    fun addScaleRow(src:Int,dst:Int, scale:Double)
    {
        for (x in 0 until this.width)
        {
            setItem(x,dst,
            getItem(x,dst) + getItem(x,src)*scale
            );
        }
    }


    fun println()
    {
        var out = "";
        for(y in 0 until this.height)
        {
            out += "[";
            for(x in 0 until this.width)
            {
                out += this.getItem(x,y).toString()+", ";
            }
            out = out.removeSuffix(", ");
            out += "]\n";
        }
        println(out);
    }
    fun runForEach(f:(Int,Int,Double)->Double)
    {
        for(y in 0 until this.height)
        {
            for(x in 0 until this.width)
            {
                this.setItem(x,y,f(x,y,this.getItem(x,y)));
            }
        }
    }
    fun clone():Matrix
    {
        return Matrix(this.width, this.height) { i -> this.mat[i] };
    }
    fun copy(out:Matrix)
    {
        if(this.width*this.height != out.width*out.height)
        {
            throw IllegalArgumentException("Matrix Copy Output Size Mismatch");
        }
        out.runForEach { x, y, v -> this.getItem(x,y)};
    }

    fun scale(scalar: Double)
    {
        this.runForEach { x, y, v -> v * scalar };
    }
}