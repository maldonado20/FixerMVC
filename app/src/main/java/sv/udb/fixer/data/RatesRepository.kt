package sv.udb.fixer.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.udb.fixer.model.Rate
import sv.udb.fixer.model.RatesResponse

class RatesRepository(private val api: FixerService) {

    fun fetchRates(
        symbols: List<String>? = null,
        base: String? = null,
        onResult: (Result<List<Rate>>) -> Unit
    ) {
        val symbolsJoined = symbols?.joinToString(",")
        api.getLatest(symbolsJoined, base).enqueue(object : Callback<RatesResponse> {
            override fun onResponse(call: Call<RatesResponse>, resp: Response<RatesResponse>) {
                if (!resp.isSuccessful) {
                    onResult(Result.failure(Exception("HTTP ${resp.code()}")))
                    return
                }
                val body = resp.body()
                if (body?.success == true && !body.rates.isNullOrEmpty()) {
                    val list = body.rates!!.entries
                        .map { Rate(it.key, it.value) }
                        .sortedBy { it.code }
                    onResult(Result.success(list))
                } else {
                    val msg = body?.error?.info ?: "Respuesta inválida"
                    onResult(Result.failure(Exception(msg)))
                }
            }

            override fun onFailure(call: Call<RatesResponse>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }
}
