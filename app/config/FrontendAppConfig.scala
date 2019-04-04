/*
 * Copyright 2019 HM Revenue & Customs
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

package config

import com.google.inject.{Inject, Singleton}
import controllers.routes
import play.api.Environment
import play.api.i18n.Lang
import play.api.mvc.Request
import uk.gov.hmrc.play.binders.ContinueUrl
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}

@Singleton
class FrontendAppConfig @Inject() (environment: Environment, val servicesConfig: ServicesConfig, runMode: RunMode) {

  private lazy val contactHost = servicesConfig.getString("contact-frontend.host")
  private val contactFormServiceIdentifier = "offpayrollfrontend"

  lazy val analyticsToken = servicesConfig.getString(s"google-analytics.token")
  lazy val analyticsHost = servicesConfig.getString(s"google-analytics.host")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  private def requestUri(implicit request: Request[_]) = ContinueUrl(host + request.uri).encodedUrl
  def feedbackUrl(implicit request: Request[_]): String =
    s"$contactHost/contact/beta-feedback-unauthenticated?service=$contactFormServiceIdentifier&backUrl=$requestUri"

  lazy val loginUrl = servicesConfig.getString("urls.login")
  lazy val loginContinueUrl = servicesConfig.getString("urls.loginContinue")

  lazy val host: String = servicesConfig.getString("host")

  lazy val languageTranslationEnabled = servicesConfig.getBoolean("microservice.services.features.welsh-translation")
  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy"))
  def routeToSwitchLanguage = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)

  lazy val mongoTtl: Int = servicesConfig.getInt("mongodb.timeToLiveInSeconds")

}