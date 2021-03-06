package amf.plugins.document.webapi.parser.spec.declaration
import amf.core.annotations.LexicalInformation
import amf.core.parser.{Annotations, Range, YMapOps, SearchScope}
import org.yaml.model.{YType, YMap, YPart, YNode}
import amf.plugins.document.webapi.parser.spec.WebApiDeclarations.ErrorSecurityScheme
import amf.plugins.document.webapi.parser.spec.common.AnnotationParser
import amf.plugins.document.webapi.parser.spec.toRaml
import amf.plugins.domain.webapi.metamodel.security.SecuritySchemeModel
import amf.plugins.domain.webapi.models.security.SecurityScheme
import amf.plugins.features.validation.CoreValidations
import amf.validations.ParserSideValidations.{
  MissingSecuritySchemeErrorSpecification,
  CrossSecurityWarningSpecification
}
import amf.core.utils.AmfStrings
import amf.plugins.document.webapi.contexts.parser.OasLikeWebApiContext

abstract class OasLikeSecuritySchemeParser(part: YPart, adopt: SecurityScheme => SecurityScheme)(
    implicit ctx: OasLikeWebApiContext)
    extends SecuritySchemeParser(part, adopt) {

  def crossSecurityWarnings(scheme: SecurityScheme): Unit

  def parseType(map: YMap, scheme: SecurityScheme): Unit = {
    map.key("type", SecuritySchemeModel.Type in scheme)

    scheme.`type`.option() match {
      case Some(s) if s.startsWith("x-") =>
        ctx.eh.warning(
          CrossSecurityWarningSpecification,
          scheme.id,
          Some(SecuritySchemeModel.Type.value.iri()),
          s"RAML 1.0 extension security scheme type '$s' detected in ${ctx.vendor.name} spec",
          scheme.`type`.annotations().find(classOf[LexicalInformation]),
          Some(ctx.rootContextDocument)
        )
      case _ =>
    }

    map.key(
      "type",
      value => {
        // we need to check this because of the problem parsing nulls like empty strings of value null
        if (value.value.tagType == YType.Null && scheme.`type`.option().contains("")) {
          ctx.eh.violation(
            MissingSecuritySchemeErrorSpecification,
            scheme.id,
            Some(SecuritySchemeModel.Type.value.iri()),
            "Security Scheme must have a mandatory value from 'oauth2', 'basic' or 'apiKey'",
            Some(LexicalInformation(Range(map.range))),
            Some(ctx.rootContextDocument)
          )
        }
      }
    )

    scheme.normalizeType() // normalize the common type

  }

  def parseReferenced(parsedUrl: String, node: YNode, adopt: SecurityScheme => SecurityScheme): SecurityScheme = {
    ctx.declarations
      .findSecurityScheme(parsedUrl, SearchScope.Fragments)
      .map(securityScheme => {
        val scheme: SecurityScheme = securityScheme.link(parsedUrl, Annotations(node))
        adopt(scheme)
        scheme
      })
      .getOrElse {
        ctx.obtainRemoteYNode(parsedUrl) match {
          case Some(schemeNode) =>
            ctx.factory.securitySchemeParser(schemeNode, adopt).parse()
          case None =>
            ctx.eh.violation(CoreValidations.UnresolvedReference,
                             "",
                             s"Cannot find security scheme reference $parsedUrl",
                             Annotations(node))
            adopt(ErrorSecurityScheme(parsedUrl, node))
        }
      }
  }

}
