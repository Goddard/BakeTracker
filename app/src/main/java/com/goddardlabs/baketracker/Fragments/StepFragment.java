package com.goddardlabs.baketracker.Fragments;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goddardlabs.baketracker.Parcelables.Step;
import com.goddardlabs.baketracker.R;
import com.goddardlabs.baketracker.databinding.FragmentStepBinding;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class StepFragment extends Fragment implements ExoPlayer.EventListener {
    private final String TAG = StepFragment.class.getSimpleName();
    private FragmentStepBinding binding;
    private Step mStep;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer exoPlayer;

    private int startWindow;
    private long startPosition;
    private String startURL;

    public StepFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if ((arguments != null) && (arguments.containsKey(getString(R.string.STEP_DATA)))) {
            mStep = arguments.getParcelable(getString(R.string.STEP_DATA));

            this.startURL = mStep.getVideoURL();
        }

        Log.i("Before restore", String.valueOf(this.startPosition));

        if(savedInstanceState != null) {
            this.startWindow = savedInstanceState.getInt("PLAYER_WINDOW");
            this.startPosition = savedInstanceState.getLong("PLAYER_POSITION");
            this.startURL = savedInstanceState.getString("PLAYER_URL");
            Log.i("After restore", String.valueOf(this.startPosition));
        } else {
            clearStartPosition();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);
        final View view = binding.getRoot();

        if(!getResources().getBoolean(R.bool.is_tablet)) {
            binding.toolbarContainer.toolbar.setVisibility(View.GONE);
        }

        if(mStep.getVideoURL() != null && !mStep.getVideoURL().matches("")) {
            initializeMediaSession();
            initializePlayer(Uri.parse(mStep.getVideoURL()));
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !getResources().getBoolean(R.bool.is_tablet)) {
                hideUI();
                binding.tvStepFragmentDirections.setVisibility(View.GONE);
                binding.exoStepFragmentPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                binding.exoStepFragmentPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                binding.tvStepFragmentDirections.setText(mStep.getDescription());
            }
        } else {
            binding.exoStepFragmentPlayerView.setVisibility(View.GONE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !getResources().getBoolean(R.bool.is_tablet)) {
                hideUI();
                binding.tvStepFragmentDirections.setVisibility(View.GONE);
                binding.ivStepFragmentNoVideoPlaceholder.setVisibility(View.VISIBLE);
            } else {
                binding.tvStepFragmentDirections.setText(mStep.getDescription());
            }
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if(mMediaSession!=null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if(!isVisibleToUser) {
                if (exoPlayer != null) {
                    exoPlayer.setPlayWhenReady(false);
                }
            }
        }
    }

    private void hideUI() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS | PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            binding.exoStepFragmentPlayerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "RecipeStepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            boolean haveStartPosition = startWindow != C.INDEX_UNSET;
            if(haveStartPosition) {
                exoPlayer.seekTo(startWindow, startPosition);
            }
            exoPlayer.prepare(mediaSource, !haveStartPosition, false);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if(exoPlayer != null) {
            updateStartPosition();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

    @Override
    public void onLoadingChanged(boolean isLoading) {}

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {}

    @Override
    public void onPositionDiscontinuity() {}

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null && exoPlayer.getPlayWhenReady()) {
            this.startPosition = exoPlayer.getCurrentPosition();
        }

        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(exoPlayer != null) {
            exoPlayer.seekTo(startPosition);
            exoPlayer.setPlayWhenReady(true);
        } else {
            initializeMediaSession();
            initializePlayer(Uri.parse(this.startURL));
        }
    }

    private void updateStartPosition() {
        if (exoPlayer != null) {
            this.startWindow = exoPlayer.getCurrentWindowIndex();
            this.startPosition = Math.max(0, exoPlayer.getCurrentPosition());
            this.startURL = mStep.getVideoURL().toString();
        }
    }

    private void clearStartPosition() {
        this.startWindow = C.INDEX_UNSET;
        this.startPosition = C.TIME_UNSET;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        updateStartPosition();
        currentState.putInt("PLAYER_Window", this.startWindow);
        currentState.putLong("PLAYER_POSITION", this.startPosition);
        currentState.putString("PLAYER_URL", this.startURL);
    }
}