package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class WorkerTypeControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /worker-trade-type" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-trade-type")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-trade-type")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-trade-type", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/worker-trade-type", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you already started working for this client?")
      }
    }
  }

  s"Post or Get to /worker-trade-type/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-trade-type/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-trade-type/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-trade-type/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/worker-trade-type/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you already started working for this client?")
      }
    }
  }
}
