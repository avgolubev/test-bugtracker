package models

import slick.driver.PostgresDriver.api._

object MyTables {
  
  case class Task(id: Int, title: String, descr: String, stateID: Int)
  
  class Tasks(tag: Tag) extends Table[Task](tag, "TASKS") {
    def id = column[Int]("TASK_ID", O.PrimaryKey, O.AutoInc)
    def title = column[String]("TITLE")
    def descr = column[String]("DESCRIPTION")
    def stateID = column[Int]("STATE_ID")
    
    def state = foreignKey("STATE_FK", stateID, states)(_.id)
    
    def * = (id, title, descr, stateID) <> (Task.tupled, Task.unapply)
  }
  
  
  case class State(id: Int, stateName: String)
  
  class States(tag: Tag) extends Table[State](tag, "STATES") {
    def id = column[Int]("STATE_ID", O.PrimaryKey)
    def stateName = column[String]("STATE_NAME")
  
    def * = (id, stateName) <> (State.tupled, State.unapply)
  }
  
  val tasks = TableQuery[Tasks]
  val states = TableQuery[States]     
  
  case class TaskDesk(id: Int, title: String, stateID: Int, stateName: String)
  case class TaskDeskStates(desk: Seq[TaskDesk], states: Seq[State])

}