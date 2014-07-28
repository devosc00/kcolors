package controllers

import akka.actor.{Actor, Props, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import play.api.libs.concurrent.Akka
import play.api.libs.iteratee._
import play.api.libs.json.{JsObject, JsString, JsValue}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

import scala.concurrent.duration._


object ConsLogger {

  def apply(logger: ActorRef) {

    // Create an Iteratee that logs all messages to the console.
    val loggerIteratee = Iteratee.foreach[JsValue](event => println(event.toString))

    implicit val timeout = Timeout(1 second)
    // Make the robot join the room
    logger ? (Join(123)) map {
      case Connected(robotChannel) =>
        // Apply this Enumerator on the logger.
        robotChannel |>> loggerIteratee
    }

  }

}


object MessageHandler {
  implicit val timeout = Timeout(1 second)

  lazy val default = {
    val logActor = Akka.system.actorOf(Props[MessageHandler])

    ConsLogger(logActor)

    logActor
  }

  def join(username:Int):scala.concurrent.Future[(Iteratee[JsValue,_],Enumerator[JsValue])] = {

    (default ? Join(username)).map {
      case Connected(enumerator) =>
        val iteratee = Iteratee.foreach[JsValue] { event =>
          default ! Event(username, event)}
          .mapDone { _ =>
          default ! Quit(username)
        }
        (iteratee,enumerator)

      case CannotConnect(error) =>

        // Connection error

        // A finished Iteratee sending EOF
        val iteratee = Done[JsValue,Unit]((),Input.EOF)

        // Send an error and close the socket
        val enumerator =  Enumerator[JsValue](JsObject(Seq("error" -> JsString(error))))
          .andThen(Enumerator.enumInput(Input.EOF))


        (iteratee,enumerator)
    }
  }
}


class MessageHandler extends Actor {


   var members = Map.empty[Int, Concurrent.Channel[JsValue]]

  override def receive: Receive = {
    case Join(username) => {

    }

    case Event(username, event) => {
      def getCommand(command: String) = (event \ command).as[String]

      getCommand("command") match {
        case ("green") => println("green")
      }
    }
  }
}

sealed trait Messages

  case class Join(username: Int)                             extends Messages
  case class Event(username: Int, event: JsValue)            extends Messages
  case class Quit(username: Int)                             extends Messages
  case class Connected	(enumerator:Enumerator[JsValue])	   		extends Messages
  case class CannotConnect(message: String)                     extends Messages


