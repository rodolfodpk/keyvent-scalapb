package keyvent.domain1

import io.underscore.validation._
import keyvent.core.CommandId

object CustomerFunctions {

  implicit val commandIdValidator: Validator[CommandId] =
    validate[CommandId].
      field(_.value)(warn(matchesRegex("123".r, "tem que comecar com cmd-id-xxx"))).
      field(_.value)(nonEmpty) // just a demo since this is already enforced by protobuf generated code

  implicit val personValidator: Validator[CreateCustomer] =
    validate[CreateCustomer].
      field(_.commandId).
      field(_.foo)(eql(Some("a"), "tem  que ser =a"))

  def isValid(cmd: CreateCustomer) = {
      cmd.validate
  }

}
