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

package connectors.mocks

import MultiDecision.Result
import cats.data.EitherT
import connectors.{DataCacheConnector, DecisionConnector}
import models.{DecisionResponse, ErrorResponse, Interview}
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Writes
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockDecisionConnector extends MockFactory {

  lazy val mockDecisionConnector = mock[DecisionConnector]

  def mockDecide(decisionRequest: Interview)(response: Either[ErrorResponse, DecisionResponse]): Unit = {
    (mockDecisionConnector.decide(_: Interview)(_: HeaderCarrier, _: ExecutionContext))
      .expects(decisionRequest, *, *)
      .returns(Future.successful(response))
  }

  def mockDecideSection(writes: Writes[Interview])(response: Result[Boolean]): Unit = {
    (mockDecisionConnector.decideSection(_: Interview,_: Writes[Interview])(_: HeaderCarrier, _: ExecutionContext))
      .expects(*,writes, *, *)
      .returns(response)
  }

  def mockLog(decisionRequest: Interview, decisionResponse: DecisionResponse)(response: Either[ErrorResponse, Boolean]): Unit ={
    (mockDecisionConnector.log(_: Interview, _: DecisionResponse)(_: HeaderCarrier, _: ExecutionContext))
      .expects(decisionRequest, decisionResponse, *, *)
      .returns(Future.successful(response))
  }

}
