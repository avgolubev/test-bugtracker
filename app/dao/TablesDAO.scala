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
    
  def allTasksState(): Future[Seq[TasksState]] = for { 
    ss <- allStates()
    ts <- allTasks()      
  } yield {
    ss map{s => TasksState(s, ts.filter(t => t.stateID.get == s.id))} 
  }
  
  private def allStates(): Future[Seq[State]] = db.run(states.sortBy(_.id).result)
  
  private def allTasks(): Future[Seq[Task]] = db.run(tasks.sortBy(_.id).result)  
  
}