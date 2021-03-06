/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package config

import java.util.Base64

import com.google.inject.{Inject, Singleton}
import config.featureSwitch.{DecisionServiceVersionFeature, FeatureSwitching}
import controllers.routes
import play.api.Environment
import play.api.i18n.Lang
import play.api.mvc.{Call, Request}
import uk.gov.hmrc.play.binders.ContinueUrl
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}

@Singleton
class FrontendAppConfig @Inject() (environment: Environment, val servicesConfig: ServicesConfig, runMode: RunMode) {

  private lazy val contactHost = servicesConfig.getString("contact-frontend.host")
  private val contactFormServiceIdentifier = "off-payroll"

  lazy val analyticsToken = servicesConfig.getString(s"google-analytics.token")
  lazy val analyticsHost = servicesConfig.getString(s"google-analytics.host")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  private lazy val exitSurveyBaseUrl = servicesConfig.getString("feedback-frontend.host") + servicesConfig.getString("feedback-frontend.url")
  lazy val exitSurveyUrl = s"$exitSurveyBaseUrl/$contactFormServiceIdentifier"

  private def whitelistConfig(key: String): Seq[String] =
    Some(new String(Base64.getDecoder.decode(servicesConfig.getString(key)), "UTF-8"))
      .map(_.split(",")).getOrElse(Array.empty).toSeq

  lazy val whitelistEnabled: Boolean = servicesConfig.getBoolean("whitelist.enabled")
  lazy val whitelistedIps: Seq[String] = whitelistConfig("whitelist.allowedIps")
  lazy val whitelistExcludedPaths: Seq[Call] = whitelistConfig("whitelist.excludedPaths").map(path => Call("GET", path))
  lazy val shutterPage: String = servicesConfig.getString("whitelist.shutter-page-url")

  private def requestPath(implicit request: Request[_]) = ContinueUrl(host + request.path).encodedUrl
  def feedbackUrl(implicit request: Request[_]): String =
    s"$contactHost/contact/beta-feedback-unauthenticated?service=$contactFormServiceIdentifier&backUrl=$requestPath"
  def reportAccessibilityIssueUrl(problemPageUri: String)(implicit request: Request[_]): String =
    s"$contactHost/contact/accessibility-unauthenticated?service=${contactFormServiceIdentifier}&userAction=${ContinueUrl(host + problemPageUri).encodedUrl}"

  lazy val loginUrl = servicesConfig.getString("urls.login")
  lazy val loginContinueUrl = servicesConfig.getString("urls.loginContinue")

  lazy val host: String = servicesConfig.getString("host")

  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy"))
  def routeToSwitchLanguage: String => Call = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)

  lazy val mongoTtl: Int = servicesConfig.getInt("mongodb.timeToLiveInSeconds")

  def decisionVersion = FeatureSwitching.getValue(DecisionServiceVersionFeature)(this)

  lazy val pdfGeneratorService = servicesConfig.baseUrl("pdf-generator-service")
  lazy val assetsFrontendUrl = servicesConfig.getString("assets.url")
  lazy val assetsFrontendVersion = servicesConfig.getString("assets.version")

  lazy val timeoutPeriod: Int = servicesConfig.getInt("timeout.period")
  lazy val timeoutCountdown: Int = servicesConfig.getInt("timeout.countdown")

  lazy val govUkStartPageUrl = servicesConfig.getString("urls.govUkStartPage")
  lazy val employmentStatusManualUrl = servicesConfig.getString("urls.employmentStatusManual")
  lazy val employmentStatusManualChapter5Url = servicesConfig.getString("urls.employmentStatusManualChapter5")
  lazy val employmentStatusManualESM0527Url = servicesConfig.getString("urls.employmentStatusManualESM0527")
  lazy val employmentStatusManualESM0521Url = servicesConfig.getString("urls.employmentStatusManualESM0521")
  lazy val officeHolderUrl = servicesConfig.getString("urls.officeHolder")
  lazy val understandingOffPayrollUrl = servicesConfig.getString("urls.understandingOffPayroll")
  lazy val feePayerResponsibilitiesUrl = servicesConfig.getString("urls.feePayerResponsibilities")
  lazy val payeForEmployersUrl = servicesConfig.getString("urls.payeForEmployers")
  lazy val govukAccessibilityStatementUrl = servicesConfig.getString("urls.govukAccessibilityStatement")
  lazy val abilityNetUrl = servicesConfig.getString("urls.abilityNet")
  lazy val wcagUrl = servicesConfig.getString("urls.wcag")
  lazy val eassUrl = servicesConfig.getString("urls.eass")
  lazy val ecniUrl = servicesConfig.getString("urls.ecni")
  lazy val hmrcAdditionalNeedsUrl = servicesConfig.getString("urls.hmrcAdditionalNeeds")
  lazy val dacUrl = servicesConfig.getString("urls.dac")

  lazy val accessibilityStatementLastUpdatedDay = servicesConfig.getString("accessibilityStatementLastUpdatedDate.day")
  lazy val accessibilityStatementLastUpdatedMonth = servicesConfig.getString("accessibilityStatementLastUpdatedDate.month")
  lazy val accessibilityStatementLastUpdatedYear = servicesConfig.getString("accessibilityStatementLastUpdatedDate.year")
  lazy val lastDacTestDay = servicesConfig.getString("lastDacTestDate.day")
  lazy val lastDacTestMonth = servicesConfig.getString("lastDacTestDate.month")
  lazy val lastDacTestYear = servicesConfig.getString("lastDacTestDate.year")

}
