package controllers

import javax.inject.{Inject, Singleton}
import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.concurrent.ExecutionContext

import models.MyTables.Task
import dao.TablesDAO
 

@Singleton
class HomeController @Inject()(tablesDAO: TablesDAO, cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  implicit val taskReader = Json.reads[models.MyTables.Task]
  
  def createTask = Action.async(BodyParsers.parse.json[models.MyTables.Task]){ implicit request =>
    val task = request.body
    tablesDAO.createTask(task).map{ _ =>
      Ok(Json.obj("success" -> true))
    }
  }
  
  def updateTask(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  def updateTaskState(task_id: Int, state_id: Int) = Action { implicit request: Request[AnyContent] =>
    println(s"task id: $task_id \n state id: $state_id")
    Ok(views.html.index())
  }
  
}
