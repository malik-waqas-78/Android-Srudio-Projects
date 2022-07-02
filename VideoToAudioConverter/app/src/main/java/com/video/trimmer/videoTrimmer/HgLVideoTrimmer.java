/*
 * MIT License
 *
 * Copyright (c) 2016 Knowledge, education for life.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.video.trimmer.videoTrimmer;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
//import com.arthenica.mobileffmpeg.Config;
//import com.arthenica.mobileffmpeg.ExecuteCallback;
//import com.arthenica.mobileffmpeg.FFmpeg;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.video.trimmer.R;
import com.video.trimmer.activities.TrimmerActivity;
import com.video.trimmer.videoTrimmer.interfaces.OnHgLVideoListener;
import com.video.trimmer.videoTrimmer.interfaces.OnProgressVideoListener;
import com.video.trimmer.videoTrimmer.interfaces.OnRangeSeekBarListener;
import com.video.trimmer.videoTrimmer.interfaces.OnTrimVideoListener;
import com.video.trimmer.videoTrimmer.utils.BackgroundExecutor;
import com.video.trimmer.videoTrimmer.utils.UiThreadExecutor;
import com.video.trimmer.videoTrimmer.view.ProgressBarView;
import com.video.trimmer.videoTrimmer.view.RangeSeekBarView;
import com.video.trimmer.videoTrimmer.view.Thumb;

import com.video.timeline.VideoTimeLine;
import com.video.timeline.render.TimelineGlSurfaceView;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
//
//import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
//import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static com.video.trimmer.videoTrimmer.utils.TrimVideoUtils.stringForTime;


public class HgLVideoTrimmer extends FrameLayout {
    private String filePath;
    private static final String TAG = "HgLVideoTrimmer.class.getSimpleName()";
    private static final int MIN_TIME_FRAME = 1000;
    private static final int SHOW_PROGRESS = 2;
    float f=0.0f;
    Float oneSecValue;
    private SeekBar mHolderTopView;
    private RangeSeekBarView mRangeSeekBarView;
    private RelativeLayout mLinearVideo;
    private View mTimeInfoContainer;
    private ImageButton btnplusStart,btnminusStart,btnplusEnd,btnminusEnd;
    private VideoView mVideoView;
    private ImageView mPlayView;
    private TextView mTextSize;
    private TextView mTextTimeFrame;
    private TextView mTextTimeFrameEnd;
    private TextView mTextTime;
    private ImageView mForwardView;
    private ImageView mReverseView;

    AlertDialog dialog2;
    AlertDialog.Builder alertDialog;
    View dialogView;
    ViewGroup viewGroup;
    ProgressBar pb;
    Button btnok;
    TextView title;
    TextView msg;

    private ProgressBarView mVideoProgressIndicator;
    private Uri mSrc;
    private String mFinalPath;
    int pos=0;
    private int mMaxDuration;
    private List<OnProgressVideoListener> mListeners;

    private OnTrimVideoListener mOnTrimVideoListener;
    private OnHgLVideoListener mOnHgLVideoListener;

    private int mDuration = 0;
    private int mTimeVideo = 0;
    private int mStartPosition = 0;
    private int mEndPosition = 0;
    TimelineGlSurfaceView timelineGlSurfaceView;
    private long mOriginSizeFile;
    private boolean mResetSeekBar = true;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HgLVideoTrimmer(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HgLVideoTrimmer(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_time_line, this, true);


        mHolderTopView = ((SeekBar) findViewById(R.id.handlerTop));
        mVideoProgressIndicator = ((ProgressBarView) findViewById(R.id.timeVideoView));
        mRangeSeekBarView = ((RangeSeekBarView) findViewById(R.id.timeLineBar));
        mLinearVideo = ((RelativeLayout) findViewById(R.id.layout_surface_view));
//        mVideoView = ((VideoView) findViewById(R.id.video_loader));
        mPlayView = ((ImageView) findViewById(R.id.icon_video_play));
        mTimeInfoContainer = findViewById(R.id.timeText);
        mTextSize = ((TextView) findViewById(R.id.textSize));

        mTextTimeFrame = ((TextView) findViewById(R.id.textTimeSelection));
        mTextTimeFrameEnd=((TextView) findViewById(R.id.textTimeSelectionEnd));
        mTextTime = ((TextView) findViewById(R.id.textTime));
        timelineGlSurfaceView=((TimelineGlSurfaceView)findViewById(R.id.fixed_thumb_list));
        mForwardView=(ImageView)findViewById(R.id.icon_forward_video);
        mReverseView=(ImageView)findViewById(R.id.icon_reverse_video);

        btnplusStart=(ImageButton)findViewById(R.id.btnPlusStart);
        btnminusStart=(ImageButton)findViewById(R.id.btnMinusStart);
        btnplusEnd=(ImageButton)findViewById(R.id.btnPlusEnd);
        btnminusEnd=(ImageButton)findViewById(R.id.btnMinusEnd);

        setUpListeners();
        setUpMargins();
    }

    private void setUpListeners() {
        mListeners = new ArrayList<>();
        mListeners.add(new OnProgressVideoListener() {
            @Override
            public void updateProgress(int time, int max, float scale) {
                updateVideoProgress(time);
            }
        });
        mListeners.add(mVideoProgressIndicator);

        if(TrimmerActivity.type==1){
            findViewById(R.id.btntitleTrim).setVisibility(VISIBLE);
        }else if(TrimmerActivity.type==2){
            findViewById(R.id.btntitleconvert).setVisibility(VISIBLE);
        }else if(TrimmerActivity.type==3){
            findViewById(R.id.btntitleRemove).setVisibility(VISIBLE);
        }



        findViewById(R.id.btSave)
                .setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BackgroundExecutor.execute(
                                        new BackgroundExecutor.Task("", 0L, "") {
                                            @Override
                                            public void execute() {

                                            }
                                        }
                                );



                            }
                        }
                );

        final GestureDetector gestureDetector = new
                GestureDetector(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        onClickVideoPlayPause();
                        return true;
                    }
                }
        );
        mPlayView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideoPlayPause();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                if (mOnTrimVideoListener != null)
                    mOnTrimVideoListener.onError("Something went wrong reason : " + what);
                return false;
            }
        });
        mForwardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mVideoView.getCurrentPosition()+2000<mEndPosition){
                   mVideoView.seekTo(mVideoView.getCurrentPosition()+2000);
                   mHolderTopView.setProgress(mVideoView.getCurrentPosition());
                   mVideoView.pause();
                   mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
               }
            }
        });
        mReverseView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoView.getCurrentPosition()-2000>mStartPosition){
                    mVideoView.seekTo(mVideoView.getCurrentPosition()-2000);
                    mHolderTopView.setProgress(mVideoView.getCurrentPosition());
                    mVideoView.pause();
                    mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
                }
            }
        });

        btnplusStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(stringForTime(mStartPosition).equals(stringForTime(mEndPosition)))) {
                    mStartPosition+=1000;
                    setTimeFrames();
                    updateVideoProgress(mStartPosition);
                    mVideoView.seekTo(mStartPosition);
                    setProgressBarPosition(mVideoView.getCurrentPosition());
                    int pos=mHolderTopView.getProgress();
                    float newpos=pos/10f;
                    if(mStartPosition==1000){
                        setOneSecValue(newpos);
                    }
                    mRangeSeekBarView.setThumbValue(0, newpos);
                }
            }
        });

        btnminusStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(stringForTime(mStartPosition).equals(stringForTime(0)))) {
                    mStartPosition-=1000;
                    setTimeFrames();
                    updateVideoProgress(mStartPosition);
                    mVideoView.seekTo(mStartPosition);
                    setProgressBarPosition(mVideoView.getCurrentPosition());
                    int pos=mHolderTopView.getProgress();
                    float newpos=pos/10f;
                    mRangeSeekBarView.setThumbValue(0, newpos);

                }

            }
        });
        btnplusEnd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(stringForTime(mEndPosition).equals(stringForTime(mDuration))))  {
                    mEndPosition+=1000;
                    setTimeFrames();
                    f = (float) (mRangeSeekBarView.getThumbValue(1) + getOneSecValue());
                    mRangeSeekBarView.setThumbValue(1, f);
                }

            }
        });
        btnminusEnd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(stringForTime(mEndPosition).equals(stringForTime(mStartPosition)))) {
                    mEndPosition-=1000;
                    setTimeFrames();
                    f = (float) (mRangeSeekBarView.getThumbValue(1) - getOneSecValue());
                    mRangeSeekBarView.setThumbValue(1, f);
                }

            }
        });
        mVideoView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, @NonNull MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        mRangeSeekBarView.addOnRangeSeekBarListener(mVideoProgressIndicator);
        mRangeSeekBarView.addOnRangeSeekBarListener(new OnRangeSeekBarListener() {
                                                        @Override
                                                        public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {

                                                        }

                                                        @Override
                                                        public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
                                                            onSeekThumbs(index, value);
                                                            long diff=mEndPosition-mStartPosition;
                                                            mTextTime.setText(stringForTime((int) diff));
                                                        }

                                                        @Override
                                                        public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {

                                                        }

                                                        @Override
                                                        public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {
//                                                            onStopSeekThumbs();
                                                        }
                                                    });

        mHolderTopView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //onPlayerIndicatorSeekChanged(progress, fromUser);
                if(fromUser){
                    if(progress>mStartPosition&&progress<mEndPosition) {
                        mVideoView.seekTo(progress);
                        mTextSize.setText(timeConversion(progress));

                        int width = mHolderTopView.getWidth()
                                - mHolderTopView.getPaddingLeft()
                                - mHolderTopView.getPaddingRight();
                        float thumbPos = width * (mHolderTopView.getProgress() / (float) mHolderTopView.getMax());
                        mTextSize.setTranslationX(thumbPos);
                    }else{
                        mVideoView.seekTo(mStartPosition);
                        mTextSize.setText(timeConversion(mStartPosition));

                        int width = mHolderTopView.getWidth()
                                - mHolderTopView.getPaddingLeft()
                                - mHolderTopView.getPaddingRight();
                        float thumbPos = width * (mHolderTopView.getProgress() / (float) mHolderTopView.getMax());
                        mTextSize.setTranslationX(thumbPos);
                    }
                }else{
                    if(progress==mEndPosition){
                        mVideoView.pause();
                        mTextSize.setText(timeConversion(progress));
                        mHolderTopView.setProgress(mStartPosition);
                        mVideoView.seekTo(mStartPosition);

                    }else {
                       mTextSize.setText(timeConversion(progress));

                        int width = mHolderTopView.getWidth()
                                - mHolderTopView.getPaddingLeft()
                                - mHolderTopView.getPaddingRight();
                        float thumbPos = width * (mHolderTopView.getProgress() / (float) mHolderTopView.getMax());
                        mTextSize.setTranslationX(thumbPos);
                    }

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                onVideoPrepared(mp);
                mHolderTopView.setMax(mp.getDuration());
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onVideoCompleted();
            }
        });
    }

    public Float getOneSecValue() {
        if(mStartPosition==0){
            mStartPosition+=1000;
            setTimeFrames();
            updateVideoProgress(mStartPosition);
            mVideoView.seekTo(mStartPosition);
            setProgressBarPosition(mVideoView.getCurrentPosition());
            int pos=mHolderTopView.getProgress();
            oneSecValue=pos/10f;
            mStartPosition-=1000;
            setTimeFrames();
            updateVideoProgress(mStartPosition);
            mVideoView.seekTo(mStartPosition);
            setProgressBarPosition(mVideoView.getCurrentPosition());
        }
        return oneSecValue;
    }

    public void setOneSecValue(Float oneSecValue) {
        this.oneSecValue = oneSecValue;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setUpMargins() {
        int marge = mRangeSeekBarView.getThumbs().get(0).getWidthBitmap();
        int widthSeek = mHolderTopView.getThumb().getMinimumWidth() / 2;

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mHolderTopView.getLayoutParams();
        lp.setMargins(marge - widthSeek, 0, marge - widthSeek, 0);
        mHolderTopView.setLayoutParams(lp);



        lp = (ConstraintLayout.LayoutParams) mVideoProgressIndicator.getLayoutParams();
        lp.setMargins(marge, 0, marge, 0);
        mVideoProgressIndicator.setLayoutParams(lp);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    private void onSaveClicked() {

        if (mStartPosition <= 0 && mEndPosition >= mDuration) {
            if (mOnTrimVideoListener != null)
                mOnTrimVideoListener.getResult(mSrc,2);
        } else {
            mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
            mVideoView.pause();
            BackgroundExecutor.execute(
                    new BackgroundExecutor.Task("", 0L, "") {
                        @Override
                        public void execute() {
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

//                            }else {
//                                try {
////                                    executeCutVideoCommand(mStartPosition, mEndPosition, 2);
//                                } catch (final Throwable e) {
//                                    mOnTrimVideoListener.cancelAction();
//                                }
//                            }
                        }
                    }
            );
        }
    }


    private void showLoadingProgress(boolean b,int check) {
                        if(b){
                            mOnTrimVideoListener.onTrimStarted(check);
                        }else{

                            mOnTrimVideoListener.getResult(mSrc,check);
                        }
                    }



    private void onClickVideoPlayPause() {
        if (mVideoView.isPlaying()) {
            mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
            mVideoView.pause();
        } else {
            mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_on_video_icon));
            mVideoView.start();
        }
    }
    Handler handler3;
    Runnable runnable3;

    private final void startserviceforVideo() {
        handler3 = new Handler();
        runnable3 = new Runnable() {
            @Override
            public void run() {
                mHolderTopView.setProgress(mVideoView.getCurrentPosition());
                mTextSize.setText(timeConversion(mVideoView.getCurrentPosition()));
                int width = mHolderTopView.getWidth()
                        - mHolderTopView.getPaddingLeft()
                        - mHolderTopView.getPaddingRight();
                float thumbPos = width * (mHolderTopView.getProgress() / (float) mHolderTopView.getMax());
                mTextSize.setTranslationX(thumbPos);
                handler3.postDelayed(runnable3, 1);
            }
        };
        handler3.postDelayed(runnable3, 0);

    }
    private void onPlayerIndicatorSeekChanged(int progress, boolean fromUser) {

        int duration = (int) ((mDuration * progress) / 1000L);
        mTextSize.setText(timeConversion(progress));
        if (fromUser) {
            mTextSize.setText(timeConversion(progress));
            if (duration < mStartPosition) {
                setProgressBarPosition(mStartPosition);
                duration = mStartPosition;
            } else if (duration > mEndPosition) {
                setProgressBarPosition(mEndPosition);

                duration = mEndPosition;
            }
            setTimeVideo(duration);
        }else{
            if(progress==mEndPosition){
                setProgressBarPosition(mStartPosition);
                onClickVideoPlayPause();
            }
            mTextSize.setText(timeConversion(progress));
        }
    }
    public String timeConversion(long value) {
        String videoTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }
    private void onPlayerIndicatorSeekStart() {

        mVideoView.pause();
        mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
        notifyProgressUpdate(false);
    }

    private void onPlayerIndicatorSeekStop(@NonNull SeekBar seekBar) {

        mVideoView.pause();
        mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));

        int duration = (int) ((mDuration * seekBar.getProgress()) / 1000L);
        mVideoView.seekTo(duration);
        setTimeVideo(duration);
        notifyProgressUpdate(false);
    }

//    private FFmpeg ffmpeg;
//    @SuppressLint("LongLogTag")
//    private void loadFFMpegBinary() {
//        try {
//            if (ffmpeg == null) {
//                Log.d(TAG, "ffmpeg : era nulo");
//                ffmpeg = FFmpeg.getInstance(getContext());
//            }
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onFailure() {
//                    showUnsupportedExceptionDialog();
//                }
//
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onSuccess() {
//                    Log.d(TAG, "ffmpeg : correct Loaded");
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
//        } catch (Exception e) {
//            Log.d(TAG, "EXception no controlada : " + e);
//        }
//    }
//    private void showUnsupportedExceptionDialog() {
//        new AlertDialog.Builder(getContext())
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Not Supported")
//                .setMessage("Device Not Supported")
//                .setCancelable(false)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .create()
//                .show();
//
//    }
//    @SuppressLint("LongLogTag")
//    private void executeCutVideoCommand(int startMs, int endMs,int type) {
//        File moviesDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES
//        );
//
//        String filePrefix = "cut_video";
//        String fileExtn = ".mp4";
//        String yourRealPath = getPath(getContext(), mSrc);
//        File dest = new File(TRIMMED_VIDEO_FOLDER_PATH+"VIDpandra"+System.currentTimeMillis()+".mp4");
//        int fileNo = 0;
//        while (dest.exists()) {
//            fileNo++;
//            dest = new File(TRIMMED_VIDEO_FOLDER_PATH+"VIDpandra"+System.currentTimeMillis()+".mp4");
//        }
//
//        Log.d(TAG, "startTrim: src: " + mSrc);
//        Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
//        Log.d(TAG, "startTrim: startMs: " + startMs);
//        Log.d(TAG, "startTrim: endMs: " + endMs);
//        filePath = dest.getAbsolutePath();
//        String[] complexCommand = {"-i", String.valueOf(mSrc), "-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000,"-preset","ultrafast", filePath};
//
//
//
//        execFFmpegBinary(complexCommand,type);
//
//    }
//    @SuppressLint("LongLogTag")
//    private void executeConvertVideoCommand(int startMs, int endMs,int type) {
//        File moviesDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES
//        );
//
//        String filePrefix = "cut_video";
//        String fileExtn = ".mp4";
//        String yourRealPath = getPath(getContext(), mSrc);
//        File dest = new File(CONVERTED_AUDIO_FOLDER_PATH+"AUD"+System.currentTimeMillis()+".mp3");
//        int fileNo = 0;
//        while (dest.exists()) {
//            fileNo++;
//            dest = new File(CONVERTED_AUDIO_FOLDER_PATH+"AUD"+System.currentTimeMillis()+".mp3");
//        }
//
//        Log.d(TAG, "startTrim: src: " + mSrc);
//        Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
//        Log.d(TAG, "startTrim: startMs: " + startMs);
//        Log.d(TAG, "startTrim: endMs: " + endMs);
//        filePath = dest.getAbsolutePath();
//        String[] complexCommand = {"-i", String.valueOf(mSrc),"-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000, "-preset","ultrafast", filePath};
//
//        execFFmpegBinary(complexCommand,type);
//
//    }
//    @SuppressLint("LongLogTag")
//    private void executeRemoveAudioCommand(int startMs, int endMs,int type) {
//        File moviesDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES
//        );
//
//        String filePrefix = "cut_video";
//        String fileExtn = ".mp4";
//        String yourRealPath = getPath(getContext(), mSrc);
//        File dest = new File(REMOVED_AUDIO_VIDEO_FOLDER_PATH+"VID"+System.currentTimeMillis()+".mp4");
//        int fileNo = 0;
//        while (dest.exists()) {
//            fileNo++;
//            dest = new File(REMOVED_AUDIO_VIDEO_FOLDER_PATH+"VID"+System.currentTimeMillis()+".mp4");
//        }
//
//        Log.d(TAG, "startTrim: src: " + mSrc);
//        Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
//        Log.d(TAG, "startTrim: startMs: " + startMs);
//        Log.d(TAG, "startTrim: endMs: " + endMs);
//        filePath = dest.getAbsolutePath();
//        String[] complexCommand = {"-i", String.valueOf(mSrc),"-c","copy","-an","-preset","ultrafast", filePath};
//
//        execFFmpegBinary(complexCommand,type);
//
//    }
//    private void execFFmpegBinary(final String[] command,int type) {
//        mOnTrimVideoListener.onTrimStarted(type);
//       FFmpeg.executeAsync(command, new ExecuteCallback() {
//           @Override
//           public void apply(long executionId, int returnCode) {
//               if (returnCode == RETURN_CODE_SUCCESS) {
//                   Log.i(Config.TAG, "Command execution completed successfully.");
//                   mOnTrimVideoListener.getResult(mSrc,type);
//               } else if (returnCode == RETURN_CODE_CANCEL) {
//                   Log.i(Config.TAG, "Command execution cancelled by user.");
//               } else {
//                   Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                   Config.printLastCommandOutput(Log.INFO);
//               }
//           }
//       });
//
//
//    }


    private String getPath(final Context context, final Uri uri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private void onVideoPrepared(@NonNull MediaPlayer mp) {
        // Adjust the size of the video
        // so it fits on the screen
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = mLinearVideo.getWidth();
        int screenHeight = mLinearVideo.getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        mVideoView.setLayoutParams(lp);

        mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_on_video_icon));

        mDuration = mVideoView.getDuration();
        setSeekBarPosition();

        setTimeFrames();
        setTimeVideo(0);

        if (mOnHgLVideoListener != null) {
            mOnHgLVideoListener.onVideoPrepared();
        }
        mp.start();
        startserviceforVideo();
    }

    private void setSeekBarPosition() {

        if (mDuration >= mMaxDuration) {
            mStartPosition = mDuration / 2 - mMaxDuration / 2;
            mEndPosition = mDuration / 2 + mMaxDuration / 2;

            mRangeSeekBarView.setThumbValue(0, (mStartPosition * 100) / mDuration);
            mRangeSeekBarView.setThumbValue(1, (mEndPosition * 100) / mDuration);

        } else {
            mStartPosition = (int) mRangeSeekBarView.getThumbs().get(0).getPos();
            mEndPosition = mDuration;
        }
        mVideoView.seekTo(mStartPosition);
        setProgressBarPosition(mVideoView.getCurrentPosition());


        mTimeVideo = mDuration;
        mRangeSeekBarView.initMaxWidth();
    }

    private void setTimeFrames() {
        String seconds = getContext().getString(R.string.short_seconds);
        mTextTimeFrame.setText(String.format("%s %s ", stringForTime(mStartPosition),""));
        mTextTimeFrameEnd.setText(String.format("%s %s ", stringForTime(mEndPosition),""));
    }

    private void setTimeVideo(int position) {
        String seconds = getContext().getString(R.string.short_seconds);
        mTextSize.setText(String.format("%s %s", stringForTime(position),""));
        int width = mHolderTopView.getWidth()
                - mHolderTopView.getPaddingLeft()
                - mHolderTopView.getPaddingRight();
        float thumbPos = width * (mHolderTopView.getProgress() / (float)mHolderTopView.getMax());
        mTextSize.setTranslationX(thumbPos) ;
        long diff=mEndPosition-mStartPosition;
        mTextTime.setText(stringForTime((int) diff));

    }

    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case Thumb.LEFT: {
                mStartPosition = (int) ((mDuration * value) / 100L);
                pos=mStartPosition;
                mVideoView.seekTo(mStartPosition);
                break;
            }
            case Thumb.RIGHT: {
                mEndPosition = (int) ((mDuration * value) / 100L);
                break;
            }
        }
        setProgressBarPosition(mVideoView.getCurrentPosition());

        setTimeFrames();
        mTimeVideo = mEndPosition - mStartPosition;
    }

    private void onStopSeekThumbs() {

        mVideoView.pause();
        mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
    }

    private void onVideoCompleted() {
        mVideoView.seekTo(mStartPosition);
        mVideoView.pause();
        mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
    }

    private void notifyProgressUpdate(boolean all) {
        if (mDuration == 0) return;

        int position = mVideoView.getCurrentPosition();
        if (all) {
            for (OnProgressVideoListener item : mListeners) {
                item.updateProgress(position, mDuration, ((position * 100) / mDuration));
            }
        } else {
            mListeners.get(1).updateProgress(position, mDuration, ((position * 100) / mDuration));
        }
    }

    private void updateVideoProgress(int time) {
        if (mVideoView == null) {
            return;
        }

        if (time >= mEndPosition) {
            mVideoView.pause();
            mPlayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
            mResetSeekBar = true;
            return;
        }

        if (mHolderTopView != null) {
            // use long to avoid overflow
            setProgressBarPosition(time);
        }
        setTimeVideo(time);
    }

    private void setProgressBarPosition(int position) {
        if (mDuration > 0) {
            long pos = 1000L * position / mDuration;
            mHolderTopView.setProgress((int) pos);
            mTextSize.setText(timeConversion(mHolderTopView.getProgress()));
            int width = mHolderTopView.getWidth()
                    - mHolderTopView.getPaddingLeft()
                    - mHolderTopView.getPaddingRight();
            float thumbPos = width * (mHolderTopView.getProgress() / (float)mHolderTopView.getMax());
            mTextSize.setTranslationX(thumbPos) ;
        }
    }

    /**
     * Set video information visibility.
     * For now this is for debugging
     *
     * @param visible whether or not the videoInformation will be visible
     */
    public void setVideoInformationVisibility(boolean visible) {
        mTimeInfoContainer.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * Listener for events such as trimming operation success and cancel
     *
     * @param onTrimVideoListener interface for events
     */
    @SuppressWarnings("unused")
    public void setOnTrimVideoListener(OnTrimVideoListener onTrimVideoListener) {
        mOnTrimVideoListener = onTrimVideoListener;
    }

    /**
     * Listener for some {@link VideoView} events
     *
     * @param onHgLVideoListener interface for events
     */
    @SuppressWarnings("unused")
    public void setOnHgLVideoListener(OnHgLVideoListener onHgLVideoListener) {
        mOnHgLVideoListener = onHgLVideoListener;
    }

    /**
     * Sets the path where the trimmed video will be saved
     * Ex: /storage/emulated/0/MyAppFolder/
     *
     * @param finalPath the full path
     */
    @SuppressLint("LongLogTag")
    @SuppressWarnings("unused")
    public void setDestinationPath(final String finalPath) {
        mFinalPath = finalPath;
        Log.d(TAG, "Setting custom path " + mFinalPath);
    }

    /**
     * Cancel all current operations
     */
    public void destroy() {
        BackgroundExecutor.cancelAll("", true);
        UiThreadExecutor.cancelAll("");
    }

    /**
     * Set the maximum duration of the trimmed video.
     * The trimmer interface wont allow the user to set duration longer than maxDuration
     *
     * @param maxDuration the maximum duration of the trimmed video in seconds
     */
    @SuppressWarnings("unused")
    public void setMaxDuration(int maxDuration) {
       // mMaxDuration = maxDuration * 1000;
        mMaxDuration = maxDuration;
     }

    /**
     * Sets the uri of the video to be trimmer
     *
     * @param videoURI Uri of the video
     */
    @SuppressWarnings("unused")
    public void setVideoURI(final Uri videoURI) {
        mSrc = videoURI;

        if (mOriginSizeFile == 0) {
            File file = new File(mSrc.getPath());

            mOriginSizeFile = file.length();
            long fileSizeInKB = mOriginSizeFile / 1024;

        }

        mVideoView.setVideoURI(mSrc);
        mVideoView.requestFocus();

        VideoTimeLine.with(String.valueOf(mSrc)).show(timelineGlSurfaceView);
    }

    private static class MessageHandler extends Handler {

        @NonNull
        private final WeakReference<HgLVideoTrimmer> mView;

        MessageHandler(HgLVideoTrimmer view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            HgLVideoTrimmer view = mView.get();
            if (view == null || view.mVideoView == null) {
                return;
            }

            view.notifyProgressUpdate(true);
            if (view.mVideoView.isPlaying()) {
                sendEmptyMessageDelayed(0, 10);
            }
        }
    }
    private boolean isVideoHaveAudioTrack(String path) {
        boolean audioTrack =false;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String hasAudioStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);
        if(hasAudioStr!=null && hasAudioStr.equals("yes")){
            audioTrack=true; }
        else{
            audioTrack=false; }

        return audioTrack;
    }
}
