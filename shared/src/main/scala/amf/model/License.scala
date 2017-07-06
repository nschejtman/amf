package amf.model

import amf.builder.LicenseBuilder

import scala.scalajs.js.annotation.JSExportAll

/**
  * Domain element of type raml-http:License
  *
  * Properties ->
  *     - schema-org:url
  *     - schema-org:name
  */
@JSExportAll
class License(val url: String, val name: String) extends DomainElement[License, LicenseBuilder] {
  override def toBuilder: LicenseBuilder = LicenseBuilder().withUrl(url).withName(name)

  def canEqual(other: Any): Boolean = other.isInstanceOf[License]

  override def equals(other: Any): Boolean = other match {
    case that: License =>
      (that canEqual this) &&
        url == that.url &&
        name == that.name
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(url, name)
    state.map(p => if (p != null) p.hashCode() else 0).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"License($url, $name)"
}
