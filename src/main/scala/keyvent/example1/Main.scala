package keyvent.example1

// import java.time.Instant
import com.trueaccord.scalapb.json.JsonFormat

import keyvent.core._

object Main extends App {

  println("*** here")

  val cmd1: CreateCustomer = CreateCustomer(CommandId("cmd-1"), Some(EntityId("entity-1")), foo = Some("teste"))
  val events1: List[Event] = List(CustomerCreated(cmd1.entityId.get), CustomerActivated(cmd1.entityId.get))
  val uow1: UnitOfWork = UnitOfWork(UnitOfWorkId("uow-1"), cmd1.commandId, cmd1.entityId.get, Version(1L),
    5L,  events1)

  val json1: String = JsonFormat.toJsonString(cmd1)
  println(s"cmd1           : $json1")

  import org.json4s._
  import org.json4s.jackson.Serialization
  import org.json4s.jackson.Serialization.{read, write}

  implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[CreateCustomer], classOf[ActivateCustomer],
                        classOf[CustomerCreated], classOf[CustomerActivated])))

  val json2: String = write(uow1)
  println(s"uow            : $json2")

  val json3: String = write(events1)
  println(s"list of events : $json3")

  val cmd2: ActivateCustomer = ActivateCustomer(CommandId("cmd-2"), Some(EntityId("entity-1")))
  val cmdList: List[Command] = List(cmd1, cmd2)
  val json4: String = write(cmdList)
  println(s"list of cmds   : $json4")

}
