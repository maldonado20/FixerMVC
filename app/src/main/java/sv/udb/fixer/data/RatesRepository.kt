package sv.udb.fixer.data


import sv.udb.fixer.model.Rate

class RatesRepository(private val api: FixerService) {

    fun fetchRates(
        symbols: List<String>? = null,
        base: String? = null,
        onResult: (Result<List<sv.udb.fixer.model.Rate>>) -> Unit
    ) {
        val symbolsJoined = symbols?.joinToString(",")
        api.getLatest(symbolsJoined, base).enqueue(object : retrofit2.Callback<sv.udb.fixer.model.RatesResponse> {
            override fun onResponse(
                call: retrofit2.Call<sv.udb.fixer.model.RatesResponse>,
                response: retrofit2.Response<sv.udb.fixer.model.RatesResponse>
            ) {
                if (!response.isSuccessful) {
                    onResult(Result.failure(Exception("HTTP ${response.code()}")))
                    return
                }
                val body = response.body()
                if (body?.success == true && !body.rates.isNullOrEmpty()) {
                    val list = body.rates!!.entries
                        .map { sv.udb.fixer.model.Rate(it.key, it.value) }
                        .sortedBy { it.code }
                    onResult(Result.success(list))
                } else {
                    val msg = body?.error?.info ?: "Respuesta inválida"
                    onResult(Result.failure(Exception(msg)))
                }
            }

            override fun onFailure(
                call: retrofit2.Call<sv.udb.fixer.model.RatesResponse>,
                t: Throwable
            ) {
                onResult(Result.failure(t))
            }
        })
    }
}

