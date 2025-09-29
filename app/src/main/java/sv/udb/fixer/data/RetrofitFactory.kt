package sv.udb.fixer.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sv.udb.fixer.BuildConfig
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    fun fixerService(): FixerService {
        // Interceptor que añade ?access_key=... a cada request
        val keyAppender = okhttp3.Interceptor { chain ->
            val original = chain.request()
            val newUrl = original.url.newBuilder()
                .addQueryParameter("access_key", BuildConfig.FIXER_API_KEY)
                .build()
            val newReq = original.newBuilder().url(newUrl).build()
            if (BuildConfig.FIXER_API_KEY.isBlank()) {
                android.util.Log.w("Fixer", "API KEY vacía. Revisa local.properties/gradle.properties/ENV.")
            }
            chain.proceed(newReq)
        }

        // Logs HTTP BODY solo en debug
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.LOG_HTTP) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(keyAppender)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.FIXER_BASE_URL) // http://... (FREE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FixerService::class.java)
    }
}
