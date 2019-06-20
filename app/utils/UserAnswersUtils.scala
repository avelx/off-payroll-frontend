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

package utils

import config.FrontendAppConfig
import models.requests.DataRequest
import play.api.i18n.Messages
import play.api.mvc.Request
import viewmodels.AnswerSection
import play.twirl.api.Html
import views.html.components._
import views.html.components.details._
import views.ViewUtils._

trait UserAnswersUtils {

  def peopleInvolved(implicit checkYourAnswersHelper: ResultPageHelper, messages: Messages, request: Request[_], appConfig: FrontendAppConfig) = AnswerSection(
    headingKey = Some("result.peopleInvolved.h2"),
    whyResult = None,
    rows = Seq(
      (checkYourAnswersHelper.aboutYou, Some(hint_p(Html(messages("aboutYou.hint"))))),
      (checkYourAnswersHelper.contractStarted, None),
      (checkYourAnswersHelper.workerType, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def workersDuties(implicit checkYourAnswersHelper: ResultPageHelper, messages: Messages, request: Request[_], appConfig: FrontendAppConfig) = AnswerSection(
    headingKey = Some("result.workersDuties.h2"),
    whyResult = Some(Html(messages("result.officeHolderInsideIR35.whyResult.p1"))),
    rows = Seq(
      (checkYourAnswersHelper.officeHolder, Some(office_holder_details.apply()))
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def substitutesHelpers(implicit checkYourAnswersHelper: ResultPageHelper, messages: Messages, request: Request[_], appConfig: FrontendAppConfig) = AnswerSection(
    headingKey = Some("result.substitutesHelpers.h2"),
    whyResult = Some(Html(messages("result.substitutesAndHelpers.summary"))),
    rows = Seq(
      (checkYourAnswersHelper.arrangedSubstitute, Some(hint(arranged_substitute_details.apply()))),
      (checkYourAnswersHelper.didPaySubstitute, Some(exclamation(Html(messages(tailorMsg("didPaySubstitute.exclamation")))))),
      (checkYourAnswersHelper.rejectSubstitute, Some(reject_substitute_details.apply())),
      (checkYourAnswersHelper.wouldWorkerPaySubstitute, Some(exclamation(Html(messages(tailorMsg("wouldWorkerPaySubstitute.exclamation")))))),
      (checkYourAnswersHelper.neededToPayHelper, Some(hint(needed_to_pay_helper_details.apply())))
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def workArrangements(implicit checkYourAnswersHelper: ResultPageHelper, messages: Messages, request: Request[_], appConfig: FrontendAppConfig) = AnswerSection(
    headingKey = Some("result.workArrangements.h2"),
    whyResult = Some(Html(messages("result.workArrangements.summary"))),
    rows = Seq(
      (checkYourAnswersHelper.moveWorker, Some(hint_p(Html(messages(tailorMsg("moveWorker.hint")))))),
      (checkYourAnswersHelper.howWorkIsDone, Some(hint_p(Html(messages(tailorMsg("howWorkIsDone.hint")))))),
      (checkYourAnswersHelper.scheduleOfWorkingHours, None),
      (checkYourAnswersHelper.chooseWhereWork, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def financialRisk(implicit checkYourAnswersHelper: ResultPageHelper, messages: Messages, request: Request[_], appConfig: FrontendAppConfig) = AnswerSection(
    headingKey = Some("result.financialRisk.h2"),
     whyResult = Some(Html(messages("result.financialRisk.summary"))),
    rows = Seq(
      (checkYourAnswersHelper.cannotClaimAsExpense, None),
      (checkYourAnswersHelper.howWorkerIsPaid, None),
      (checkYourAnswersHelper.putRightAtOwnCost, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def partAndParcel(implicit checkYourAnswersHelper: ResultPageHelper, messages: Messages, request: Request[_], appConfig: FrontendAppConfig) = AnswerSection(
    headingKey = Some("result.partAndParcel.h2"),
    whyResult = Some(Html(messages("result.partParcel.summary"))),
    rows = Seq(
      (checkYourAnswersHelper.benefits, Some(hint(benefits_details.apply()))),
      (checkYourAnswersHelper.lineManagerDuties, Some(hint(line_manager_duties_details.apply()))),
      (checkYourAnswersHelper.interactWithStakeholders, Some(hint_p(Html(messages(tailorMsg("interactWithStakeholders.hint")))))),
      (checkYourAnswersHelper.identifyToStakeholders, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def answers(implicit request: DataRequest[_], messages: Messages, frontendAppConfig: FrontendAppConfig): Seq[AnswerSection] = {
    implicit val checkYourAnswersHelper: ResultPageHelper = new ResultPageHelper(request.userAnswers)
    Seq(peopleInvolved,workersDuties,substitutesHelpers,workArrangements,financialRisk,partAndParcel)
  }

}
