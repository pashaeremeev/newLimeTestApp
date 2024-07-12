package com.example.new_practice.app.fragments

import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.new_practice.app.ClickIconListener
import com.example.new_practice.app.Quality
import com.example.new_practice.R
import com.example.new_practice.app.AppViewModelFactory
import com.example.new_practice.app.VideoViewModel
import com.example.new_practice.app.adapters.ChannelIconAdapter
import com.example.new_practice.data.repos.ChannelRepositoryImpl
import com.example.new_practice.domain.models.ChannelModel


@UnstableApi
class VideoFragment : Fragment() {
    private var playerView: PlayerView? = null
    private var progressBar: ProgressBar? = null
    private var player: ExoPlayer? = null
    private var isFullScreen = false
    private var runnable: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: ChannelIconAdapter

    private var channelId: Int? = null
    private var channels: List<ChannelModel> = listOf()
    private lateinit var iconChannel: ImageView
    private lateinit var toPrevChannel: ImageView
    private lateinit var toNextChannel: ImageView
    private lateinit var catalog: RecyclerView
    private lateinit var channelName: TextView
    private lateinit var channelTop: LinearLayout
    private lateinit var playerInfo: LinearLayout
    private lateinit var playerControl: LinearLayout
    private lateinit var channelRepo: ChannelRepositoryImpl
    private lateinit var vm: VideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment: View = inflater.inflate(R.layout.fragment_videoplayer, container, false)

        container!!.visibility = VISIBLE
        playerView = fragment.findViewById(R.id.exoplayerView)
        progressBar = fragment.findViewById(R.id.progressBar)
        vm = ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(requireContext())
        )[VideoViewModel::class.java]
        channelRepo = ChannelRepositoryImpl.getInstance(context)
        //channelId = requireArguments().getInt(BUNDLE_ID_KEY)
        channelRepo.currentChannelId.value = requireArguments().getInt(BUNDLE_ID_KEY)

        adapter = ChannelIconAdapter(requireContext(), object : ClickIconListener {
            override fun invoke(channel: ChannelModel) {
                updateChannel(channel.id)
                channelRepo.currentChannelId.value = channel.id
            }
        })

        iconChannel = playerView!!.findViewById(R.id.iconChannel)
        channelName = playerView!!.findViewById(R.id.name)
        toPrevChannel = playerView!!.findViewById(R.id.toBackChannel)
        toNextChannel = playerView!!.findViewById(R.id.toFwdChannel)
        catalog = playerView!!.findViewById(R.id.catalogView)
        channelTop = playerView!!.findViewById(R.id.channelTop)
        playerInfo = playerView!!.findViewById(R.id.playerInfo)
        playerControl = playerView!!.findViewById(R.id.playerControl)

        toPrevChannel.setOnClickListener {
            val curChannel: ChannelModel? = channels.firstOrNull { it.id == channelId }
            val index: Int =
                if (channels.indexOf(curChannel) - 1 < 0) channels.size - 1
                else channels.indexOf(curChannel) - 1
            val prevChannel: ChannelModel = channels[index]
            channelId = prevChannel.id
            updateChannel(prevChannel.id)
            channelRepo.currentChannelId.value = prevChannel.id
        }

        toNextChannel.setOnClickListener {
            val curChannel: ChannelModel? = channels.firstOrNull { it.id == channelId }
            val index: Int =
                if (channels.indexOf(curChannel) + 1 >= channels.size) 0
                else channels.indexOf(curChannel) + 1
            val nextChannel: ChannelModel = channels[index]
            channelId = nextChannel.id
            updateChannel(nextChannel.id)
            channelRepo.currentChannelId.value = nextChannel.id
        }

        val settingsBtn: ImageView? = playerView?.findViewById(R.id.settingsBtn)
        val fullScreenBtn: ImageView? = playerView?.findViewById(R.id.fullscreenBtn)
        val backBtn: ImageView? = playerView?.findViewById(R.id.backBtn)
        val tvShow: TextView? = playerView?.findViewById(R.id.tvShow)

        catalog?.adapter = adapter

        vm.getCatalogOfChannel().observe(viewLifecycleOwner) { catalog ->
            this.channels = catalog
            val channel: ChannelModel? = channels.firstOrNull { it.id == channelId }
            channelId = channel?.id
            //updateChannel(channelId!!)
            updateChannel(channelRepo.currentChannelId.value!!)
        }

        vm.getEpgForChannel(channelRepo.currentChannelId).observe(viewLifecycleOwner) {
            tvShow!!.text = it?.title
        }

        val newTimeBar: SeekBar? = playerView?.findViewById(R.id.newProgress)

        newTimeBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        settingsBtn?.setOnClickListener {
            val qualities: ArrayList<Quality> = ArrayList()
            if (player?.currentTracks!!.groups.size > 0) {
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
        }

        fullScreenBtn?.setOnClickListener {
            if (isFullScreen) {
                requireActivity().requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                isFullScreen = false
            } else {
                requireActivity().requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                isFullScreen = true
            }
        }

        backBtn?.setOnClickListener {
            onDestroyView()
            onDestroy()
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }

        val trackSelector = DefaultTrackSelector(requireContext())
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())
        val loadControl: LoadControl = DefaultLoadControl()
        player = ExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .build()
        playerView?.player = player
        playerView?.keepScreenOn = true

        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        newTimeBar?.max = player?.duration!!.toInt()
                        runnable = Runnable {
                            newTimeBar?.progress = player?.currentPosition!!.toInt()
                            handler.postDelayed(runnable!!, 1000)
                        }
                        handler.postDelayed(runnable!!, 0)
                        progressBar?.visibility = GONE
                    }
                    Player.STATE_BUFFERING -> {
                        progressBar?.visibility = VISIBLE
                    }
                    else -> {
                        progressBar?.visibility = GONE
                    }
                }
            }
        })

        val pipBtn: ImageView? = playerView?.findViewById(R.id.pipBtn)
        pipBtn?.setOnClickListener {
            val ratio = Rational(16, 9)
            val pipBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PictureInPictureParams.Builder()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            pipBuilder.setAspectRatio(ratio).build()
            requireActivity().enterPictureInPictureMode(pipBuilder.build())
        }

        val ppBtn: ImageView? = playerView?.findViewById(R.id.exo_play)
        ppBtn?.setOnClickListener {
            if (player?.isPlaying == true) {
                player?.pause()
                player?.playWhenReady = false
                ppBtn?.setImageResource(R.drawable.ic_arrow_play)
            } else {
                player?.play()
                player?.playWhenReady = true
                ppBtn?.setImageResource(R.drawable.ic_pause)
            }
        }

