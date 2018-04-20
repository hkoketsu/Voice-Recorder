
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import java.lang.Exception;
import java.awt.Color;
import javax.swing.JPanel;
import java.io.File;
import java.lang.Thread;

/**
  ボタンを押した時のそれぞれの処理
  録音開始、録音終了、再生
  @author Hiroki Koketsu
*/
class Controller extends JPanel
{
  private static Recorder r;
  private static Player p;
  private static DataReader dr;
  private File file;


  public Controller(File file) throws Exception
  {
    this.file = file;
    r = new Recorder(file);
  }

  // 録音開始
  public void record()
  {
    try
    {
      Thread thread = new Thread(r);
      thread.start();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  // 録音終了
  public void record_stop()
  {
    try
    {
      r.stopRecording();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  // 再生
  public void play()
  {
    try
    {
      p = new Player(file);
      p.playAudioFile();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
