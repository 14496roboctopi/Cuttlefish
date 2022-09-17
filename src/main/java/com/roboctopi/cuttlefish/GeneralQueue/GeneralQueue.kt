package com.roboctopi.cuttlefish.GeneralQueue

class GeneralQueue {
    var inactiveTasks:ArrayList<GeneralTask> = ArrayList<GeneralTask>();
    var activeTasks:ArrayList<GeneralTask> = ArrayList<GeneralTask>();
    var completeTasks:ArrayList<GeneralTask> = ArrayList<GeneralTask>();
    var readyTasks:ArrayList<GeneralTask> = ArrayList<GeneralTask>();

    fun update()
    {
        var taskBuffer:ArrayList<GeneralTask> = ArrayList<GeneralTask>();
        for(bufferEle in activeTasks)
        {
            taskBuffer.add(bufferEle);
        }

        var ids:String = "";

        System.out.println("QueueCycle")
        for(active in activeTasks)
        {
            ids+=active.id;
            ids+=",";
        }
        System.out.println(ids)

        for(active in taskBuffer)
        {
            if(active != null && active.loop())
            {
                completeTasks.add(active);
                System.out.println("TaskComplete")
                System.out.println(active.id)

                if(!active.failed)
                {
                    for(inactive in inactiveTasks)
                    {
                        inactive.dependencies.remove(active.id);
                        if(inactive.dependencies.isEmpty())
                        {
                            readyTasks.add(inactive)
                            System.out.println(inactive.id)
                        }
                    }
                }
            }
        }

        if(!completeTasks.isEmpty())
        {
            for(complete in completeTasks)
            {
                activeTasks.remove(complete);
                System.out.println("Removed")
                println(complete.id)
            }
            completeTasks.clear();
        }

        if(!readyTasks.isEmpty())
        {
            for(ready in readyTasks)
            {
                inactiveTasks.remove(ready);

                if(ready.onBegin()){activeTasks.add(ready)}

            }
            readyTasks.clear();
        }

    }
    fun addTask(task:GeneralTask)
    {
        if(task.dependencies.isEmpty())
        {
            activeTasks.add(task)
            task.onBegin();
        }
        else
        {
            inactiveTasks.add(task);
        }
    }
}