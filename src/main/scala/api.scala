import java.util.UUID._

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives.{complete, get, _}
import akka.http.scaladsl.server.Route
import domain.{Account, Counter}

class RestApi(rootSystem: ActorSystem) extends CounterRoutes with AccountRoutes {

  def routes: Route = countUpRoute ~ countDownRoute ~ accountRoute

  override val system: ActorSystem = rootSystem
}

// コントローラー
trait CounterRoutes extends CounterApi {
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

// service
trait CounterApi {

  import Counter._

  val system: ActorSystem

  def countUp = {
    val counter = system.actorOf(Props[Counter], Counter.ACTOR_NAME) // 複数回叩くとactor name重複エラー(cluster shardingを使えば解決できるかも？)
    counter ! CountUp
  }

  def countDown = {
    val counter = system.actorOf(Props[Counter], Counter.ACTOR_NAME)
    counter ! CountDown
  }
}

trait AccountApi {

  import domain.Account._

  val system: ActorSystem

  def createAccount(name: String, age: Int) = {
    val account = system.actorOf(Props[Account], randomUUID().toString)
    account ! CreateAccount(name, age)
  }
}
