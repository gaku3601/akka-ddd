import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route

class RestApi extends Routes

trait Routes {
  def routes: Route = route

  val route =
    path("hello") {
      get {
        complete(HttpEntity("<h1>Say hello to akka-http</h1>"))
      }
    }
}