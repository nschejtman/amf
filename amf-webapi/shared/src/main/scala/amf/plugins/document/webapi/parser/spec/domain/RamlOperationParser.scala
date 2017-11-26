package amf.plugins.document.webapi.parser.spec.domain

import amf.core.metamodel.domain.DomainElementModel
import amf.core.model.domain.AmfArray
import amf.core.parser.Annotations
import amf.core.parser._
import amf.plugins.document.webapi.contexts.WebApiContext
import amf.plugins.domain.webapi.metamodel.OperationModel
import amf.plugins.domain.webapi.metamodel.OperationModel.Method
import amf.plugins.domain.webapi.models.{Operation, Response}
import amf.plugins.document.webapi.parser.spec.common.AnnotationParser
import amf.plugins.document.webapi.parser.spec.declaration.OasCreativeWorkParser
import amf.plugins.domain.shapes.models.CreativeWork
import org.yaml.model._

import scala.collection.mutable

/**
  *
  */
case class RamlOperationParser(entry: YMapEntry, producer: (String) => Operation, parseOptional: Boolean = false)(
    implicit ctx: WebApiContext) {

  def parse(): Operation = {

    val method: String = entry.key

    val operation = producer(method).add(Annotations(entry))
    operation.set(Method, ValueNode(entry.key).string())

    if (parseOptional && method.endsWith("?")) {
      operation.set(OperationModel.Optional, value = true)
      operation.set(OperationModel.Method, method.stripSuffix("?"))
    }

    entry.value.tagType match {

      // Regular operation
      case YType.Map =>
        val map = entry.value.as[YMap]
        ctx.closedShape(operation.id, map, "operation")

        map.key("displayName", entry => {
          val value = ValueNode(entry.value)
          operation.set(OperationModel.Name, value.string(), Annotations(entry))
        })

        map.key("description", entry => {
          val value = ValueNode(entry.value)
          operation.set(OperationModel.Description, value.string(), Annotations(entry))
        })

        map.key("(deprecated)", entry => {
          val value = ValueNode(entry.value)
          operation.set(OperationModel.Deprecated, value.boolean(), Annotations(entry))
        })

        map.key("(summary)", entry => {
          val value = ValueNode(entry.value)
          operation.set(OperationModel.Summary, value.string(), Annotations(entry))
        })

        map.key(
          "(externalDocs)",
          entry => {
            val creativeWork: CreativeWork = OasCreativeWorkParser(entry.value.as[YMap]).parse()
            operation.set(OperationModel.Documentation, creativeWork, Annotations(entry))
          }
        )

        map.key(
          "protocols",
          entry => {
            val value = ArrayNode(entry.value)
            operation.set(OperationModel.Schemes, value.strings(), Annotations(entry))
          }
        )

        map.key("(consumes)", entry => {
          val value = ArrayNode(entry.value)
          operation.set(OperationModel.Accepts, value.strings(), Annotations(entry))
        })

        map.key("(produces)", entry => {
          val value = ArrayNode(entry.value)
          operation.set(OperationModel.ContentType, value.strings(), Annotations(entry))
        })

        map.key(
          "is",
          entry => {
            val traits = entry.value
              .as[Seq[YNode]]
              .map(value => {
                ParametrizedDeclarationParser(value, operation.withTrait, ctx.declarations.findTraitOrError(value))
                  .parse()
              })
            if (traits.nonEmpty) operation.setArray(DomainElementModel.Extends, traits, Annotations(entry))
          }
        )

        RamlRequestParser(map, () => operation.withRequest())
          .parse()
          .map(operation.set(OperationModel.Request, _))

        map.key(
          "responses",
          entry => {
            entry.value
              .as[YMap]
              .regex(
                "\\d{3}",
                entries => {
                  val responses = mutable.ListBuffer[Response]()
                  entries.foreach(entry => {
                    responses += RamlResponseParser(entry, operation.withResponse)
                      .parse()
                  })
                  operation.set(OperationModel.Responses,
                                AmfArray(responses, Annotations(entry.value)),
                                Annotations(entry))
                }
              )
          }
        )

        map.key(
          "securedBy",
          entry => {
            // TODO check for empty array for resolution ?
            val securedBy = entry.value
              .as[Seq[YNode]]
              .map(s =>
                RamlParametrizedSecuritySchemeParser(s, operation.withSecurity)
                  .parse())

            operation.set(OperationModel.Security, AmfArray(securedBy, Annotations(entry.value)), Annotations(entry))
          }
        )

        AnnotationParser(() => operation, map).parse()

        operation

      // Empty operation
      case _ if entry.value.toOption[YScalar].map(_.text).exists(s => s == "" || s == "null") => operation

      case _ =>
        ctx.violation(operation.id, s"Invalid node ${entry.value} for method $method", entry.value)
        operation
    }
  }
}
