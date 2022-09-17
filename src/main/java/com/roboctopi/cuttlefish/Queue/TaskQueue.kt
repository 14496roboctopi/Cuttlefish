package com.roboctopi.cuttlefish.Queue

import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf
//TODO: Add persistent tasks
class TaskQueue
{
    var tasks = LinkedList<Task>();
    var task: Task = NullTask();
    var paused = false;
    private var persistantTasks = ArrayList<Task>();

    private var completePersistants = ArrayList<Task>();

    fun update()
    {
        if(!paused)
        {
            var done = task.loop();
            for(pTask in persistantTasks)
            {
                var pDone = pTask.loop();
                if(pDone)
                {
                    completePersistants.add(pTask);
                }
            }
            for(rTask in completePersistants)
            {
                persistantTasks.remove(rTask);
            }
            completePersistants.clear();

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
        if(taskToAdd.persistant)
        {
            persistantTasks.add(taskToAdd);
            taskToAdd.onBegin();
        }
        else
        {
            tasks.add(taskToAdd);
        }
        if(getIdle()&&!paused)
        {
            nextTask();
        }
    }

    fun getIdle():Boolean
    {
        return (task is NullTask)&&persistantTasks.isEmpty();
    }
}