package com.example.playlistmakernewversion.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.databinding.ActivitySearchBinding
import com.example.playlistmakernewversion.player.domain.models.Track
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_ARTIST_NAME
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_COLLECTION_NAME
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_COUNTRY
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_DATA
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_IMAGE
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_PRIMARY_NAME
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_SONG
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_TIME_MILLIS
import com.example.playlistmakernewversion.player.ui.activity.EXTRA_TRACK_NAME
import com.example.playlistmakernewversion.player.ui.activity.PlayerActivity
import com.example.playlistmakernewversion.search.ui.adapter.HistoryAdapter
import com.example.playlistmakernewversion.search.ui.adapter.SearchAdapter
import com.example.playlistmakernewversion.search.ui.view_model.SearchViewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var searchText: String = ""

    private var flag = false
    private val tracksHistory = ArrayList<Track>()
    private val tracksSearch = ArrayList<Track>()

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var vm: SearchViewModel

    private var isClickAllowed = true


    private val historyAdapter = HistoryAdapter {
        if (clickDebounce()) {
            openPlayerToIntent(it)
        }
    }

    private val searchAdapter = SearchAdapter {
        if (clickDebounce()) {
            vm.addHistoryTrack(it)
            openPlayerToIntent(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(
            this,
            SearchViewModel.factoryViewModelSearch()
        )[SearchViewModel::class.java]

        init()
        vm.addHistoryTracks(tracksHistory)


        vm.observeState().observe(this) {
            render(it)
        }

        binding.buttonArrowBackSearch.setOnClickListener() {
            finish()
        }

        binding.buttonClearHistory.setOnClickListener {
            vm.clearTrackListHistory(historyAdapter.trackListHistory)
        }

        vm.clearHistory.observe(this) {
            binding.historyMenu.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }

        binding.apply {
            binding.clearIcon.setOnClickListener {
                clearSearch()
            }

            inputEditText.setOnFocusChangeListener { view, hasFocus ->
                historyMenu.visibility =
                    if (hasFocus && inputEditText.text.isEmpty() &&
                        tracksHistory.isNotEmpty()
                    ) View.VISIBLE else View.GONE

                if (!hasFocus) vm.addHistoryList(historyAdapter.trackListHistory)

            }
        }

        binding.btResetSearch.setOnClickListener {
            binding.massageNotInternet.visibility = View.GONE
            vm.searchRequest(binding.inputEditText.text.toString())
        }

        binding.inputEditText.addTextChangedListener {
            binding.clearIcon.visibility = clearButtonVisibility(it)
            vm.searchDebounce(
                changedText = it?.toString() ?: ""
            )

            searchAdapter.notifyDataSetChanged()

            if (searchText.isNotEmpty()) {
                binding.historyMenu.visibility = View.GONE
            }
        }

    }

    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Content -> showContent(state.tracks)
            is TrackState.Empty -> showEmpty()
            is TrackState.Error -> showError()
            is TrackState.Loading -> showLoading()
        }
    }

    fun showLoading() {
        binding.rcViewSearch.visibility = View.GONE
        binding.historyMenu.visibility = View.GONE
        binding.massageNotFound.visibility = View.GONE
        binding.massageNotInternet.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError() {
        binding.rcViewSearch.visibility = View.GONE
        binding.massageNotInternet.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.massageNotFound.visibility = View.GONE
        binding.placeholderMessageNotInternet.text = getString(R.string.not_internet)


    }

    private fun showEmpty() {
        binding.rcViewSearch.visibility = View.GONE
        binding.massageNotFound.visibility = View.VISIBLE
        binding.massageNotInternet.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderMessage.text = getString(R.string.nothing_not_found)
    }

    private fun showContent(track: List<Track>) {
        binding.massageNotFound.visibility = View.GONE
        binding.massageNotInternet.visibility = View.GONE
        binding.rcViewSearch.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.historyMenu.visibility = View.GONE
        searchAdapter.trackList.clear()
        searchAdapter.trackList.addAll(track)
        searchAdapter.notifyDataSetChanged()
    }


    fun clearSearch() {
        binding.apply {
            tracksSearch.clear()
            inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(inputEditText.windowToken, 0) // скрыть клавиатуру
            inputEditText.clearFocus()
            progressBar.visibility = View.GONE
            massageNotFound.visibility = View.GONE
            massageNotInternet.visibility = View.GONE
            searchAdapter.notifyDataSetChanged()
            historyAdapter.notifyDataSetChanged()
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchText)
        outState.putParcelableArrayList(TRACK_QUERY, searchAdapter.trackList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.inputEditText.setText(savedInstanceState.getString(SEARCH_QUERY, ""))
        val trackSave = savedInstanceState.getParcelableArrayList<Track>(TRACK_QUERY)
        if (trackSave != null) {
            searchAdapter.trackList.addAll(trackSave)
        }
    }

    private fun init() {
        searchAdapter.trackList = tracksSearch
        historyAdapter.trackListHistory = tracksHistory

        binding.apply {
            rcViewSearch.layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            rcViewSearch.adapter = searchAdapter

            rcViewHistory.layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            rcViewHistory.adapter = historyAdapter

            if (tracksHistory.isEmpty()) {
                historyMenu.visibility = View.GONE
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (!flag) {
            binding.inputEditText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.inputEditText, InputMethodManager.SHOW_IMPLICIT)
            flag = true
        }
    }

    override fun onStop() {
        super.onStop()
        vm.addHistoryList(historyAdapter.trackListHistory)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun openPlayerToIntent(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(EXTRA_TRACK_NAME, track.trackName)
        intent.putExtra(EXTRA_ARTIST_NAME, track.artistName)
        intent.putExtra(EXTRA_TIME_MILLIS, track.trackTimeMillis)
        intent.putExtra(EXTRA_IMAGE, track.artworkUrl100)
        intent.putExtra(EXTRA_DATA, track.releaseDate)
        intent.putExtra(EXTRA_COLLECTION_NAME, track.collectionName)
        intent.putExtra(EXTRA_PRIMARY_NAME, track.primaryGenreName)
        intent.putExtra(EXTRA_COUNTRY, track.country)
        intent.putExtra(EXTRA_SONG, track.previewUrl)
        startActivity(intent)
    }


    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val TRACK_QUERY = "TRACK_QUERY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}

