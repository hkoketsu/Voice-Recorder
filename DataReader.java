import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.lang.Exception;
import java.lang.Math;

import java.nio.ByteOrder;

/**
  WAVファイルから波形データを抽出
  URL(http://krr.blog.shinobi.jp/javafx_praxis/javaで音声波形データを表示してみる）から参照
  @author Hiroki Koketsu
*/
class DataReader extends ArrayList
{

    private File file;
    private AudioInputStream is;
    private AudioFormat format;
    private int mount;
    private double durationInSeconds;

    public DataReader()
    {
      new DataReader(null);
    }

    public DataReader(File file)
    {
      this.file = file;
      try
      {
        is = AudioSystem.getAudioInputStream(file);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      format = is.getFormat();
      long frames = is.getFrameLength();
      durationInSeconds = (frames+0.0) / format.getFrameRate();
      mount   = (int) ( format.getSampleRate() * durationInSeconds );
    }

    public int getMount(){
      return mount;
    }

    public AudioFormat getFormat() {
      return format;
    }

    /*
    * 参照: http://krr.blog.shinobi.jp/javafx_praxis/javaで音声波形データを表示してみる
    */
    public double[] readWavData() throws Exception
    {
        // 取得する標本数を計算
        // 1秒間で取得した標本数がサンプルレートであることから計算

        double[] values = new double[ mount ];

        // 音声データの取得
        for( int i = 0 ; i < mount ; i++ )
        {
            // 1標本分の値を取得
            int     size        = format.getFrameSize();
            byte[]  data        = new byte[ size ];
            int     readedSize  = is.read(data);

            // データ終了でループを抜ける
            if( readedSize == -1 ){ break; }

            // 1標本分の値を取得
            switch( format.getSampleSizeInBits() )
            {
                case 8:
                    values[i]   = Math.sin(Math.PI*data[0]);
                    break;
                case 16:
                    values[i]   = Math.sin(Math.PI* (ByteBuffer.wrap( data ).order( ByteOrder.BIG_ENDIAN ).getShort() / 32767.0));
                    break;
                default:
            }
        }
        return values;
    }
}
