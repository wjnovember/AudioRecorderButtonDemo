package studio.cooffee.audiorecorderbuttondemo.bean;

/**
 * Created by cooffee on 15/10/20.
 */
public class Recorder {
    float time;
    String filePath;

    public Recorder(float time, String filePath) {
        this.time = time;
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
