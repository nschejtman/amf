Model: file://amf-client/shared/src/test/resources/org/raml/parser/types/sugar/object/input.raml
Profile: RAML
Conforms? false
Number of results: 1

Level: Violation

- Source: file://amf-client/shared/src/test/resources/org/raml/parser/types/sugar/object/input.raml#/declarations/types/Person_validation
  Message: Object at / must be valid
Scalar at //name must have data type http://www.w3.org/2001/XMLSchema#integer

  Level: Violation
  Target: file://amf-client/shared/src/test/resources/org/raml/parser/types/sugar/object/input.raml#/declarations/types/Person/example/default-example
  Property: http://a.ml/vocabularies/data#name
  Position: Some(LexicalInformation([(8,0)-(8,18)]))
  Location: file://amf-client/shared/src/test/resources/org/raml/parser/types/sugar/object/input.raml