package amf.framework.validation

import amf.ProfileNames
import amf.document.BaseUnit
import amf.framework.validation.core.ValidationResult
import amf.validation.model.ValidationSpecification
import amf.vocabulary.Namespace

import scala.collection.mutable

trait ValidationResultProcessor {

  protected def processAggregatedResult(result: AMFValidationResult,
                                        messageStyle: String,
                                        validations: EffectiveValidations): AMFValidationResult = {
    val spec = validations.all.get(result.validationId) match {
      case Some(s) => s
      case None    => throw new Exception(s"Cannot find spec for aggregated validation result ${result.validationId}")
    }

    var message: String = messageStyle match {
      case ProfileNames.RAML => spec.ramlMessage.getOrElse(result.message)
      case ProfileNames.OAS  => spec.oasMessage.getOrElse(result.message)
      case _                 => spec.message
    }
    if (message == "") {
      message = "Constraint violation"
    }

    val severity = findLevel(spec.id(), validations)
    new AMFValidationResult(message, severity, result.targetNode, result.targetProperty, spec.id(), result.position)
  }

  protected def buildValidationResult(model: BaseUnit,
                                      result: ValidationResult,
                                      messageStyle: String,
                                      validations: EffectiveValidations): Option[AMFValidationResult] = {
    val validationSpecToLook = if (result.sourceShape.startsWith(Namespace.Data.base)) {
      result.sourceShape
        .replace(Namespace.Data.base, "") // this is for custom validations they are all prefixed with the data namespace
    } else {
      result.sourceShape // by default we expect to find a URI here
    }
    val idMapping: mutable.HashMap[String, String] = mutable.HashMap()
    val maybeTargetSpec: Option[ValidationSpecification] = validations.all.get(validationSpecToLook) match {
      case Some(validationSpec) =>
        idMapping.put(result.sourceShape, validationSpecToLook)
        Some(validationSpec)

      case None =>
        validations.all.find {
          case (v, _) =>
            // processing property shapes Id computed as constraintID + "/prop"

            validationSpecToLook.startsWith(v)
        } match {
          case Some((v, spec)) =>
            idMapping.put(result.sourceShape, v)
            Some(spec)
          case None =>
            if (validationSpecToLook.startsWith("_:")) {
              None
            } else {
              throw new Exception(s"Cannot find validation spec for validation error:\n $result")
            }
        }
    }

    maybeTargetSpec match {
      case Some(targetSpec) =>
        var message = messageStyle match {
          case ProfileNames.RAML => targetSpec.ramlMessage.getOrElse(targetSpec.message)
          case ProfileNames.OAS  => targetSpec.ramlMessage.getOrElse(targetSpec.message)
          case _                 => Option(targetSpec.message).getOrElse(result.message.getOrElse(""))
        }

        if (Option(message).isEmpty || message == "") {
          message = result.message.getOrElse("Constraint violation")
        }

        val finalId = idMapping(result.sourceShape).startsWith("http") match {
          case true => idMapping(result.sourceShape)
          case false =>
            Namespace.Data.base + idMapping(result.sourceShape) // we put back the prefix for the custom validations
        }
        val severity = findLevel(idMapping(result.sourceShape), validations)
        Some(
          AMFValidationResult.withShapeId(finalId,
            AMFValidationResult.fromSHACLValidation(model, message, severity, result)))
      case _ => None
    }
  }

  protected def findLevel(id: String, validations: EffectiveValidations): String = {
    if (validations.info.get(id).isDefined) {
      SeverityLevels.INFO
    } else if (validations.warning.get(id).isDefined) {
      SeverityLevels.WARNING
    } else {
      SeverityLevels.VIOLATION
    }
  }


}
