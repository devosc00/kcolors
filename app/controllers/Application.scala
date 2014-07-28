package controllers

import play.api._
import play.api.libs.json.JsValue
import play.api.mvc._

object Application extends Controller {

  def index(username: Int) = WebSocket.async[JsValue] { implicit request =>
   MessageHandler.join(username)
  }

}