package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.ws._
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class WellnessController @Inject()(ws: WSClient, val controllerComponents: ControllerComponents) extends BaseController {

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

  def search(term: String) = Action { implicit request: Request[AnyContent] =>
    val API_KEY = sys.env("VAGALUME_API_KEY")
    val BASE_URL = "https://api.vagalume.com.br/"
    val url = s"${BASE_URL}search.excerpt"
    val request : WSRequest = ws.url(url).withQueryString(
      "q" -> term, "limit" -> "5"
    ).withHeaders("Authorization" -> ("Bearer " + API_KEY))
    val futureResponse : Future[WSResponse] = request.get()
    // now parse the response and render the page
    // https://api.vagalume.com.br/search.excerpt?q=vamos%20fugir&limit=5
    
    // calls API
    // renders page with response
    // is there any way to do this asynchronously and
    // without re-rendering the whole page?
    Ok(views.html.index()) // retornar um json que o twirl possa usar pra mostrar o resultado bonitinho
  }
}

// responseContém todas informações das músicas no Vagalume:
// numFoundTotal de registros encontrados
// startInício da busca (campo inalterável)
// docsListagem dos registros encontrados
// idID da música encontrada
// urlA URL da música no Vagalume
// titleO título da música encontrada
// bandNome da banda
// highlightingInformações sobre trechos encontrados.
// l3ade68b7g3be34ea3O 1º caractere "l" refere-se á palavra "letra" e o restante ao ID da música
// title/letraO texto da letra ou titulo da música, será destacado por "<em>...trecho...<em>".
