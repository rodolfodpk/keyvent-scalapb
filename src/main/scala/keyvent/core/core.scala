package keyvent.core

trait Command {
  def commandId: CommandId
//  def entityId: Option[EntityId]
}

trait Event {}

trait UnitOfWork {
//  def uowId: UnitOfWorkId
//  def commandId: CommandId
//  def entityId: EntityId
//  def version: Version
  // def instant: Long
}
