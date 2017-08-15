package amf.domain

import amf.common.AMFAST
import amf.metadata.domain.CreativeWorkModel.{Description, Url}

/**
  * Creative work internal model
  */
case class CreativeWork(fields: Fields, annotations: Annotations) extends DomainElement {

  val url: String         = fields(Url)
  val description: String = fields(Description)

  def withUrl(url: String): this.type                 = set(Url, url)
  def withDescription(description: String): this.type = set(Description, description)
}

object CreativeWork {
  def apply(ast: AMFAST): CreativeWork = apply(Fields(), Annotations(ast))

  def apply(fields: Fields = Fields(), annotations: Annotations = new Annotations()): CreativeWork =
    new CreativeWork(fields, annotations)
}
