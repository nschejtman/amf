Model: file://amf-client/shared/src/test/resources/org/raml/parser/examples/complex/input.raml
Profile: RAML
Conforms? false
Number of results: 1

Level: Violation

- Source: file://amf-client/shared/src/test/resources/org/raml/parser/examples/complex/input.raml#/declarations/types/Org_validation
  Message: Object at / must be valid
Data at //onCall must be a valid polymorphic type: Manager, AlertableAdmin
Object at //Head must be valid
Array items at //Head/reports must be valid
Object at //Head/reports/items must be valid
Data at //Head/reports/items/kind must match pattern ^Person$
Data at //Head/phone must match pattern ^[0-9|-]+$

  Level: Violation
  Target: file://amf-client/shared/src/test/resources/org/raml/parser/examples/complex/input.raml#/web-api/end-points/%2Forgs%2F%7BorgId%7D/get/200/application%2Fjson/schema/example/default-example
  Property: file://amf-client/shared/src/test/resources/org/raml/parser/examples/complex/input.raml#/web-api/end-points/%2Forgs%2F%7BorgId%7D/get/200/application%2Fjson/schema/example/default-example
  Position: Some(LexicalInformation([(44,0)-(59,31)]))
  Location: file://amf-client/shared/src/test/resources/org/raml/parser/examples/complex/input.raml