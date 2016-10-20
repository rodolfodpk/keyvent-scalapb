package keyvent.core

trait Command {
  def commandId: CommandId
  def entityId: Option[EntityId]
}

trait Event {}

case class UnitOfWork(uowId: UnitOfWorkId,
                      commandId: CommandId,
                      entityId: EntityId,
                      version: Version,
                      instant: Long,
                      events: List[Event]
                     )
