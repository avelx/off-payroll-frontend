package helpers


import play.api.libs.json.Json



trait TestData {
  val emptyValue = Json.toJson({""->""})
  val defaultValue = "csrfToken=123"
  val aboutYouValue = "value=personDoingWork"
  val selectedNo = "value=false"
  val selectedYes = "value=true"
  val arrangeSubValue = "value=noSubstitutionHappened"
  val taskChangeValue = "value=canMoveWorkerWithPermission"
  val workerCompValue = "value=incomeFixed"
  val howWorkDoneValue = "value=workerDecidesWithoutInput"
  val putWorkRightValue = "value=noObligationToCorrect"
  val chooseWhereDoneValue = "value=workerChooses"
  val chooseWhenDoneValue = "value=workerDecideSchedule"
  val introduceValue = "value=workAsIndependent"
  val cannotClaimValue = "cannotClaimAsExpense[]=expensesAreNotRelevantForRole"
  val whoAreYouValue = "value=personDoingWork"
  val whatDoYouWantToDoValue = "value=makeNewDetermination"
  val whatDoYouWantToFindOutValue = "value=ir35"
  val workerTypeValue = "value=limitedCompany"
}
