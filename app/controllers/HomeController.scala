package controllers

import javax.inject.{Inject, Singleton}
import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

import scala.concurrent.ExecutionContext

import models.MyTables._
import dao.TablesDAO
 

@Singleton
class HomeController @Inject()(tablesDAO: TablesDAO, cc: ControllerComponents)(implicit executionContext: ExecutionContext) 
                                                                                            extends AbstractController(cc) {
                                                                                            
  implicit val stateWrites  = Json.writes[State]
  implicit val taskDeskWrites  = Json.writes[TaskDesk]
  implicit val taskDeskStatesWrites  = Json.writes[TaskDeskStates]
  // список задач
  def getTaskDesk = Action.async { implicit request: Request[AnyContent] =>
    tablesDAO.getTaskDeskStates().map( tds => Ok(Json.toJson(tds)) )  
  }
  
  
  implicit val taskReader = Json.reads[Task]
  // создание задачи
  def createTask = Action.async(BodyParsers.parse.json[Task]){ implicit request =>
    val task: Task = request.body
    tablesDAO.createTask(task).map{ _ =>
      Ok(Json.obj("success" -> true))
    }
  }
  
  def updateTask(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.obj("success" -> true))
  }
  
  def updateTaskState(task_id: Int, state_id: Int) = Action { implicit request: Request[AnyContent] =>
    println(s"task id: $task_id \n state id: $state_id")
    Ok(Json.obj("success" -> true))
  }
  
}
