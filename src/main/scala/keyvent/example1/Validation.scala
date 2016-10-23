package keyvent.example1

import io.underscore.validation._
import keyvent.core.{CommandId, EntityId}
import keyvent.domain1.CustomerFunctions._
import keyvent.domain1.{CreateCustomer, CustomerFunctions}

/**
  * Created by rodolfo on 23/10/16.
  */
object Validation {

  def validation() = {

    println("*** validation")

    val invalidCmd = CreateCustomer(CommandId("cmd-1"), Some(EntityId("entity-1")))
    CustomerFunctions.isValid(invalidCmd)
    println(invalidCmd.validate)

    val validCmd = CreateCustomer(CommandId("cmd-1"), Some(EntityId("entity-1")), foo = Some("teste"))
    val result = validCmd.validate
    println(result)

  }

}
