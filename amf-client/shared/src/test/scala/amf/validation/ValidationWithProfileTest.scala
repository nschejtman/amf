package amf.validation

import amf.core.validation.AMFValidationReport
import amf.plugins.features.validation.emitters.ValidationReportJSONLDEmitter

class ValidationWithProfileTest extends ValidationReportGenTest {

  override val basePath: String    = "file://amf-client/shared/src/test/resources/validations/"
  override val reportsPath: String = "amf-client/shared/src/test/resources/validations/reports/with-profiles/"

  private def cycle(api: String, profile: String, profileFile: Option[String]) =
    super.cycle(api, None, profile, profileFile)

  override protected def generate(report: AMFValidationReport): String = ValidationReportJSONLDEmitter.emitJSON(report)

  test("Validation test, ignore profile") {
    cycle("data/error1.raml", "Test Profile", Some("data/error1_ignore_profile.raml"))
  }

  test("Raml Vocabulary") {
    cycle("data/error1.raml", "Test Profile", Some("data/custom_function_validation_success.raml"))
  }

  test("Custom function validation failure test") {
    cycle("data/error1.raml",
          Some("custom-validation-error.report"),
          "Test Profile",
          Some("data/custom_function_validation_error.raml"))
  }

  test("Validation test, custom validation profile") {
    cycle("data/error1.raml",
          Some("error1-custom-validation.report"),
          "Test Profile",
          Some("data/error1_custom_validation_profile.raml"))
  }

  test("Banking example validation") {
    cycle("banking/api.raml", Some("baking-api.report"), "Banking", Some("banking/profile.raml"))
  }

  ignore("Example JS library validations") {
    cycle("libraries/api.raml", Some("libraries-profile.report"), "Test", Some("libraries/profile.raml"))
//      assert(!report.conforms)
//      assert(report.results.length == 1)
  }

}
