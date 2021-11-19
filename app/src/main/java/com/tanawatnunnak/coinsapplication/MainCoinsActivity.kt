package com.tanawatnunnak.coinsapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tanawatnunnak.coinsapplication.ui.coin_list.CoinListFragment
import com.tanawatnunnak.coinsapplication.databinding.ActivityMainCoinsBinding

class MainCoinsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainCoinsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCoinsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(R.id.mainCoinsContainer, CoinListFragment.newInstance())
            .commit()
    }
}