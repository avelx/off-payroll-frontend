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

package controllers.sections.businessOnOwnAccount

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.{BaseController, BaseNavigationController}
import controllers.actions._
import forms.SimilarWorkOtherClientsFormProvider
import javax.inject.Inject
import models.Mode
import navigation.BusinessOnOwnAccountNavigator
import pages.SimilarWorkOtherClientsPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CompareAnswerService, DecisionService}
import views.html.sections.businessOnOwnAccount.SimilarWorkOtherClientsView

import scala.concurrent.Future

class SimilarWorkOtherClientsController @Inject()(dataCacheConnector: DataCacheConnector,
                                                  navigator: BusinessOnOwnAccountNavigator,
                                                  identify: IdentifierAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  formProvider: SimilarWorkOtherClientsFormProvider,
                                                  controllerComponents: MessagesControllerComponents,
                                                  compareAnswerService: CompareAnswerService,
                                                  decisionService: DecisionService,
                                                  view: SimilarWorkOtherClientsView,
                                                  implicit val appConfig: FrontendAppConfig) extends BaseNavigationController(
controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(SimilarWorkOtherClientsPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode, value, SimilarWorkOtherClientsPage)
    )
  }
}