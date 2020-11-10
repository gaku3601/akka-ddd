import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor

object Main extends App {
  val rootConfig: Config = ConfigFactory.load()
  implicit val system: ActorSystem = ActorSystem("root-system", config = rootConfig)
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val config = ConfigFactory.load()
  val host = config.getString("http.host") // 設定からホスト名とポートを取得
  val port = config.getInt("http.port")
  // Httpはakka-http
  // 起動時に"ch.qos.logback" % "logback-classic" % "1.2.3"を入れておかないとエラー出るので入れておく
  val api = new RestApi(system).routes
  val bindingFuture = Http().newServerAt(host, port).bind(api)
  sys.addShutdownHook {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
