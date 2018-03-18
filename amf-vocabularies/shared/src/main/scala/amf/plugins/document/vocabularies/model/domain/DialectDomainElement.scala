package amf.plugins.document.vocabularies.model.domain

import amf.core.metamodel.{Field, Obj, Type}
import amf.core.model.domain._
import amf.core.parser.{Annotations, Fields}
import amf.core.vocabulary.ValueType
import amf.plugins.document.vocabularies.metamodel.domain.DialectDomainElementModel
import org.mulesoft.common.time.SimpleDateTime
import org.yaml.model.{YMap, YNode}

import scala.collection.mutable

case class DialectDomainElement(override val fields: Fields, annotations: Annotations) extends DynamicDomainElement with Linkable {

  val literalProperties: mutable.Map[String, Any] = mutable.HashMap()
  val mapKeyProperties: mutable.Map[String, Any] = mutable.HashMap()
  val objectProperties: mutable.Map[String, DialectDomainElement] = mutable.HashMap()
  val objectCollectionProperties: mutable.Map[String, Seq[DialectDomainElement]] = mutable.HashMap()
  val propertyAnnotations: mutable.Map[String, Annotations] = mutable.HashMap()

  // Types of the instance
  protected var instanceTypes: Seq[String] = Nil
  def withInstanceTypes(types: Seq[String]) = {
    instanceTypes = types
    this
  }
  override def dynamicType: List[ValueType] = (instanceTypes.distinct.map(iriToValue) ++ DialectDomainElementModel().`type`).toList

  // Dialect mapping defining the instance
  protected var instanceDefinedBy: Option[NodeMapping] = None
  def withDefinedBy(nodeMapping: NodeMapping) = {
    instanceDefinedBy = Some(nodeMapping)
    this
  }
  def definedBy: NodeMapping = instanceDefinedBy match {
    case Some(mapping) => mapping
    case None          => throw new Exception("NodeMapping for the instance not defined")
  }

  def localRefName: String = {
    if (isLink)
      linkTarget.map(_.id.split("#").last.split("/").last).getOrElse {
        throw new Exception(s"Cannot produce local reference without linked element at elem $id")
      }
    else id.split("#").last.split("/").last
  }

  def includeName: String = {
    if (isLink)
      linkLabel.getOrElse(linkTarget.map(_.id.split("#").head).getOrElse(throw new Exception(s"Cannot produce include reference without linked element at elem $id")))
    else
      throw new Exception(s"Cannot produce include reference without linked element at elem $id")
  }

  def iriToValue(iri: String) = ValueType(iri)

  override def dynamicFields: List[Field] = {
    val mapKeyFields = mapKeyProperties.keys map { propertyId =>
        Field(Type.Str, iriToValue(propertyId))
    }

    (literalProperties.keys ++ objectProperties.keys ++ objectCollectionProperties.keys).map { propertyId =>
      val property = instanceDefinedBy.get.propertiesMapping().find(_.id == propertyId).get
      property.toField()
    }.toList ++ mapKeyFields ++ fields.fields().map(_.field)// ++ LinkableElementModel.fields
  }


  def findPropertyByTermPropertyId(termPropertyId: String) =
    definedBy.propertiesMapping().find(_.nodePropertyMapping().value() == termPropertyId).map(_.id).getOrElse(termPropertyId)

  def findPropertyMappingByTermPropertyId(termPropertyId: String) =
    definedBy.propertiesMapping().find(_.nodePropertyMapping().value() == termPropertyId)


  override def valueForField(f: Field): Option[AmfElement] = {
    val termPropertyId = f.value.iri()
    val propertyId = findPropertyByTermPropertyId(termPropertyId)
    val annotations = propertyAnnotations.getOrElse(propertyId, Annotations())

    // Warning, mapKey has the term property id, no the property mapping id because
    // there's no real propertyMapping for it
    mapKeyProperties.get(termPropertyId) map { stringValue =>
      AmfScalar(stringValue, annotations)
    } orElse objectProperties.get(propertyId) map { dialectDomainElement =>
      dialectDomainElement
    } orElse  {
      objectCollectionProperties.get(propertyId) map { seqElements =>
        AmfArray(seqElements, annotations)
      }
    } orElse {
      literalProperties.get(propertyId) map {
        case vs: Seq[_] =>
          val scalars = vs.map { s => AmfScalar(s) }
          AmfArray(scalars, annotations)
        case other =>
          AmfScalar(other, annotations)
      }
    } orElse {
      fields.fields().find(_.field == f).map(_.element)
    }
  }

  def containsProperty(property: PropertyMapping): Boolean = {
    mapKeyProperties.contains(property.nodePropertyMapping().value()) ||
    objectCollectionProperties.contains(property.id) ||
    literalProperties.contains(property.id)
  }


  def setObjectField(property: PropertyMapping, value: DialectDomainElement, node: YNode) = {
    objectProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    if (value.isUnresolved) {
      value.toFutureRef {
        case resolvedDialectDomainElement: DialectDomainElement =>
          objectProperties.put(property.id,
            resolveUnreferencedLink(value.refName, value.annotations, resolvedDialectDomainElement)
              .withId(value.id)
          )
        case resolved => throw new Exception(s"Cannot resolve reference with not dialect domain element value ${resolved.id}")
      }
    }
    this
  }

  def setObjectField(property: PropertyMapping, value: Seq[DialectDomainElement], node: YNode) = {
    objectCollectionProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    value.foreach {
      case linkable: Linkable if linkable.isUnresolved =>
        linkable.toFutureRef((resolved) => {
          objectCollectionProperties.get(property.id) map { oldValues =>
            oldValues map { oldValue =>
              if (oldValue.id == resolved.id) {
                resolved
              } else {
                oldValue
              }
            }
          } foreach { case updatedValues: Seq[DialectDomainElement] =>
            objectCollectionProperties.put(property.id, updatedValues)
          }
        })
      case _ => // ignore
    }
    this
  }

  def setLiteralField(property: PropertyMapping, value: Int, node: YNode) = {
    literalProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    this
  }

  def setLiteralField(property: PropertyMapping, value: Float, node: YNode) = {
    literalProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    this
  }

  def setLiteralField(property: PropertyMapping, value: Double, node: YNode) = {
    literalProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    this
  }

  def setLiteralField(property: PropertyMapping, value: Boolean, node: YNode) = {
    literalProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    this
  }

  def setLiteralField(property: PropertyMapping, value: Seq[_], node: YNode) = {
    literalProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    this
  }

  def setLiteralField(property: PropertyMapping, value: String, node: YNode) = {
    literalProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    this
  }

  def setLiteralField(property: PropertyMapping, value: SimpleDateTime, node: YNode) = {
    literalProperties.put(property.id, value)
    propertyAnnotations.put(property.id, Annotations(node))
    this
  }

  def setMapKeyField(propertyId: String, value: String, node: YNode) = {
    mapKeyProperties.put(propertyId, value)
    this
  }

  override def meta: Obj = if (instanceTypes.isEmpty) {
    DialectDomainElementModel()
  } else {
    new DialectDomainElementModel(instanceTypes.head, dynamicFields, Some(definedBy))
  }

  override def adopted(newId: String): DialectDomainElement.this.type = if (Option(this.id).isEmpty) withId(newId) else this

  override def linkCopy(): Linkable = DialectDomainElement().withId(id).withDefinedBy(definedBy).withInstanceTypes(instanceTypes)

  override def resolveUnreferencedLink[T](label: String, annotations: Annotations, unresolved: T): T = {
    val unresolvedNodeMapping = unresolved.asInstanceOf[DialectDomainElement]
    unresolvedNodeMapping.link(label, annotations).asInstanceOf[DialectDomainElement].asInstanceOf[T]
  }
}

object DialectDomainElement {
  def apply(): DialectDomainElement = apply(Annotations())

  def apply(ast: YMap): DialectDomainElement = apply(Annotations(ast))

  def apply(annotations: Annotations): DialectDomainElement = DialectDomainElement(Fields(), annotations)

}