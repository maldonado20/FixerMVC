package sv.udb.fixer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import sv.udb.fixer.databinding.ActivityMainBinding
import sv.udb.fixer.ui.ConvertFragment
import sv.udb.fixer.ui.RatesFragment
import sv.udb.fixer.ui.SymbolsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pantalla por defecto
        replace(RatesFragment())

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_rates -> replace(RatesFragment())
                R.id.nav_symbols -> replace(SymbolsFragment())
                R.id.nav_convert -> replace(ConvertFragment())
                // R.id.nav_fluct -> replace(FluctuationsFragment())
            }
            true
        }
    }

    private fun replace(f: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, f)
            .commit()
    }
}
