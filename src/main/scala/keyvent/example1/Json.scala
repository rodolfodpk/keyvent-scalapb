package keyvent.example1

import com.trueaccord.scalapb.json.JsonFormat
import keyvent.core.{Command, CommandId, EntityId}
import keyvent.domain1.{ActivateCustomer, CustomerUnitOfWork, CreateCustomer}

/**
  * Created by rodolfo on 23/10/16.
  */
object Json {


  def json(cmd1: CreateCustomer, uow1: CustomerUnitOfWork) {

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
}
