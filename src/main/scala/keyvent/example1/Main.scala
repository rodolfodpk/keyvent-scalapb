package keyvent.example1

// import java.time.Instant
import keyvent.core._
import keyvent.domain1.CustomerEvent.TestOneof.{Activated, Created}
import keyvent.domain1._

object Main extends App {

  println("*** here")

  val cmd1: CreateCustomer = CreateCustomer(CommandId("cmd-1"), Some(EntityId("entity-1")), foo = Some("teste"))

  val event1 = CustomerEvent(Created(CustomerCreated(cmd1.entityId.get)))
  val event2 = CustomerEvent(Activated(CustomerActivated(cmd1.entityId.get)))
  val uow1 = CustomerUnitOfWork.apply(UnitOfWorkId("uow-1"), cmd1.commandId, cmd1.entityId.get, Version(1L),
                                       Seq(event1, event2))


  Validation.validation()

//  Json.json(cmd1, uow1)

//  Camel.start

}
