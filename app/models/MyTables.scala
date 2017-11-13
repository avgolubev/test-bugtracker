package models

import slick.driver.PostgresDriver.api._
import slick.driver.PostgresDriver

object MyTables {
  
  case class Task(id: Option[Int] = None, title: String, descr: String, stateID: Option[Int] = None)
  
  class Tasks(tag: Tag) extends Table[Task](tag, "tasks") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def descr = column[String]("description")
    def stateID = column[Int]("state_id")
    
    def state = foreignKey("state_fk", stateID, states)(_.id)
    
    def * = (id.?, title, descr, stateID.?) <> (Task.tupled, Task.unapply)
  }
  
  
  case class State(id: Int, stateName: String)
  
  class States(tag: Tag) extends Table[State](tag, "states") {
    def id = column[Int]("id", O.PrimaryKey)
    def stateName = column[String]("name")
  
    def * = (id, stateName) <> (State.tupled, State.unapply)
  }
  
  val tasks = TableQuery[Tasks]
  val states = TableQuery[States]     
  
  case class TaskDesk(id: Int, title: String, stateID: Int, stateName: String)
  //case class TaskDeskStates(desk: Seq[TaskDesk], states: Seq[State])
  case class TasksState(state: State, tasks: Seq[Task])
//insert into tasks(title, description, state_id) values('второе', 'ещё одно', 1);  

}