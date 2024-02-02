package adapters

import play.api.libs.ws._


case class Song(url: String)

// vagalume (feature flag to choose between providers)
class MusicAPIAdapter(ws: WSClient, val providerName: String) {
    // the controller is responsible to provide the client
    val provider = providerMap(providerName)

    def listSongs(query: String): List[Song] = provider.listSongs(query)
    
    // companion object must have map provider name to corresponding provider
    // this is to be inherited for the implementations
    // abstract class or trait, whatever
}

object MusicAPIAdapter {
    val providerMap = Map("vagalume" -> Vagalume)
    // enum with valid names???
}

trait MusicAPIProvider {
    // every provider should define a reader and a writer?!
    val ws: WSClient
    protected val BaseUrl: String // maybe there should be some validation
    def listSongs(query: String): List[Song]
    // every response has a different formatting
    // I should think about a Class to map from the JSResponse
    // and now there will be a method helping me with serialization
    // ok, this seems doable
    // List of objects that just wrap the url (for now). Use fucking implicit conversions
    // def formatHtml()
    // block of code that will run asynchronouly
    // def 
}

// maybe this being an object is enough ( yes, objects can extend traits )
// this must be a singleton ... does the adapter have to be a singleton too?
object Vagalume extends MusicAPIProvider {
    implicit readerSong: Reader[Song] = 1
    // both of these should be required (present in trait or in constructor)   
    // Imma need an auth abstraction better than this
    private val ApiKey = config.get[String]("VAGALUME_API_KEY")
    override val BaseUrl = "https://api.vagalume.com.br/"

    override def listSongs(query: String): Future[WSResponse] = {
        val endpoint = "search.excerpt"
        val url = s"${BaseUrl}${endpoint}"
        ws.url(url).withQueryString(
            "q" -> query, "limit" -> "100"    
        ).withHeaders("Authorization" -> ("Bearer " + ApiKey)).get()
        // maybe should return WSResponse
        
        // the adapter should be capable of formatting whatever is thrown to it
    }

    // val futureResponse : Future[WSResponse] = request.get()
}

//   this looks like serialization, that would be a serializer for a certain
// provider's response
  private def vagalume_response_formatter(response: WSResponse): Html = {
    val links = for (
      doc <- response.json("response")("docs").as[List[JsValue]];
      url = "https://www.vagalume.com" + doc("url").as[String]
    )
      yield (s"<a href=${url}>" + url + " </a>")
    // the html part is not the provider's responsibility, because it counts as serialization
    Html(links.mkString(" <br/>"))
  }
