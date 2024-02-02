package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.Future
// import play.twirl.api.Html

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class WellnessController @Inject()(config: Configuration, ws: WSClient, val controllerComponents: ControllerComponents) extends BaseController {
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

  def search(term: String, provider: String = "vagalume") = Action.async { 
    implicit request: Request[AnyContent] =>
    val songs = MusicAPIAdapter(config, ws, provider).listSongs(term)
    // ainda tem umas paradas mto sketchy com future que depende de um entendimento
    // mais aprofundado do que rola por deibaixo dos panos
    Ok(views.html.search(songs))
  }
}