package dao


import javax.inject.Inject
import scala.concurrent.{ ExecutionContext, Future }

import play.db.NamedDatabase
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.driver.PostgresDriver.api._

import models.MyTables._

class TablesDAO @Inject()(@NamedDatabase("postgres") protected val dbConfigProvider: DatabaseConfigProvider)
						(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  
  def changeTaskState(id: Int, state_id: Int) = db.run{
    (for { 
      t <- tasks if t.id === id 
      } yield t.stateID
    ).update(state_id)
  }
  
  def updateTask(id: Int, title: String, descr: String) = db.run{
    (for { 
      t <- tasks if t.id === id 
      } yield (t.title, t.descr)
    ).update(title, descr)
  }
    
  def createTask(task: Task): Future[Unit] = db.run(tasks += task).map{_ => ()}
  
  def getTaskDeskStates(): Future[TaskDeskStates] = for {
    d <- getTaskDesk()
    s  <- allStates()
  } yield (TaskDeskStates(d, s))
  
  def allStates(): Future[Seq[State]] = db.run(states.result)
  
  def getTaskDesk(): Future[Seq[TaskDesk]] = for {
    s <- db.run(queryTaskDesk.result)
  } yield (s map TaskDesk.tupled)
  
  val queryTaskDesk = 
      for {
        t <- tasks
        s <- t.state
      } yield (t.id, t.title, t.stateID, s.stateName)
      
 //.transactionally     
  
}