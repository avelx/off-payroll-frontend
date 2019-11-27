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
import forms.sections.setup.ContractStartedFormProvider
import models.Mode
import models.requests.DataRequest
import navigation.SetupNavigator
import pages.sections.setup.ContractStartedPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}

import scala.concurrent.Future

class ContractStartedController @Inject()(identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: ContractStartedFormProvider,
                                          controllerComponents: MessagesControllerComponents,
                                          optimisedView: views.html.sections.setup.ContractStartedView,
                                          checkYourAnswersService: CheckYourAnswersService,
                                          compareAnswerService: CompareAnswerService,
                                          dataCacheConnector: DataCacheConnector,
                                          navigator: SetupNavigator,
                                          implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController(controllerComponents,compareAnswerService,dataCacheConnector,navigator) with FeatureSwitching {

  def form(implicit request: DataRequest[_]): Form[Boolean] = formProvider()

  def renderView(mode: Mode, oForm: Option[Form[Boolean]] = None)(implicit request: DataRequest[_]) = {
    val formData = oForm.getOrElse(fillForm(ContractStartedPage, form))
    optimisedView(formData, mode)
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
      Ok(optimisedView(request.userAnswers.get(ContractStartedPage).fold(form)(answerModel => form.fill(answerModel.answer)), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(renderView(mode, Some(formWithErrors)))),
      value => redirect(mode,value,ContractStartedPage)
    )
  }
}
