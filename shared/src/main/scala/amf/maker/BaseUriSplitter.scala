package amf.maker

/**
  * Created by hernan.najles on 6/29/17.
  */
class BaseUriSplitter(val protocol: String, val domain: String, val path: String) {

  def url(): String =
    (if (protocol.isEmpty) "" else protocol + "://") + domain +
      (if (path.startsWith("/")) path else "/" + path)

}

object BaseUriSplitter {
  def apply(s: String): BaseUriSplitter = {
    if (s == null) return new BaseUriSplitter("", "", "")

    var tail = ""
    var host = ""
    if (s.contains("://")) {
      val splittedOne = s.split("://")
      host = splittedOne.head
      tail = splittedOne(1)
    } else {
      tail = s
    }
    val firstSlashIndex = tail.indexOf("/")
    var domain          = ""
    var path            = ""
    if (firstSlashIndex > -1) {
      domain = tail.substring(0, firstSlashIndex)
      path = tail.substring(firstSlashIndex)
    } else
      domain = tail

    new BaseUriSplitter(host, domain, path)
  }

}
