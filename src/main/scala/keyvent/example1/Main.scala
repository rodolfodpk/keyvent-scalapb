package keyvent.example1

// import java.time.Instant
import com.trueaccord.scalapb.json.JsonFormat
import keyvent.core._
import keyvent.example1.CompanyEvent.TestOneof._

object Main extends App {

  println("*** here")

  val cmd1: CreateCustomer = CreateCustomer(CommandId("cmd-1"), Some(EntityId("entity-1")), foo = Some("teste"))

  val event1 = CompanyEvent(Created(CustomerCreated(cmd1.entityId.get)))
  val event2 = CompanyEvent(Activated(CustomerActivated(cmd1.entityId.get)))
  val uow1 = CompanyUnitOfWork.apply(UnitOfWorkId("uow-1"), cmd1.commandId, cmd1.entityId.get, Version(1L),
                                       Seq(event1, event2))

  // printing json
  val cmd_as_json: String = JsonFormat.toJsonString(cmd1)
  println(s"cmd           : $cmd_as_json")

  val uow_as_json: String = JsonFormat.toJsonString(uow1)
  println(s"uow           : $uow_as_json")

  // to print polymorphic collections as json
  import org.json4s._
  import org.json4s.jackson.Serialization
  import org.json4s.jackson.Serialization.write

  implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[CreateCustomer], classOf[ActivateCustomer])))

  val cmd2: ActivateCustomer = ActivateCustomer(CommandId("cmd-2"), EntityId("entity-1"))
  val cmdList: List[Command] = List(cmd1, cmd2)
  val json4: String = write(cmdList)
  println(s"list of cmds   : $json4")

}
