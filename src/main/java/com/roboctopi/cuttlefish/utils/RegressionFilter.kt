package com.roboctopi.cuttlefish.utils
class RegressionFilter(val window_size: Int,val order:Int = 3) {
    val time_window = RingBuffer(window_size)
    val data_window = RingBuffer(window_size)
    var pT:Double = 0.0;
    var out:Double = 0.0;

    private var equations:SquareMatrix= SquareMatrix(order);
    private var solution:SquareMatrix= SquareMatrix(order);
    private var values :Vector = Vector(order);
    public var derivatives:Vector = Vector(order);

    fun update(data:Double,time:Double = System.nanoTime().toDouble()/(1000.0*1000.0*1000.0))
    {
        val dT = time-pT;
        pT = time;


        for(i in 0 until window_size)
        {
            time_window.set(time_window.get(i)-dT,i);
        }

        data_window.add(data);
        time_window.add(0.0);

        values.runForEach({x,y,v->0.0});
        equations.runForEach({x,y,v->0.0});

        //Regression
        for(term in 0 until order)
        {
            for(i in 0 until window_size)
            {
                val t = time_window.get(i);
                val y = data_window.get(i);
                for(j in 0 until order)
                {
                    equations.addToItem(j,term,2*Math.pow(t,(j + term).toDouble())/window_size.toDouble());
                }
                values.addToItem(0,term,2*y*Math.pow(t,(term).toDouble())/window_size.toDouble());
            }
            equations.invert(solution);
            solution.mul(values,derivatives);
        }
    }
}