package studio.cooffee.audiorecorderbuttondemo.managers;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by cooffee on 15/10/19.
 */
public class AudioManager {

    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager mInstance;

    private boolean isPrepared = false;

    private AudioManager(String dir) {
        mDir = dir;
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }

    /**
     * 回调准备完毕
     */
    public interface AudioStateListener {
        void wellPrepared();
    }

    public AudioStateListener mListener;

    public void setOnAudioStateListner(AudioStateListener listner) {
        mListener = listner;
    }

    public static AudioManager getInstance(String dir) {
        if (null == mInstance) {
            synchronized (AudioManager.class) {
                if (null == mInstance) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    public void prepareAudio() {
        Log.d("LONG", "preparedAudio");
        try {
            isPrepared = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = generateFileName();
            File file = new File(dir, fileName);

            Log.d("LONG", "the file name is " + fileName);

            mCurrentFilePath = file.getAbsolutePath();
            mMediaRecorder = new MediaRecorder();
            // 设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath()); Log.d("LONG", "1");
            // 设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); Log.d("LONG", "2");
            // 设置音频格式 AMR_NB
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB); Log.d("LONG", "3");
            // 设置音频的编码为AMR
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); Log.d("LONG", "4");
            mMediaRecorder.prepare(); Log.d("LONG", "5");
            mMediaRecorder.start(); Log.d("LONG", "6");
            // 准备结束
            isPrepared = true; Log.d("LONG", "7");

            if (mListener != null) {
                Log.d("LONG", "AudioStateListener is not null");
                mListener.wellPrepared();
            } else {
                Log.d("LONG", "lisetner null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 随机生成文件的名称
     *
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepared) {
            // mMediaRecorder.getMaxAmplitude() 1-32767
            try {
                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
                // 忽略产生的异常
            }
        }
        return 1;
    }

    public void release() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    /**
     * 释放资源 同时删除音频文件
     */
    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }
}
