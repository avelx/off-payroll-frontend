package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class TurnoverOverControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /turnover-over" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/turnover-over", cookies,true)
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/turnover-over")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/turnover-over",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/turnover-over",selectedNo, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }

    }

  }

  s"Post or Get to /turnover-over/edit" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/turnover-over/edit", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/turnover-over/edit")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/turnover-over/edit",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/turnover-over/edit",selectedNo, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }

    }

  }


  s"Post or Get to /private-sector-turnover" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/private-sector-turnover", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/private-sector-turnover")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/private-sector-turnover",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/private-sector-turnover",selectedNo, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }

    }

  }

  s"Post or Get to /private-sector-turnover/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/private-sector-turnover/change", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/private-sector-turnover/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/private-sector-turnover/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/private-sector-turnover/change",selectedNo, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }

    }

  }

}
