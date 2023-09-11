package com.roboctopi.cuttlefish.Queue

import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf
class TaskQueue
{
    var tasks = LinkedList<Task>();
    var task: Task = NullTask();
    var paused = false;
    fun update()
    {
        if(!paused)
        {
            var done = task.loop();

            if(done)
            {
                nextTask();
            }

            if(task is NullTask && !tasks.isEmpty())
            {
                nextTask();
            }
        }
    }

    fun clear()
    {
        tasks.clear();
        nextTask();
    }
    private fun nextTask()
    {
        if(!tasks.isEmpty())
        {
            task = tasks.poll();
            if(!task.onBegin())
            {
                nextTask();
            }
        }
        else
        {
            task = NullTask();
        }
    }

    fun unpause()
    {
        paused = false;
    }

    fun pause()
    {
        paused = true;
    }

    fun addTask(taskToAdd :Task)
    {
        tasks.add(taskToAdd);
        if(getIdle()&&!paused)
        {
            nextTask();
        }
    }

    fun getIdle():Boolean
    {
        return (task is NullTask);
    }
}