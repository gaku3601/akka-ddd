package bank.apiserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.{Config, ConfigFactory}
import pureconfig._
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContextExecutor

object Main extends App {
  val rootConfig: Config = ConfigFactory.load()
  implicit val system: ActorSystem = ActorSystem("bank-system", config = rootConfig)
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  //TODO: pureConfigの使い方を学習するか、別のものを利用することを検討する(import pureconfig.generic.auto._が嫌

  // fromConfigはpureconfigの機能
  // コンフィグをcase classにmappingしてくれるみたい
  val ApiServerConfig(host, port) = ConfigSource.fromConfig(system.settings.config.getConfig("bank.api-server")).loadOrThrow[ApiServerConfig]

  // 仮のroute そのうち消す
  val route =
    path("hello") {
      get {
        complete(HttpEntity("<h1>Say hello to akka-http</h1>"))
      }
    }
  // Httpはakka-http
  // 起動時に"ch.qos.logback" % "logback-classic" % "1.2.3"を入れておかないとエラー出るので入れておく
  val bindingFuture = Http().newServerAt(host, port).bind(route)
  sys.addShutdownHook {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
