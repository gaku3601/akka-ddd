import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives.{complete, get, _}
import akka.http.scaladsl.server.Route
import domain.Counter

class RestApi(system: ActorSystem) extends Routes {
  val actor = system.actorOf(Props[Counter], Counter.ACTOR_NAME)
}

trait Routes extends CounterApi {
  def routes: Route = countUpRoute ~ countDownRoute

  val countUpRoute: Route =
    pathPrefix("countup") {
      pathEndOrSingleSlash {
        get {
          countUp
          complete(HttpEntity("<h1>Say hello to akka-http</h1>"))
        }
      }
    }
  val countDownRoute: Route =
    pathPrefix("countdown") {
      pathEndOrSingleSlash {
        get {
          countDown
          complete(HttpEntity("<h1>Say hello to akka-http</h1>"))
        }
      }
    }
}

trait CounterApi {

  import Counter._

  def actor(): ActorRef

  lazy val counter = actor()

  def countUp = counter ! CountUp

  def countDown = counter ! CountDown
}