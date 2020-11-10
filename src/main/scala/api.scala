import java.util.UUID.randomUUID

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives.{complete, get, _}
import akka.http.scaladsl.server.Route
import domain.{Account, Counter}

class RestApi(sys: ActorSystem) extends CounterRoutes with AccountRoutes {
  val system = sys

  def routes: Route = countUpRoute ~ countDownRoute ~ accountRoute
}

trait CounterRoutes extends CounterApi {
  val system: ActorSystem
  lazy val counterActor = system.actorOf(Props[Counter], Counter.ACTOR_NAME)
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

trait AccountRoutes extends AccountApi {
  val system: ActorSystem
  lazy val accountActor = system.actorOf(Props[Account], randomUUID().toString)

  val accountRoute: Route =
    pathPrefix("account") {
      pathEndOrSingleSlash {
        get {
          createAccount("testname", 20)
          complete(HttpEntity("<h1>Say hello to akka-http</h1>"))
        }
      }
    }
}

trait CounterApi {

  import Counter._

  def counterActor(): ActorRef

  lazy val counter = counterActor()

  def countUp = counter ! CountUp

  def countDown = counter ! CountDown
}

trait AccountApi {

  import domain.Account._

  def accountActor(): ActorRef

  lazy val account = accountActor()

  def createAccount(name: String, age: Int) = account ! CreateAccount(name, age)
}