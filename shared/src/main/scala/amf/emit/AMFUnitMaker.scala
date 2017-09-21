package amf.emit

import amf.client.GenerationOptions
import amf.document.BaseUnit
import amf.graph.GraphEmitter
import amf.remote.{Amf, Oas, Raml, Vendor}
import amf.spec.oas.OasSpecEmitter
import amf.spec.raml.RamlSpecEmitter
import org.yaml.model.YDocument

/**
  * AMF Unit Maker
  */
class AMFUnitMaker {

  def make(unit: BaseUnit, vendor: Vendor, options: GenerationOptions): YDocument = {
    vendor match {
      case Amf        => makeAmfWebApi(unit, options)
      case Raml | Oas => makeUnitWithSpec(unit, vendor)
    }
  }

  private def makeUnitWithSpec(unit: BaseUnit, vendor: Vendor): YDocument = {
    vendor match {
      case Raml =>
        RamlSpecEmitter(unit).emitDocument()
      case Oas =>
        OasSpecEmitter(unit).emitDocument()
      case _ => throw new IllegalStateException("Invalid vendor " + vendor)
    }
  }

  private def makeAmfWebApi(unit: BaseUnit, options: GenerationOptions): YDocument = GraphEmitter.emit(unit, options)
}

object AMFUnitMaker {
  def apply(unit: BaseUnit, vendor: Vendor, options: GenerationOptions): YDocument =
    new AMFUnitMaker().make(unit, vendor, options)
}
