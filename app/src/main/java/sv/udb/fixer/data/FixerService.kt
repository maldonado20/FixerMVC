package sv.udb.fixer.data

import retrofit2.http.GET
import retrofit2.http.Query
import sv.udb.fixer.model.RatesResponse

interface FixerService {
    @GET("latest")
    fun getLatest(
        @Query("symbols") symbols: String? = null,
        @Query("base") base: String? = null // Solo aplica en planes que lo permitan
    ): retrofit2.Call<RatesResponse>
}
