Model: file://amf-client/shared/src/test/resources/validations/jsonschema/allOf/api1.raml
Profile: RAML 1.0
Conforms? false
Number of results: 3

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: should have required property 'bar'
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/jsonschema/allOf/api1.raml#/web-api/end-points/%2Fep2/get/200/application%2Fjson/any/schema/example/default-example
  Property: 
  Position: Some(LexicalInformation([(42,0)-(44,0)]))
  Location: file://amf-client/shared/src/test/resources/validations/jsonschema/allOf/api1.raml

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: should have required property 'foo'
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/jsonschema/allOf/api1.raml#/web-api/end-points/%2Fep3/get/200/application%2Fjson/any/schema/example/default-example
  Property: 
  Position: Some(LexicalInformation([(52,0)-(54,0)]))
  Location: file://amf-client/shared/src/test/resources/validations/jsonschema/allOf/api1.raml

- Source: http://a.ml/vocabularies/amf/parser#example-validation-error
  Message: bar should be integer
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/jsonschema/allOf/api1.raml#/web-api/end-points/%2Fep4/get/200/application%2Fjson/any/schema/example/default-example
  Property: 
  Position: Some(LexicalInformation([(62,0)-(63,23)]))
  Location: file://amf-client/shared/src/test/resources/validations/jsonschema/allOf/api1.raml
