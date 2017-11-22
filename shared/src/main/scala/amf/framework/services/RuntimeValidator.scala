package amf.framework.services

import amf.document.BaseUnit
import amf.framework.validation.{AMFValidationReport, EffectiveValidations}
import amf.framework.validation.core.ValidationReport

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Validation of AMF models
  */
trait RuntimeValidator {

  /**
    * Loads a validation profile from a URL
    */
  def loadValidationProfile(validationProfilePath: String): Future[Unit]

  /**
    * Low level validation returning a SHACL validation report
    */
  def shaclValidation(model: BaseUnit, validations: EffectiveValidations, messageStyle: String): Future[ValidationReport]

  /**
    * Main validation function returning an AMF validation report linking validation errors
    * for validations in the profile to domain elements in the model
    */
  def validate(model: BaseUnit, profileName: String, messageStyle: String): Future[AMFValidationReport]
}

object RuntimeValidator {
  var validator: Option[RuntimeValidator] = None
  def register(runtimeValidator: RuntimeValidator) = {
    validator = Some(runtimeValidator)
  }

  def loadValidationProfile(validationProfilePath: String) = {
    validator match {
      case Some(runtimeValidator) => runtimeValidator.loadValidationProfile(validationProfilePath)
      case None                   => throw new Exception("No registered runtime validator")
    }
  }

  def shaclValidation(model: BaseUnit, validations: EffectiveValidations, messgeStyle: String = "AMF"): Future[ValidationReport] = {
    validator match {
      case Some(runtimeValidator) => runtimeValidator.shaclValidation(model, validations, messgeStyle)
      case None                   => throw new Exception("No registered runtime validator")
    }
  }

  def apply(model: BaseUnit, profileName: String, messageStyle: String = "AMF"): Future[AMFValidationReport] = {
    validator match {
      case Some(runtimeValidator) => runtimeValidator.validate(model, profileName, messageStyle)
      case None                   => throw new Exception("No registered runtime validator")
    }
  }

}
