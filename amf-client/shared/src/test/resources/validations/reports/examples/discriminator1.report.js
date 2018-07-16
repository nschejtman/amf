Model: file://amf-client/shared/src/test/resources/validations/examples/discriminator1.raml
Profile: RAML
Conforms? false
Number of results: 3

Level: Violation

- Source: http://a.ml/vocabularies/amf/parser#exampleError
  Message: Unknown discriminator value
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/examples/discriminator1.raml#/web-api/end-points/%2Fresource/get/200/application%2Fjson/schema/example/bad1
  Property: 
  Position: Some(LexicalInformation([(34,0)-(37,0)]))
  Location: 

- Source: http://a.ml/vocabularies/amf/parser#exampleError
  Message: {"keyword":"type","dataPath":".employeeId","schemaPath":"#/properties/employeeId/type","params":{"type":"integer"},"message":"should be integer"}
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/examples/discriminator1.raml#/web-api/end-points/%2Fresource/get/200/application%2Fjson/schema/example/bad2
  Property: 
  Position: Some(LexicalInformation([(42,0)-(45,0)]))
  Location: 

- Source: http://a.ml/vocabularies/amf/parser#exampleError
  Message: Unknown discriminator value
  Level: Violation
  Target: file://amf-client/shared/src/test/resources/validations/examples/discriminator1.raml#/web-api/end-points/%2Fresource/get/200/application%2Fjson/schema/example/bad3
  Property: 
  Position: Some(LexicalInformation([(46,0)-(50,0)]))
  Location: 