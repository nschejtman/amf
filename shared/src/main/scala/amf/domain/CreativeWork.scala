package amf.domain

import amf.metadata.domain.CreativeWorkModel.{Description, Url}
import org.yaml.model.YMap

/**
  * Creative work internal model
  */
case class CreativeWork(fields: Fields, annotations: Annotations) extends DomainElement {

  def url: String         = fields(Url)
  def description: String = fields(Description)

  def withUrl(url: String): this.type                 = set(Url, url)
  def withDescription(description: String): this.type = set(Description, description)

  override def adopted(parent: String): this.type = withId(parent + "/creative-work")
}

object CreativeWork {

  def apply(): CreativeWork = apply(Annotations())

  def apply(ast: YMap): CreativeWork = apply(Annotations(ast))

  def apply(annotations: Annotations): CreativeWork = apply(Fields(), annotations)
}
