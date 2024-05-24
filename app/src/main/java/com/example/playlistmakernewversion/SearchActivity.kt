package com.example.playlistmakernewversion

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

class SearchActivity : AppCompatActivity() {

    private val baseUrl = "https://itunes.apple.com"

    var saveText = ""
    private val arrayTracks = mutableListOf<Track>()
    private val arrayTracksHistory = mutableListOf<Track>()
    private val tracksAdapter = TrackAdapter(arrayTracks)
    private val tracksAdapterHistory = TrackAdapter(arrayTracksHistory)

    private lateinit var searchHistory: SearchHistory
    private lateinit var buttonBack: ImageView
    private lateinit var buttonClear: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeHolderNothingFound: LinearLayout
    private lateinit var placeholderErrorNetwork: LinearLayout
    private lateinit var buttonPlaceholder: Button
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var clearHistoryButton: Button

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPreferences = getSharedPreferences(KEY_SEARCH_PREF, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        buttonBack = findViewById(R.id.button_arrowBack_search)
        buttonClear = findViewById(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)
        placeHolderNothingFound = findViewById(R.id.placeholderNothingFound)
        placeholderErrorNetwork = findViewById(R.id.placeholderErrorNetwork)
        buttonPlaceholder = findViewById(R.id.button_placeholder)
        searchHistoryLayout = findViewById(R.id.searchHistory_layout)
        recyclerView = findViewById(R.id.recyclerViewTracks)
        recyclerViewHistory = findViewById((R.id.recyclerViewHistory))
        clearHistoryButton = findViewById(R.id.buttonClearHistory)

        recyclerView.adapter = tracksAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        recyclerViewHistory.adapter = tracksAdapterHistory
        recyclerViewHistory.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )


        inputEditText.setText(saveText)

        buttonBack.setOnClickListener {
            finish()
        }

        buttonClear.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            arrayTracks.clear()
            tracksAdapter.notifyDataSetChanged()
            placeHolderNothingFound.visibility = View.GONE
            placeholderErrorNetwork.visibility = View.GONE
            recyclerView.visibility = View.GONE
            searchHistoryLayout.visibility = if (searchHistory.getList().isEmpty())
                View.GONE else View.VISIBLE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.visibility = clearButtonVisibility(s)
                searchHistoryLayout.visibility =
                    if (inputEditText.hasFocus() && s?.isEmpty() == true && searchHistory.getList()
                            .isNotEmpty()
                    ) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                saveText = s.toString()
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)




        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(inputEditText.text.toString())
            }
            false
        }

        buttonPlaceholder.setOnClickListener {
            search(inputEditText.text.toString())
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus && inputEditText.text.isEmpty() && searchHistory.getList().isNotEmpty()) {
                searchHistoryLayout.visibility = View.VISIBLE
                arrayTracksHistory.clear()
                arrayTracksHistory.addAll(searchHistory.getList())
                tracksAdapterHistory.notifyDataSetChanged()
            } else {
                searchHistoryLayout.visibility = View.GONE
            }
        }


        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == SEARCH_HISTORY_KEY) {
                arrayTracksHistory.clear()
                arrayTracksHistory.addAll(searchHistory.getList())
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        clearHistoryButton.setOnClickListener {
            arrayTracksHistory.clear()
            searchHistory.clear()
            tracksAdapterHistory.notifyDataSetChanged()
            searchHistoryLayout.visibility = View.GONE
        }

        tracksAdapter.onItemClick = {
            showTrack(it)
            searchHistory.addTrek(it)
        }

        tracksAdapterHistory.onItemClick = {
            showTrack(it)
            searchHistory.addTrek(it)
        }
    }

    override fun onResume() {
        super.onResume()
        tracksAdapterHistory.notifyDataSetChanged()
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_AMOUNT, saveText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveText = savedInstanceState.getString(INPUT_AMOUNT, AMOUNT_DEF)
    }

    private fun showPlaceholderNothingFound() {
        placeHolderNothingFound.visibility = View.VISIBLE
        arrayTracks.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showPlaceholderErrorNetwork() {
        placeholderErrorNetwork.visibility = View.VISIBLE
        arrayTracks.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun search(inputText: String) {
        if (inputText.isNotEmpty()) {
            placeHolderNothingFound.visibility = View.GONE
            placeholderErrorNetwork.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            itunesService.search(inputText).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>

                ) {
                    val newResponse = response.body()?.results
                    if (response.isSuccessful) {
                        arrayTracks.clear()
                        if (newResponse != null) {
                            if (newResponse.isNotEmpty()) {
                                arrayTracks.addAll(response.body()?.results!!)
                                tracksAdapter.notifyDataSetChanged()
                            } else {
                                showPlaceholderNothingFound()
                            }
                        }
                    } else {
                        showPlaceholderErrorNetwork()
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showPlaceholderErrorNetwork()
                }
            })
        }
    }

    fun showTrack(track: Track) {
        startActivity(
            Intent(this, PlayerActivity::class.java).putExtra(
                "track",
                Gson().toJson(track)
            )
        )
    }


    companion object {
        const val INPUT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
        private const val KEY_SEARCH_PREF = "KEY_SEARCH_PREF"
    }
}

