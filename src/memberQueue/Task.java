package memberQueue;


/**
 * Created by mvangundy on 3/10/2016.
 */
public class Task  {
    private String taskName;
    private int taskTime;

    public Task(String taskName, int taskTime) {
        this.taskName = taskName;
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(int taskTime) {
        this.taskTime = taskTime;
    }

    @Override
    public String toString() {
        return taskName;
    }
}
