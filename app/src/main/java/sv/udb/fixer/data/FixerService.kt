package sv.udb.fixer.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import sv.udb.fixer.model.RatesResponse

interface FixerService {
    @GET("latest")
    fun getLatest(
        @Query("symbols") symbols: String? = null,
        @Query("base") base: String? = null
    ): Call<RatesResponse>
}
