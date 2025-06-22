interface Cloneable {
    fun clone(): Any
}

interface HTTPGet {
    fun getResponse(): Response
}

class GenericRequest(
    var url: String,
    var params: Map<String, String?>
) : Cloneable {

    override fun clone(): GenericRequest {
        return GenericRequest(url, params.toMap())
    }
}

class Response(val content: String, val statusCode: Int) {
    override fun toString(): String {
        return "Response(statusCode=$statusCode, content=$content)"
    }
}

class GetRequest(
    url: String,
    params: Map<String, String?>,
    var timeout: Int
) {
    var genericReq: GenericRequest = GenericRequest(url, params)

    fun getResponse(): Response {
        println("Executing GET request to ${genericReq.url} with timeout: $timeout")
        return Response("Content from ${genericReq.url}", 200)
    }
}

class PostRequest(
    url: String,
    params: Map<String, String?>
) {
    var genericReq: GenericRequest = GenericRequest(url, params)

    fun postData(): Response {
        println("Posting data to ${genericReq.url}")
        return Response("Post response from ${genericReq.url}", 201)
    }
}

class CleanGetRequest(
    val getRequest: GetRequest,
    private val parentalControlDisallow: List<String>
) : HTTPGet {

    override fun getResponse(): Response {
        val urlToCheck = getRequest.genericReq.url.lowercase()

        for (disallowedPattern in parentalControlDisallow) {
            if (urlToCheck.contains(disallowedPattern.lowercase())) {
                println("Access to ${getRequest.genericReq.url} blocked by parental control")
                return Response("Access blocked by parental control", 403)
            }
        }

        return getRequest.getResponse()
    }
}

class KidsBrowser(
    private val cleanGet: CleanGetRequest,
    private val postReq: PostRequest?
) {

    fun start() {
        println("Kids browser started with parental controls enabled")
    }

    fun browseUrl(url: String): Response {
        println("Child is browsing to: $url")
        cleanGet.getRequest.genericReq.url = url
        return cleanGet.getResponse()
    }

    fun submitForm(url: String, formData: Map<String, String?>): Response {
        if (postReq == null) {
            return Response("POST requests not enabled", 405)
        }
        println("Submitting form to: $url")
        postReq.genericReq.url = url
        postReq.genericReq.params = formData
        return postReq.postData()
    }
}

fun main() {
    val baseGetRequest = GetRequest(
        url = "https://example.com",
        params = mapOf("lang" to "ro"),
        timeout = 5000
    )

    val postRequest = PostRequest(
        url = "https://example.com/submit",
        params = mapOf("name" to "Alex", "age" to "10")
    )

    val parentalControls = listOf(
        "adult", "gambling", "violence", "social", "game", "chat"
    )

    val cleanGetRequest = CleanGetRequest(baseGetRequest, parentalControls)

    val kidsBrowser = KidsBrowser(cleanGetRequest, postRequest)
    kidsBrowser.start()

    println("\n--- Testing safe browsing ---")
    val response1 = kidsBrowser.browseUrl("https://kids.education.com/math")
    println("Response: $response1")

    println("\n--- Testing blocked browsing ---")
    val response2 = kidsBrowser.browseUrl("https://socialmedia.com/feed")
    println("Response: $response2")

    println("\n--- Testing form submission ---")
    val formResponse = kidsBrowser.submitForm(
        "https://kids.education.com/submit-quiz",
        mapOf("q1" to "answer1", "q2" to "answer2")
    )
    println("Form submission response: $formResponse")
}