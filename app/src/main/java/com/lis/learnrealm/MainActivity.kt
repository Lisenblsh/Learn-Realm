package com.lis.learnrealm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.lis.learnrealm.database.CatImage
import com.lis.learnrealm.databinding.ActivityMainBinding
import com.lis.learnrealm.network.RetrofitService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val repository: Repository = Repository(RetrofitService.create())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setButton()
        lifecycleScope.launch{
            binding.setImage()
        }

    }

    private suspend fun ActivityMainBinding.setImage() {
        val image = getImage()
        Glide.with(this@MainActivity).load(image).into(catImage)
    }

    private suspend fun getImage(): String? {
        val response = repository.getCatImage()
        return if(response.isSuccessful){
            response.body()?.get(0)?.url
        } else {
            Toast.makeText(this, response.message(),Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun ActivityMainBinding.setButton() {
        buttonChange.setOnClickListener {
            lifecycleScope.launch {
                setImage()
            }
        }
    }
}
