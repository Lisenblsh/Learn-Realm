package com.lis.learnrealm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.lis.learnrealm.database.Cat
import com.lis.learnrealm.database.Database
import com.lis.learnrealm.databinding.ActivityMainBinding
import com.lis.learnrealm.network.RetrofitService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val database = Database
    private val repository: Repository = Repository(RetrofitService.create())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setButtonRefresh()
        binding.setButtonDelete()
        lifecycleScope.launch {
            binding.setImage()
        }

    }

    private suspend fun ActivityMainBinding.setImage() {
        val image = getImage()
        viewIds()
        Glide.with(this@MainActivity).load(image).into(catImage)
    }

    private suspend fun getImage(): String? {
        val response = repository.getCatImage()
        return if (response.isSuccessful) {
            val body = response.body()
            return if (body != null) {
                writeToDB(body)
                response.body()?.get(0)?.url
            } else null
        } else {
            Toast.makeText(this, response.message(), Toast.LENGTH_SHORT).show()
            getFromDB()
        }
    }

    private fun getFromDB(): String? {
        return database.query()?.url
    }

    private suspend fun writeToDB(body: List<Cat>) {
        database.writeAsync(body[0])
    }

    private fun ActivityMainBinding.setButtonRefresh() {
        buttonChange.setOnClickListener {
            lifecycleScope.launch {
                setImage()
            }
        }
    }

    private suspend fun ActivityMainBinding.viewIds() {
        lifecycleScope.launch {
            database.queryAsync().collect {
                Log.e("flow","getFlow")
                var string = ""
                it.list.forEachIndexed { index, cat ->
                    string += "$index: ${cat.id}\n"
                }
                idsText.text = string
            }


        }
    }
    private fun ActivityMainBinding.setButtonDelete() {

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                database.delete(deleteEdit.text.toString())
                viewIds()
            }
        }
    }
}
