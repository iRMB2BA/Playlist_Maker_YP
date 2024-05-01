package com.example.playlistmakernewversion

import android.app.Activity
import android.content.Context
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val baseUrl = "https://itunes.apple.com"

    var saveText = ""
    private val arrayTracks = mutableListOf<Track>()
    private val tracksAdapter = TrackAdapter(arrayTracks)

    private lateinit var buttonBack: ImageView
    private lateinit var buttonClear: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeHolderNothingFound: LinearLayout
    private lateinit var placeholderErrorNetwork: LinearLayout
    private lateinit var buttonPlaceholder: Button

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        buttonBack = findViewById(R.id.button_arrowBack_search)
        buttonClear = findViewById(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)
        placeHolderNothingFound = findViewById(R.id.placeholderNothingFound)
        placeholderErrorNetwork = findViewById(R.id.placeholderErrorNetwork)
        buttonPlaceholder = findViewById(R.id.button_placeholder)

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

        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                saveText = s.toString()
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = tracksAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(inputEditText.text.toString())
            }
            false
        }

        buttonPlaceholder.setOnClickListener {
            search(inputEditText.text.toString())
        }
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


    companion object {
        const val INPUT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
    }
}

