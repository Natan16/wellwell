package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.Future
import play.twirl.api.Html

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class WellnessController @Inject()(config: Configuration, ws: WSClient, val controllerComponents: ControllerComponents) extends BaseController {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def search(term: String) = Action.async { 
    implicit request: Request[AnyContent] =>
    val API_KEY = config.get[String]("VAGALUME_API_KEY")
    val BASE_URL = "https://api.vagalume.com.br/"
    val url = s"${BASE_URL}search.excerpt"
    val request : WSRequest = ws.url(url).withQueryString(
      "q" -> term, "limit" -> "100"
    ).withHeaders("Authorization" -> ("Bearer " + API_KEY))
    val futureResponse : Future[WSResponse] = request.get() 
    futureResponse.map(response => Ok(
      views.html.search(vagalume_response_formatter(response)))
    )
  }

  private def vagalume_response_formatter(response: WSResponse): Html = {
    val links = for (doc <- response.json("response")("docs").as[List[JsValue]])
      yield ("<a href=\"url\"> www.vagalume.com" + doc("url").as[String] + " </a>")
    Html(links.mkString(" <br/>"))
  }
}