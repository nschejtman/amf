package amf.plugins.document.webapi.parser.spec.domain

import amf.core.emitter.BaseEmitters._
import amf.core.emitter.{EntryEmitter, SpecOrdering}
import amf.core.model.document.BaseUnit
import amf.core.parser.{FieldEntry, Fields, Position}
import amf.plugins.document.webapi.contexts.RamlSpecEmitterContext
import amf.plugins.document.webapi.parser.spec.declaration.AnnotationsEmitter
import amf.plugins.domain.webapi.metamodel.{RequestModel, ResponseModel}
import amf.plugins.domain.webapi.models.Response
import org.yaml.model.YDocument.EntryBuilder

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  *
  */
case class RamlResponsesEmitter(key: String, f: FieldEntry, ordering: SpecOrdering, references: Seq[BaseUnit])(
    implicit spec: RamlSpecEmitterContext)
    extends EntryEmitter {

  override def emit(b: EntryBuilder): Unit = {
    sourceOr(
      f.value.annotations,
      b.entry(key, _.obj { traverse(responses(f, ordering, references), _) })
    )
  }

  private def responses(f: FieldEntry, ordering: SpecOrdering, references: Seq[BaseUnit]): Seq[EntryEmitter] = {
    val result = f.array.values.map(e => spec.factory.responseEmitter(e.asInstanceOf[Response], ordering, references))
    ordering.sorted(result)
  }

  override def position(): Position = pos(f.value.annotations)
}

case class Raml10ResponseEmitter(response: Response, ordering: SpecOrdering, references: Seq[BaseUnit])(
    implicit spec: RamlSpecEmitterContext)
    extends RamlResponseEmitter(response, ordering, references) {

  override protected def emitters(fs: Fields): ListBuffer[EntryEmitter] = {
    val result = ListBuffer[EntryEmitter]()

    fs.entry(ResponseModel.Examples).map(f => result += OasResponseExamplesEmitter("(examples)", f, ordering))

    result ++= AnnotationsEmitter(response, ordering).emitters

    super.emitters(fs) ++ result
  }

}

case class Raml08ResponseEmitter(response: Response, ordering: SpecOrdering, references: Seq[BaseUnit])(
    implicit spec: RamlSpecEmitterContext)
    extends RamlResponseEmitter(response, ordering, references) {}

abstract class RamlResponseEmitter(response: Response, ordering: SpecOrdering, references: Seq[BaseUnit])(
    implicit spec: RamlSpecEmitterContext)
    extends EntryEmitter {

  protected def emitters(fs: Fields): ListBuffer[EntryEmitter] = {
    val result = mutable.ListBuffer[EntryEmitter]()

    fs.entry(ResponseModel.Description).map(f => result += ValueEmitter("description", f))
    fs.entry(RequestModel.Headers).map(f => result += RamlParametersEmitter("headers", f, ordering, references))
    fs.entry(RequestModel.Payloads).map(f => result += spec.factory.payloadsEmitter("body", f, ordering, references))
    result
  }

  override def emit(b: EntryBuilder): Unit = {
    val fs = response.fields
    sourceOr(
      response.annotations,
      b.complexEntry(
        ScalarEmitter(fs.entry(ResponseModel.StatusCode).get.scalar).emit(_),
        _.obj { b =>
          traverse(ordering.sorted(emitters(fs)), b)
        }
      )
    )
  }

  override def position(): Position = pos(response.annotations)
}
