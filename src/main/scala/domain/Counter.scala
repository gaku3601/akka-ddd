package domain

import akka.actor.{Actor, ActorLogging}

class Counter extends Actor with ActorLogging {

  import Counter._

  // state
  var count = 0

  override def receive: Receive = {
    case CountUp => {
      count = count + 1
      log.info(s"this count: $count")
    }
    case CountDown => {
      count = count - 1
      log.info(s"this count: $count")
    }
    case _ => log.info(s"received unknown message.")
  }
}

object Counter {
  val ACTOR_NAME = "counter"

  case class CountUp()

  case class CountDown()

}
