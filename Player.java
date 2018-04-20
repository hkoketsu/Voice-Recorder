import java.io.File;
import java.lang.Exception;;
import javax.sound.sampled.*;

/**
  録音したWAVファイルの再生
  URL(http://mocha-java.com/play-wav/)から参照
  @author Hiroki Koketsu
*/
class Player
{
    private static AudioInputStream ais;
    private static AudioFormat format;
    private static DataLine.Info info;
    private static SourceDataLine line;

    public Player(File file) throws Exception
    {
        ais = AudioSystem.getAudioInputStream(file);
        format = ais.getFormat();
        info = new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);
        line = (SourceDataLine) AudioSystem.getLine(info);
    }

    public static void playAudioFile() throws Exception
    {
        line.open(format, AudioSystem.NOT_SPECIFIED);
        line.start();

        int buffer_size = 128*1000;
        int bytes_read = 0;
        byte[] ab_data = new byte[buffer_size];
        while (bytes_read != -1)
        {
            bytes_read = ais.read(ab_data,0,ab_data.length);
            if(bytes_read >= 0)
            {
                line.write(ab_data, 0, bytes_read);
            }
        }
        line.drain();
        line.close();
        ais.close();
    }

}
