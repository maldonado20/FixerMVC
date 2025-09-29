package sv.udb.fixer.model

class RatesResponse (
    val success: Boolean? = null,
    val base: String? = null,
    val date: String? = null,
    val rates: Map<String, Double>? = null,
    val error: ErrorInfo? = null
)