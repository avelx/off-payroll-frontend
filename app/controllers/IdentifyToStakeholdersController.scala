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

import javax.inject.Inject

import config.FrontendAppConfig
import controllers.actions._
import forms.IdentifyToStakeholdersFormProvider
import models.Answers._
import models.{ErrorTemplate, IdentifyToStakeholders, Mode}
import pages.IdentifyToStakeholdersPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.IdentifyToStakeholdersView

import scala.concurrent.Future

class IdentifyToStakeholdersController @Inject()(identify: IdentifierAction,
                                                 getData: DataRetrievalAction,
                                                 requireData: DataRequiredAction,
                                                 formProvider: IdentifyToStakeholdersFormProvider,
                                                 controllerComponents: MessagesControllerComponents,
                                                 view: IdentifyToStakeholdersView,
                                                 controllerHelper: ControllerHelper,
                                                 implicit val appConfig: FrontendAppConfig
                                                ) extends BaseController(controllerComponents) {

  val form: Form[IdentifyToStakeholders] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(request.userAnswers.get(IdentifyToStakeholdersPage).fold(form)(answerModel => form.fill(answerModel.answer)), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        controllerHelper.redirect(mode,value, IdentifyToStakeholdersPage, Some(ErrorTemplate("identifyToStakeholders.title")))
      }
    )
  }
}
