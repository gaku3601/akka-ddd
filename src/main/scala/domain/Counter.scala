package domain

import akka.actor.{Actor, ActorLogging}

class Counter extends Actor with ActorLogging {
  // state
  var count = 0

  override def receive: Receive = {
    case "test" => {
      count = count + 1
      log.info(s"received test.$count")
    }
    case _ => log.info(s"received unknown message.")
  }
}

object Counter {
  val ACTOR_NAME = "counter"
}
