import javax.sound.sampled.*;
import java.lang.Runnable;
import java.io.*;
import java.lang.Exception;

/**
  外部の音を録音する
  URL(http://mocha-java.com/recording-2/）、URL(http://mocha-java.com/recording-3/)から参照
*/
class Recorder implements Runnable
{
  private static final float    SAMPLE_RATE         = 44100;
  private static final int      SAMPLE_SIZE_IN_BITS = 16;
  private static final int      CHANNELS            = 2;
  private static final boolean  SIGNED              = true;
  private static final boolean  BIG_ENDIAN          = true;

  private         TargetDataLine        line;
  private final   AudioFileFormat.Type  fileType  =   AudioFileFormat.Type.WAVE;
  private         AudioFormat           format;
  private         File                  file;
  private static  Thread                stopper;

  public Recorder(File file) throws LineUnavailableException
  {
    this.file = file;
    format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
    line = AudioSystem.getTargetDataLine(format);
  }

  public void startRecording()
  {
    AudioInputStream ais;
    try
    {
      line.open(format);
      line.start();
      ais = new AudioInputStream(line);
      AudioSystem.write(ais, fileType, file);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void stopRecording()
  {
    line.stop();
    line.close();
  }

  @Override
  public void run()
  {
    startRecording();
  }

}
