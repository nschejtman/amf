Model: file://amf-client/shared/src/test/resources/validations/recursives/props1.raml
Profile: RAML 1.0
Conforms? false
Number of results: 1

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: a.a.b should be string
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/recursives/props1.raml#/declarations/types/A/example/invalid
  Property: 
  Position: Some(LexicalInformation([(16,0)-(21,0)]))
  Location: file://amf-client/shared/src/test/resources/validations/recursives/props1.raml
