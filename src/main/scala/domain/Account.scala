package domain

import akka.actor.{Actor, ActorLogging}

class Account extends Actor with ActorLogging {

  import Account._

  // state
  var name = ""
  var age = 0

  override def receive: Receive = {
    case CreateAccount(name, age) => {
      this.name = name
      this.age = age
      log.info(s"Created Account: name[${this.name}] age[${this.age}]")
    }
    case _ => log.info(s"received unknown message.")
  }
}

object Account {

  case class CreateAccount(name: String, age: Int)

}
