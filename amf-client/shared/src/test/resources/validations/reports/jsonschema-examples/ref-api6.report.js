Model: file://amf-client/shared/src/test/resources/validations/jsonschema/ref/api6.raml
Profile: RAML 1.0
Conforms? false
Number of results: 1

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: nodes[0].subtree.nodes[0].value should be number
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/jsonschema/ref/api6.raml#/web-api/end-points/%2Fep2/get/200/application%2Fjson/schema/example/default-example
  Property: 
  Position: Some(LexicalInformation([(61,0)-(75,0)]))
  Location: file://amf-client/shared/src/test/resources/validations/jsonschema/ref/api6.raml
