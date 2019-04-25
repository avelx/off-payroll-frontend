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

package controllers

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.ArrangedSubstituteFormProvider
import javax.inject.Inject

import models.{ArrangedSubstitute, Enumerable, Mode}
import navigation.Navigator
import services.CompareAnswerService
import pages.ArrangedSubstitutePage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.ArrangedSubstituteView

import scala.concurrent.{ExecutionContext, Future}

class ArrangedSubstituteController @Inject()(dataCacheConnector: DataCacheConnector,
                                             navigator: Navigator,
                                             identify: IdentifierAction,
                                             getData: DataRetrievalAction,
                                             requireData: DataRequiredAction,
                                             formProvider: ArrangedSubstituteFormProvider,
                                             controllerComponents: MessagesControllerComponents,
                                             view: ArrangedSubstituteView,
                                             implicit val appConfig: FrontendAppConfig
                                           ) extends FrontendController(controllerComponents) with I18nSupport with Enumerable.Implicits with CompareAnswerService[ArrangedSubstitute]{
  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form: Form[ArrangedSubstitute] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(appConfig, request.userAnswers.get(ArrangedSubstitutePage).fold(form)(form.fill), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(appConfig, formWithErrors, mode))),
      value => {
        val answers = constructAnswers(request,value,ArrangedSubstitutePage)
        dataCacheConnector.save(answers.cacheMap).map(
          _ => Redirect(navigator.nextPage(ArrangedSubstitutePage, mode)(answers))
        )
      }
    )
  }
}