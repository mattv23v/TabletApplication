package memberQueue;

import java.io.Serializable;

public class Member implements Serializable{
    private  String name;
    private int waitTime;
    private String task;
    private int taskTime;

    public Member(String name, int waitTime, String task, int taskTime){
        this.name=name;
        this.waitTime=waitTime;
        this.task=task;
        this.taskTime=taskTime;
    }


    @Override
    public String toString() {
        return name +"     "+"Wait time: "+ waitTime +"      "+ task + "      task time: " + taskTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(int taskTime) {
        this.taskTime = taskTime;
    }
}
