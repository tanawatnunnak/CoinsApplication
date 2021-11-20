package com.tanawatnunnak.coinsapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tanawatnunnak.coinsapplication.databinding.ActivityMainCoinsBinding
import com.tanawatnunnak.coinsapplication.ui.coin_list.CoinListFragment

class MainCoinsActivity : AppCompatActivity() {
    private var binding: ActivityMainCoinsBinding? = null
    private var coinListFragment: CoinListFragment? = null
    private val FRAGMENT_TAG = "fragment tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCoinsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (savedInstanceState != null) {
            coinListFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as CoinListFragment
        } else if (coinListFragment == null) {
            coinListFragment = CoinListFragment.newInstance()
        }

        coinListFragment?.let { fragment ->
            if (!fragment.isInLayout) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainCoinsContainer, fragment, FRAGMENT_TAG)
                    .commit()
            }
        }

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


}