package sv.udb.fixer.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import sv.udb.fixer.model.RatesResponse
import sv.udb.fixer.model.SymbolsResponse

interface FixerService {

    @GET("latest")
    fun getLatest(
        @Query("symbols") symbols: String? = null,
        @Query("base") base: String? = null
    ): Call<RatesResponse>

    @GET("symbols")
    fun getSymbols(): Call<SymbolsResponse>

    // Fluctuación (puede requerir plan pago; lo usamos con manejo de error)
    @GET("fluctuation")
    fun getFluctuation(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("symbols") symbols: String? = null
    ): Call<sv.udb.fixer.model.FluctuationResponse>
}
