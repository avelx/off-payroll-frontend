/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.offpayroll

import java.io.File

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import play.Logger
import play.api.Mode._
import play.api.mvc.{Request, RequestHeader, Result, Filter}
import play.api.{Application, Configuration, Play}
import play.twirl.api.Html
import uk.gov.hmrc.crypto.ApplicationCrypto
import uk.gov.hmrc.play.audit.filters.FrontendAuditFilter
import uk.gov.hmrc.play.config.{AppName, ControllerConfig, RunMode}
import uk.gov.hmrc.play.frontend.bootstrap.DefaultFrontendGlobal
import uk.gov.hmrc.play.http.logging.filters.FrontendLoggingFilter
import uk.gov.hmrc.play.filters.MicroserviceFilterSupport
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

import scala.concurrent.Future

object FrontendGlobal
  extends DefaultFrontendGlobal {

  override val auditConnector = FrontendAuditConnector
  override val loggingFilter = LoggingFilter
  override val frontendAuditFilter = AuditFilter
  override def defaultFrontendFilters = CSRFNoCheckFilter +: super.defaultFrontendFilters

  override def onStart(app: Application) {
    super.onStart(app)
    ApplicationCrypto.verifyConfiguration()
    play.api.Logger.debug("In FrontendGlobal")
  }

  override def standardErrorTemplate(pageTitle: String, heading: String, message: String)(implicit rh: Request[_]): Html =
    uk.gov.hmrc.offpayroll.views.html.error_template(pageTitle, heading, message)

  override def microserviceMetricsConfig(implicit app: Application): Option[Configuration] = app.configuration.getConfig(s"microservice.metrics")

  def getStackTrace(stackTrace: Array[StackTraceElement]) = {

    stackTrace.toList.map(_.toString).mkString("\n")
  }

  override def resolveError(rh: RequestHeader, ex: Throwable): Result = {
    Logger.info(s"*** Intercepting resolveError ${ex.getMessage} \n Stacktrace \n ${getStackTrace(ex.getStackTrace)}")
    super.resolveError(rh, ex)
  }

}

object ControllerConfiguration extends ControllerConfig {
  lazy val controllerConfigs = Play.current.configuration.underlying.as[Config]("controllers")
}

object LoggingFilter extends FrontendLoggingFilter with MicroserviceFilterSupport {
  override def controllerNeedsLogging(controllerName: String) = ControllerConfiguration.paramsForController(controllerName).needsLogging
}

object AuditFilter extends FrontendAuditFilter with RunMode with AppName with MicroserviceFilterSupport {

  override lazy val maskedFormFields = Seq("password")

  override lazy val applicationPort = None

  override lazy val auditConnector = FrontendAuditConnector

  override def controllerNeedsAuditing(controllerName: String) = ControllerConfiguration.paramsForController(controllerName).needsAuditing
}

object CSRFNoCheckFilter extends Filter with MicroserviceFilterSupport {
  def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader) =
    f(rh.copy(headers = rh.headers.add("Csrf-Token" -> "nocheck")))
}
