package com.roboctopi.cuttlefish.Queue

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

    fun addTask(task:Task)
    {
        queue.addTask(task);
    }
}