//        Uri videoUrl = Uri.parse("https://mhd.iptv2022.com/p/5tzYJRkx_8x4VIGmmym0KA,1689751804/streaming/1kanalott/324/1/index.m3u8");
//        Uri videoUrl = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
//        Uri videoUrl = Uri.parse("https://alanza.iptv2022.com/Miami_TV/index.m3u8");
//        Uri videoUrl = Uri.parse("https://alanza.iptv2022.com/LawCrime-eng/index.m3u8");

        return fragment
    }

    private fun startChannel(channelId: Int) {
        player?.stop()
//        player?.playWhenReady = false
//        player?.release()
        player?.clearMediaItems()

        val channel: ChannelModel? = channels.firstOrNull {it.id == channelId}
        val videoUrl = Uri.parse(channel?.stream)
        Glide.with(requireContext())
            .load(channel?.image)
            .into(iconChannel)
        channelName.text = channel?.name
        val mediaItem = MediaItem.fromUri(videoUrl)
        val httpDataSourceFactory: DefaultHttpDataSource.Factory =
            DefaultHttpDataSource.Factory()
                .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
                .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)
                .setAllowCrossProtocolRedirects(true)
        val requestProperties = HashMap<String, String>()
        requestProperties["X-LHD-Agent"] =
            "{\"platform\":\"android\",\"app\":\"stream.tv.online\",\"version_name\":\"3.1.3\",\"version_code\":\"400\",\"sdk\":\"29\",\"name\":\"Huawei+Wgr-w19\",\"device_id\":\"a4ea673248fe0bcc\",\"is_huawei\":\"0\"}"
        httpDataSourceFactory.setDefaultRequestProperties(requestProperties)
        val mediaSource: MediaSource =
            HlsMediaSource.Factory(httpDataSourceFactory).createMediaSource(mediaItem)
        player?.setMediaSource(mediaSource)
        //        player.setMediaItem(mediaItem);
        player?.prepare()
        player?.playWhenReady = true
        val result = adapter.setChannels(channels)
        result.dispatchUpdatesTo(adapter)
    }

    private fun updateChannel(newChannelId: Int) {
        channelId = newChannelId

        startChannel(newChannelId)

        adapter.setSelected(channels.indexOfFirst { it.id == newChannelId })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireView().visibility = GONE
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

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        if (isInPictureInPictureMode) {
            channelTop.visibility = GONE
            playerInfo.visibility = GONE
            playerControl.visibility = GONE
        } else {
            channelTop.visibility = VISIBLE
            playerInfo.visibility = VISIBLE
            playerControl.visibility = VISIBLE
        }
        player?.playWhenReady = true
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