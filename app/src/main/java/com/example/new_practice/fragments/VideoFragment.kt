package com.example.new_practice.fragments

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import com.example.new_practice.Channel
import com.example.new_practice.Epg
import com.example.new_practice.Quality
import com.example.new_practice.R
import com.example.new_practice.repos.ChannelRepo
import com.example.new_practice.repos.EpgRepo

@UnstableApi
class VideoFragment : Fragment() {
    private var playerView: PlayerView? = null
    private var progressBar: ProgressBar? = null
    private var player: ExoPlayer? = null
    private var isFullScreen = false
    private var runnable: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment: View = inflater.inflate(R.layout.fragment_videoplayer, container, false)
        container!!.visibility = View.VISIBLE
        val channelRepo = ChannelRepo(context)
        val epgRepo = EpgRepo.getInstance(context)
        val channelId = requireArguments().getInt(BUNDLE_ID_KEY)
        val channel: Channel? = channelRepo.getById(channelId)
        val epgOfChannel: Epg? = epgRepo.getById(channelId)
        val videoUrl = Uri.parse(channel?.stream)
        playerView = fragment.findViewById(R.id.exoplayerView)
        progressBar = fragment.findViewById<ProgressBar>(R.id.progressBar)
        val settingsBtn: ImageView? = playerView?.findViewById(R.id.settingsBtn)
        val fullScreenBtn: ImageView? = playerView?.findViewById(R.id.fullscreenBtn)
        val backBtn: ImageView? = playerView?.findViewById(R.id.backBtn)
        val iconChannel: ImageView? = playerView?.findViewById(R.id.iconChannel)
        val channelName: TextView? = playerView?.findViewById(R.id.name)
        val tvShow: TextView? = playerView?.findViewById(R.id.tvShow)
        Glide.with(requireContext())
            .load(channel?.image)
            .into(iconChannel!!)
        channelName!!.text = channel?.name
        tvShow!!.text = epgOfChannel?.title

        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        Uri videoUrl = Uri.parse("https://mhd.iptv2022.com/p/5tzYJRkx_8x4VIGmmym0KA,1689751804/streaming/1kanalott/324/1/index.m3u8");
//        Uri videoUrl = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
//        Uri videoUrl = Uri.parse("https://alanza.iptv2022.com/Miami_TV/index.m3u8");
//        Uri videoUrl = Uri.parse("https://alanza.iptv2022.com/LawCrime-eng/index.m3u8");
        val trackSelector = DefaultTrackSelector(requireContext())
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())
        val loadControl: LoadControl = DefaultLoadControl()
        player = ExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .build()
        playerView?.player = player
        playerView?.keepScreenOn = true
        val mediaItem = MediaItem.fromUri(videoUrl)
        val httpDataSourceFactory: DefaultHttpDataSource.Factory = DefaultHttpDataSource.Factory()
            .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)
            .setAllowCrossProtocolRedirects(true)
        val requestProperties = HashMap<String, String>()
        requestProperties["X-LHD-Agent"] =
            "{\"platform\":\"android\",\"app\":\"stream.tv.online\",\"version_name\":\"3.1.3\",\"version_code\":\"400\",\"sdk\":\"29\",\"name\":\"Huawei+Wgr-w19\",\"device_id\":\"a4ea673248fe0bcc\",\"is_huawei\":\"0\"}"
        httpDataSourceFactory.setDefaultRequestProperties(requestProperties)
        val mediaSource: MediaSource = HlsMediaSource.Factory(httpDataSourceFactory).createMediaSource(mediaItem)
        player?.setMediaSource(mediaSource)
        //        player.setMediaItem(mediaItem);
        player?.prepare()
        player?.playWhenReady = true
        val newTimeBar: SeekBar? = playerView?.findViewById(R.id.newProgress)
        newTimeBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        settingsBtn?.setOnClickListener {
            val qualities: ArrayList<Quality> = ArrayList<Quality>()
            for (i in 0 until player?.currentTracks!!.groups[0].length) {
                val height: Int = player?.currentTracks!!.groups[0].mediaTrackGroup
                    .getFormat(i).height
                val width: Int = player?.currentTracks!!.groups[0].mediaTrackGroup
                    .getFormat(i).width
                qualities.add(Quality(width, height, i))
            }
            var currentIndex = qualities.size
            qualities.add(Quality(-1, -1, qualities.size))
            for (i in 0 until player?.currentTracks!!.groups[0].length) {
                if (player?.currentTracks!!.groups[0].isTrackSelected(i)
                    && !player?.trackSelector!!.parameters.overrides.isEmpty()
                ) {
                    currentIndex = i
                }
            }
            if (!ItemQualityFragment.isExist) {
                val itemQualityFragment: ItemQualityFragment =
                    ItemQualityFragment.getInstance(qualities, currentIndex)
                itemQualityFragment.show(requireActivity().supportFragmentManager, null)
            }
            requireActivity().supportFragmentManager
                .setFragmentResultListener(
                    ItemQualityFragment.REQUEST_KEY,
                    this
                ) { requestKey: String?, result: Bundle ->
                    val index = result.getInt(ItemQualityFragment.BUNDLE_KEY)
                    if (index + 1 == qualities.size) {
                        val parameters: TrackSelectionParameters =
                            player?.trackSelector!!.parameters.buildUpon()
                                .clearOverrides()
                                .build()
                        player?.trackSelector!!.parameters = parameters
                    } else {
                        val parameters: TrackSelectionParameters =
                            player?.trackSelector!!.parameters.buildUpon().addOverride(
                                TrackSelectionOverride(
                                    player?.currentTracks!!.groups[0].mediaTrackGroup,
                                    index
                                )
                            ).build()
                        player?.trackSelector!!.parameters = parameters
                    }
                    requireActivity().supportFragmentManager
                        .clearFragmentResultListener(ItemQualityFragment.REQUEST_KEY)
                }
        }
        fullScreenBtn?.setOnClickListener { view: View? ->
            if (isFullScreen) {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                isFullScreen = false
            } else {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                isFullScreen = true
            }
        }
        backBtn?.setOnClickListener {
            onDestroyView()
            onDestroy()
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    newTimeBar?.max = player?.duration as Int
                    runnable = Runnable {
                        newTimeBar?.progress = player?.currentPosition as Int
                        handler.postDelayed(runnable!!, 1000)
                    }
                    handler.postDelayed(runnable!!, 0)
                    progressBar?.visibility = View.GONE
                } else if (state == Player.STATE_BUFFERING) {
                    progressBar?.visibility = View.VISIBLE
                } else {
                    progressBar?.visibility = View.GONE
                }
            }
        })
        val ppBtn: ImageView? = playerView?.findViewById(R.id.exo_play)
        ppBtn?.setOnClickListener { view: View? ->
            if (player?.isPlaying == true) {
                player?.pause()
                player?.playWhenReady = false
                ppBtn?.setImageResource(R.drawable.ic_arrow_play)
            } else {
                player?.play()
                player?.playWhenReady = false
                ppBtn?.setImageResource(R.drawable.ic_pause)
            }
        }
        return fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireView().visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        player?.seekToDefaultPosition()
        player?.playWhenReady = true
    }

    override fun onPause() {
        player?.playWhenReady = false
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }

    companion object {
        const val BUNDLE_ID_KEY = "BUNDLE_ID_KEY"
        fun getInstance(channelId: Int): VideoFragment {
            val videoFragment = VideoFragment()
            val bundle = Bundle()
            bundle.putInt(BUNDLE_ID_KEY, channelId)
            videoFragment.arguments = bundle
            return videoFragment
        }
    }
}