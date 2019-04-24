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

package connectors.HttpParsers

import play.api.Logger
import play.api.http.Status
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object PDFGeneratorHttpParser {

  type Response = Either[ErrorResponse, SuccessResponse]

  val reads: HttpReads[Response] = new HttpReads[Response] {
    override def read(method: String, url: String, response: HttpResponse): Response = {
      response.status match {
        case Status.OK => Right(SuccessfulPDF(response.body))
        case Status.BAD_REQUEST =>
          Logger.debug(s"[PDFGeneratorHttpParser][updateReads]: Bad Request returned from PDF Generator Service:\n\n $response")
          Logger.warn(s"[PDFGeneratorHttpParser][updateReads]: Bad Request returned from PDF Generator Service")
          Left(BadRequest)
        case status =>
          Logger.debug(s"[PDFGeneratorHttpParser][updateReads]: $status returned from PDF Generator Service:\n\n $response")
          Logger.warn(s"[PDFGeneratorHttpParser][updateReads]: $status returned from PDF Generator Service")
          Left(UnexpectedError(status))
      }
    }
  }

  sealed trait ErrorResponse {
    val status: Int = Status.INTERNAL_SERVER_ERROR
    val body: String
  }
  case object BadRequest extends ErrorResponse {
    override val status: Int = Status.BAD_REQUEST
    override val body: String = "Bad Request returned from PDF Generator Service"
  }
  case class UnexpectedError(override val status: Int,
                             override val body: String = "Unexpected Response returned from PDF Generator Service") extends ErrorResponse

  sealed trait SuccessResponse
  case class SuccessfulPDF(body: String) extends SuccessResponse

}