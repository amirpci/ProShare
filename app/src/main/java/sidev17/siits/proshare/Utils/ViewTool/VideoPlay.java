package sidev17.siits.proshare.Utils.ViewTool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.VideoView;

import sidev17.siits.proshare.R;

public class VideoPlay /*extends Activity */{
/*
    private String videoPath ="url";

    private static ProgressDialog progressDialog;
    String videourl;
    VideoView videoView ;


    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video);

        videoView = (VideoView) findViewById(R.id.videoView);


        progressDialog = ProgressDialog.show(PlayVideo.this, "", "Buffering video...", true);
        progressDialog.setCancelable(true);


        PlayVideo();

    }
    private void PlayVideo()
    {
        try
        {
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            MediaController mediaController = new MediaController(PlayVideo.this);
            mediaController.setAnchorView(videoView);

            Uri video = Uri.parse(videoPath );
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new OnPreparedListener()
            {

                public void onPrepared(MediaPlayer mp)
                {
                    progressDialog.dismiss();
                    videoView.start();
                }
            });


        }
        catch(Exception e)
        {
            progressDialog.dismiss();
            System.out.println("Video Play Error :"+e.toString());
            finish();
        }

    }
*/
}