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

package controllers.sections.setup

import javax.inject.Inject

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.setup.{WorkerTypeFormProvider, WorkerUsingIntermediaryFormProvider}
import models.Mode
import models.requests.DataRequest
import models.sections.setup.WorkerType
import navigation.SetupNavigator
import pages.sections.setup.{WorkerTypePage, WorkerUsingIntermediaryPage}
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import play.twirl.api.Html
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.setup.WorkerUsingIntermediaryView

import scala.concurrent.Future

class WorkerTypeController @Inject()(identify: IdentifierAction,
                                     getData: DataRetrievalAction,
                                     requireData: DataRequiredAction,
                                     workerTypeFormProvider: WorkerTypeFormProvider,
                                     newFormProvider: WorkerUsingIntermediaryFormProvider,
                                     controllerComponents: MessagesControllerComponents,
                                     workerUsingIntermediaryView: WorkerUsingIntermediaryView,
                                     checkYourAnswersService: CheckYourAnswersService,
                                     compareAnswerService: CompareAnswerService,
                                     dataCacheConnector: DataCacheConnector,
                                     navigator: SetupNavigator,
                                     implicit val appConfig: FrontendAppConfig) extends BaseNavigationController(controllerComponents,compareAnswerService,dataCacheConnector,navigator) with FeatureSwitching {

  val workerTypeForm: Form[WorkerType] = workerTypeFormProvider()
  def workerUsingIntermediaryForm(implicit request: DataRequest[_]): Form[Boolean] = newFormProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    submitWorkerUsingIntermediary(mode)
  }

  private[controllers] def view(mode: Mode)(implicit request: DataRequest[_]): Html = {
    workerUsingIntermediaryView(request.userAnswers.get(WorkerUsingIntermediaryPage).fold(workerUsingIntermediaryForm)
    (answerModel => workerUsingIntermediaryForm.fill(answerModel.answer)), mode)
  }

  private[controllers] def submitWorkerUsingIntermediary(mode: Mode)(implicit request: DataRequest[AnyContent]): Future[Result] =
    workerUsingIntermediaryForm.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(workerUsingIntermediaryView(formWithErrors, mode))),
      value => redirect(mode,value,WorkerUsingIntermediaryPage)
    )
}
