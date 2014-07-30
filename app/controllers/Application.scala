package controllers

import play.api.Routes
import play.api.libs.json.JsValue
import play.api.mvc._

object Application extends Controller {

  def index = Action { implicit request => Ok (views.html.index())}


  def indexWS = WebSocket.async[JsValue] { implicit request =>
   MessageHandler.join()

  }


  def javascriptRoutes = Action {
    implicit request =>
      Ok(
        Routes.javascriptRouter("jsRoutes")(
        routes.javascript.Application.indexWS
        )
      ).as("text/javascript")
  }
}