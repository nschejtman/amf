package amf.plugins.document.vocabularies.metamodel.domain

import amf.core.metamodel.Type.{Bool, Str}
import amf.core.metamodel.domain.{DomainElementModel, LinkableElementModel}
import amf.core.metamodel.{DynamicObj, Field}
import amf.core.model.domain.AmfObject
import amf.core.vocabulary.{Namespace, ValueType}
import amf.plugins.document.vocabularies.model.domain.NodeMappable

class DialectDomainElementModel(val typeIri: Seq[String] = Seq(),
                                val typeFields: Seq[Field] = Nil,
                                val nodeMapping: Option[NodeMappable] = None)
    extends DomainElementModel
    with DynamicObj
    with LinkableElementModel {


  val DeclarationName = Field(Str, Namespace.Meta + "declarationName")
  val Abstract        = Field(Bool, Namespace.Meta + "abstract")
  override def fields: List[Field] =
    Abstract :: DeclarationName :: DomainElementModel.fields ++ LinkableElementModel.fields ++ typeFields
  override val `type`: List[ValueType] = typeIri
    .map(iriToValue)
    .toList ++ ((Namespace.Meta + "DialectDomainElement") :: DomainElementModel.`type`)
  def iriToValue(iri: String) = ValueType(iri)

  override def modelInstance: AmfObject =
    throw new Exception("DialectDomainElement is an abstract class and it cannot be isntantiated directly")
}

object DialectDomainElementModel {

  val PluginNodeProperty: ValueType = Namespace.Meta + "pluginNode"
  val PluginNodeClass: ValueType = Namespace.Meta + "PluginNode"

  def apply(): DialectDomainElementModel = new DialectDomainElementModel()
  def apply(typeIri: String)             = new DialectDomainElementModel(Seq(typeIri))
}
