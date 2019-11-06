package controllers.partAndParcel

import helpers.IntegrationSpecBase

class LineManagerDutiesControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /management-responsibilities" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/management-responsibilities")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will you have any management responsibilities for your client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/management-responsibilities")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/management-responsibilities", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you have any management responsibilities for your client?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/management-responsibilities", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("How would you introduce yourself to your client’s consumers or suppliers?")
      }
    }
  }

  s"Post or Get to /management-responsibilities/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/management-responsibilities/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you have any management responsibilities for your client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/management-responsibilities/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/management-responsibilities/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you have any management responsibilities for your client?")

      }
    }

    "Return a 409 on Successful post and move onto something went wrong" in {

      lazy val res = postSessionRequest("/management-responsibilities/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }
    }
  }
}
