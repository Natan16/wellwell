package providers

trait MusicAPIProvider {
    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val config: Configuration
    val ws: WSClient
    protected val BaseUrl: String
    def listSongs(query: String): List[Song]
}
