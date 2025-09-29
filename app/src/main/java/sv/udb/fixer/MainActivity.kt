package sv.udb.fixer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import sv.udb.fixer.data.RatesRepository
import sv.udb.fixer.data.RetrofitFactory
import sv.udb.fixer.databinding.ActivityMainBinding
import sv.udb.fixer.model.Rate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = sv.udb.fixer.ui.RatesAdapter()
    private val repository by lazy {
        RatesRepository(RetrofitFactory.fixerService())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        setupSwipeRefresh()

        // Carga inicial
        loadRates()
    }

    private fun setupRecycler() {
        binding.rvRates.layoutManager = LinearLayoutManager(this)
        binding.rvRates.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            loadRates()
        }
    }

    private fun setStateLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.rvRates.visibility = View.GONE
        binding.tvError.visibility = View.GONE
    }

    private fun setStateError(msg: String) {
        binding.progress.visibility = View.GONE
        binding.rvRates.visibility = View.GONE
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = msg
    }

    private fun setStateData(list: List<Rate>) {
        binding.progress.visibility = View.GONE
        binding.tvError.visibility = View.GONE
        binding.rvRates.visibility = View.VISIBLE
        adapter.submit(list)
    }

    private fun loadRates() {
        setStateLoading()
        val symbols = listOf("USD", "EUR", "GBP", "JPY", "CAD", "MXN")
        repository.fetchRates(symbols = symbols, base = null) { result ->
            runOnUiThread {
                binding.swipeRefresh.isRefreshing = false
                result.onSuccess { setStateData(it) }
                    .onFailure { setStateError(it.message ?: "Error desconocido") }
            }
        }
    }
}