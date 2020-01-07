/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import models.sections.partAndParcel.IdentifyToStakeholders
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class IdentifyToStakeholdersSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues {

  "IdentifyToStakeholders" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(IdentifyToStakeholders.values)

      forAll(gen) {
        identifyToStakeholders =>

          JsString(identifyToStakeholders.toString).validate[IdentifyToStakeholders].asOpt.value mustEqual identifyToStakeholders
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!IdentifyToStakeholders.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[IdentifyToStakeholders] mustEqual JsError("Unknown identifyToStakeholders")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(IdentifyToStakeholders.values)

      forAll(gen) {
        identifyToStakeholders =>

          Json.toJson(identifyToStakeholders) mustEqual JsString(identifyToStakeholders.toString)
      }
    }
  }
}
