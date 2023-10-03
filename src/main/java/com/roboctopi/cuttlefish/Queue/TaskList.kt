package com.roboctopi.cuttlefish.Queue


/**
 * A task containing a list of tasks that will occur sequentially as if they were in a queue.
 * Can be used with a ForkTask to allow multiple sequences of events to happen concurrently.
 * A task list can contain a custom task at the end of it which adds more tasks to the list allowing for recurrent execution.
 * @see ForkTask
 * */
class TaskList(): Task
{
    var queue:TaskQueue = TaskQueue();
    init
    {
        queue.pause();
    }
    override fun onBegin(): Boolean
    {
        queue.unpause();
        return true;
    }

    override fun loop(): Boolean
    {
        queue.update();
        return queue.task is NullTask;
    }
    /**
     * Add a task to th task list
     * @param task
     * */
    fun addTask(task:Task)
    {
        queue.addTask(task);
    }
}