Model: file://amf-client/shared/src/test/resources/validations/resource_types/examplesValidation.raml
Profile: RAML 1.0
Conforms? false
Number of results: 2

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: should be <= 10
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/resource_types/examplesValidation.raml#/web-api/end-points/%2FnonParametrizedIncorrect/get/request/application%2Fjson/any/schema/example/default-example
  Property: 
  Position: Some(LexicalInformation([(34,17)-(34,19)]))
  Location: file://amf-client/shared/src/test/resources/validations/resource_types/examplesValidation.raml

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: should be <= 10
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/resource_types/examplesValidation.raml#/web-api/end-points/%2FparametrizedIncorrect/get/request/application%2Fjson/any/schema/example/default-example
  Property: 
  Position: Some(LexicalInformation([(52,17)-(52,19)]))
  Location: file://amf-client/shared/src/test/resources/validations/resource_types/examplesValidation.raml
