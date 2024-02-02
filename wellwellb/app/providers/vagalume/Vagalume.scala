package providers
import models._

final class Vagalume(val config: Configuration, val ws: WSClient) extends MusicAPIProvider {
    implicit val songReads: Reads[Song] = (
        (JsPath \ "url").read[String] and (JsPath \ "name").read[String]  
    )(Song.apply _) // ("https://www.vagalume.com/" +
    override val BaseUrl = "https://api.vagalume.com.br/"
    private val ApiKey = config.get[String]("VAGALUME_API_KEY")

    override def listSongs(ws: WSClient, query: String): List[Song] = {
        val endpoint = "search.excerpt"
        val url = s"${BaseUrl}${endpoint}"
        val futureResponse = ws.url(url).withQueryString(
            "q" -> query, "limit" -> "100"    
        ).withHeaders("Authorization" -> ("Bearer " + ApiKey)).get()
        futureResponse.map(response => (
            response.json \ "response" \ "docs").as[List[Song]]
        )
    }
}