package adapters

import play.api.libs.ws._
import play.api.libs.json._
import play.api._
import play.api.libs.functional.syntax._
import models.{Song}
import providers.{Vagalume}

class MusicAPIAdapter(val config: Configuration, val ws: WSClient, val providerName: String) {
    val provider = MusicAPIAdapter.providerMap(providerName)(config, ws) // returns an
    // instance of the correct provider

    def listSongs(query: String): List[Song] = provider.listSongs(query)
}

object MusicAPIAdapter {
    // não sabe o que Vagalume é ... aí fica difícil instanciar
    // talvez o que precise aqui seja reflection
    // quem sabe em vez de um mapa um match ou um ponteiro pra
    // alguma coisa que consiga instanciar ... um construtor, quem sabe, sei lá
    // por enquanto não sei fazer isso
    val providerMap = Map("vagalume" -> Vagalume)
}


