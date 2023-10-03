package com.roboctopi.cuttlefish.Queue

import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

/**
 * The Task Queue is a scheduling system. It is a Queue to which Tasks can be added.
 * The loop function of the task at the front of the queue will be executed every update cycle until it returns true.
 * Once the task loop function returns true the task is discarded from the queue and the next task begins executing.
 * By using ForkTasks and TaskLists it is possible to create arbitrarily complex trees of tasks.
 * Loops of tasks can be implementing via recursion using a TaskList and CustomTask.
 * @see Task
 * @see TaskList
 * @see ForkTask
 * @see CustomTask
 * */
class TaskQueue
{
    var tasks = LinkedList<Task>();
    var task: Task = NullTask();
    var paused = false;
    /**
     * Execute the loop function of the current tasks and discard it if it is complete
     * */
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

    /**
     * Remove all tasks from the queue
     * */
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

    /**
     * Resume execution of the queue
     * */
    fun unpause()
    {
        paused = false;
    }

    /**
     * Temporarily halt the execution of the queue
     * */
    fun pause()
    {
        paused = true;
    }

    /**
     * Add a task to the queue
     * @param taskToAdd
     * */
    fun addTask(taskToAdd :Task)
    {
        tasks.add(taskToAdd);
        if(getIdle()&&!paused)
        {
            nextTask();
        }
    }

    /**
     * Check if there are currently tasks in the queue
     * */
    fun getIdle():Boolean
    {
        return (task is NullTask);
    }
}