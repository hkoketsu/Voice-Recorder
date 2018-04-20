import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.embed.swing.SwingNode;
import javax.swing.SwingUtilities;
import java.io.File;
import java.lang.Exception;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.Exception;
import java.awt.BorderLayout;

/**
  メインファイル、GUIの設定
  @author Hiroki Koketsu
*/
public class VoiceRecorder extends Application
{

    private final   String          filepath = "audio/RecordedAudio.wav";
    private         DataReader      reader;
    private         HBox            root;

    public static void main(String[] args) throws Exception
    {
      launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // フォント色がおかしくなることへの対処
        System.setProperty( "prism.lcdtext" , "false" );

        // シーングラフの作成
        root    = new HBox();

        // // チャートを作成
        // root.getChildren().add( createLineChart() );            // 折れ線グラフの追加


        // 録音ボタンと再生ボタン、メッセージパネルの設置
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        root.getChildren().add(swingNode);


        // シーンの作成
        Scene       scene   = new Scene( root , 900 , 500 );

        // ウィンドウ表示
        primaryStage.setScene( scene );
        primaryStage.show();

    }

    @SuppressWarnings("unchecked")
    public Node createLineChart(double[] values)
    {
        // 折れ線グラフ
        NumberAxis                  xAxis   = new NumberAxis();
        NumberAxis                  yAxis   = new NumberAxis();
        LineChart<Number, Number>   chart   = new LineChart<Number, Number>( xAxis , yAxis );
        chart.setMinWidth( 900 );

        // データを作成
        Series< Number , Number > series1    = new Series<Number, Number>();
        for( int i=0 ; i<values.length ; i++ )
        {
            series1.getData().add( new XYChart.Data<Number, Number>( i , values[i] ) );
        }

        // データを登録
        chart.getData().addAll( series1 );

        // タイトルを設定
        String  title   = String.format( "音声波形データ（サンプリング周波数：%fHz )", reader.getFormat().getSampleRate() );
        chart.setTitle( title );

        // 見た目を調整
        chart.setCreateSymbols(false);                                                      // シンボルを消去
        series1.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;");  // 線を細く

        return chart;
    }

    private void createAndSetSwingContent(final SwingNode swingNode) throws Exception {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
               try
               {
                 VRComponent comp = new VRComponent(filepath);
                 swingNode.setContent(comp);
               }
               catch (Exception e)
               {
                 e.printStackTrace();
               }
             }
         });
   }

   private class VRComponent extends JComponent{

     private         TextPanel       tp;
     private         ButtonPanel     bp;
     private         Controller      controller;

     private         boolean         isRecording;
     private         boolean         isPlaying;

     private         File            file;


     public VRComponent(String filepath) throws Exception {
       file       = new File(filepath);
       controller = new Controller(file);
       tp         = new TextPanel();
       bp         = new ButtonPanel();

       setLayout(new BorderLayout());
       add(tp, BorderLayout.CENTER);
       add(bp, BorderLayout.SOUTH);
     }

     class TextPanel extends JPanel
     {
       private JLabel rec_msg;

       // メッセージ表示用
       public TextPanel()
       {
         rec_msg = new JLabel("Hello");
         add(rec_msg);
       }

       public void setTextInThePanel(String s) {
         rec_msg.setText(s);
       }
     }

     class ButtonPanel extends JPanel implements ActionListener
     {
       private JButton         rec_btn;
       private JButton         play_btn;
       private JButton         stop_btn;
       private String          rec         = "rec";
       private String          play        = "play";
       private VoiceRecorder   app;

       // 録音ボタンと再生ボタン
       public ButtonPanel()
       {
         rec_btn = new JButton("録音開始");
         rec_btn.addActionListener(this);
         rec_btn.setActionCommand(rec);
         add(rec_btn);

         play_btn = new JButton("再生");
         play_btn.addActionListener(this);
         play_btn.setActionCommand(play);
         add(play_btn);
       }

       @Override
       public void actionPerformed(ActionEvent e)
       {
         if (e.getActionCommand() == rec)
         {
           // 録音開始ボタンを押した場合
           if (!isRecording)
           {
             tp.setTextInThePanel("Recording...");
             rec_btn.setText("録音終了");
             isRecording = true;
             controller.record();
           }
           // 録音終了ボタンを押した場合
           else
           {
             rec_btn.setText("録音開始");
             isRecording = false;
             tp.setTextInThePanel("");
             controller.record_stop();
           }
         }

         if (e.getActionCommand() == play)
         {
           tp.setTextInThePanel("Playing...");

           if (!isPlaying) {
             isPlaying = true;
             controller.play();
             DataReader reader = new DataReader(file);
             try
             {
               Node n = createLineChart(reader.readWavData());
               root.getChildren().add(n);
             }
             catch (Exception ex)
             {
               ex.printStackTrace();
             }
           }
           isPlaying = false;
         }
       }
     }
   }



}
