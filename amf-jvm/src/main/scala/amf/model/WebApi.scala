package amf.model

import scala.collection.JavaConverters._

/**
  * WebApi java class
  */
case class WebApi private (private val webApi: amf.domain.WebApi) extends DomainElement {

  def this() = this(amf.domain.WebApi())

  val name: String                                 = webApi.name
  val description: String                          = webApi.description
  val host: String                                 = webApi.host
  val schemes: java.util.List[String]              = webApi.schemes.asJava
  val endPoints: java.util.List[EndPoint]          = webApi.endPoints.map(EndPoint).asJava
  val basePath: String                             = webApi.basePath
  val accepts: java.util.List[String]              = webApi.accepts.asJava
  val contentType: java.util.List[String]          = webApi.contentType.asJava
  val version: String                              = webApi.version
  val termsOfService: String                       = webApi.termsOfService
  val provider: Organization                       = Option(webApi.provider).map(amf.model.Organization).orNull
  val license: License                             = Option(webApi.license).map(amf.model.License).orNull
  val documentation: CreativeWork                  = Option(webApi.documentation).map(amf.model.CreativeWork).orNull
  val baseUriParameters: java.util.List[Parameter] = webApi.baseUriParameters.map(Parameter).asJava

  override private[amf] def element: amf.domain.WebApi = webApi

  /** Set name property of this [[WebApi]]. */
  def withName(name: String): this.type = {
    webApi.withName(name)
    this
  }

  /** Set description property of this [[WebApi]]. */
  def withDescription(description: String): this.type = {
    webApi.withDescription(description)
    this
  }

  /** Set host property of this [[WebApi]]. */
  def withHost(host: String): this.type = {
    webApi.withHost(host)
    this
  }

  /** Set schemes property of this [[WebApi]]. */
  def withSchemes(schemes: java.util.List[String]): this.type = {
    webApi.withSchemes(schemes.asScala)
    this
  }

  /** Set endPoints property of this [[WebApi]]. */
  def withEndPoints(endPoints: java.util.List[EndPoint]): this.type = {
    webApi.withEndPoints(endPoints.asScala.map(_.element))
    this
  }

  /** Set basePath property of this [[WebApi]]. */
  def withBasePath(path: String): this.type = {
    webApi.withBasePath(path)
    this
  }

  /** Set accepts property of this [[WebApi]]. */
  def withAccepts(accepts: java.util.List[String]): this.type = {
    webApi.withAccepts(accepts.asScala)
    this
  }

  /** Set contentType property of this [[WebApi]]. */
  def withContentType(contentType: java.util.List[String]): this.type = {
    webApi.withContentType(contentType.asScala)
    this
  }

  /** Set version property of this [[WebApi]]. */
  def withVersion(version: String): this.type = {
    webApi.withVersion(version)
    this
  }

  /** Set termsOfService property of this [[WebApi]]. */
  def withTermsOfService(terms: String): this.type = {
    webApi.withTermsOfService(terms)
    this
  }

  /** Set provider property of this [[WebApi]] using a [[Organization]]. */
  def withProvider(provider: Organization): this.type = {
    webApi.withProvider(provider.element)
    this
  }

  /** Set license property of this [[WebApi]] using a [[License]]. */
  def withLicense(license: License): this.type = {
    webApi.withLicense(license.element)
    this
  }

  /** Set documentation property of this [[WebApi]] using a [[CreativeWork]]. */
  def withDocumentation(documentation: CreativeWork): this.type = {
    webApi.withDocumentation(documentation.element)
    this
  }

  /** Set baseUriParameters property of this [[WebApi]]. */
  def withBaseUriParameters(parameters: java.util.List[Parameter]): this.type = {
    webApi.withBaseUriParameters(parameters.asScala.map(_.element))
    this
  }

  /**
    * Adds one [[EndPoint]] to the endPoints property of this [[WebApi]] and returns it for population.
    * Path property of the endPoint is required.
    */
  def withEndPoint(path: String): EndPoint = EndPoint(webApi.withEndPoint(path))

  /**
    * Adds one [[Parameter]] to the baseUriParameters property of this [[WebApi]] and returns it for population.
    * Name property of the parameter is required.
    */
  def withBaseUriParameter(name: String): Parameter = Parameter(webApi.withBaseUriParameter(name))
}
