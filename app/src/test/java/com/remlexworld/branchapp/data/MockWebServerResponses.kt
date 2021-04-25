package com.remlexworld.branchapp.data


object MockWebServerResponses {

    val loginResponse: String = "{\"auth_token\":\"7ONC_1xxf6SKIPz7kF9CAQ\"}"

    val messageListResponse: String = "[{\"id\":25,\"thread_id\":12,\"user_id\":\"2035\",\"body\":\"Ok\",\"timestamp\":\"2017-02-03T09:06:39.000Z\",\"agent_id\":null},{\"id\":80,\"thread_id\":40,\"user_id\":\"5696\",\"body\":\"Iamsendingthefullamounttodayjustgotbusy\",\"timestamp\":\"2017-02-03T13:00:34.000Z\",\"agent_id\":null},{\"id\":99,\"thread_id\":56,\"user_id\":\"8647\",\"body\":\"Sorry,ImeantDecember2016\",\"timestamp\":\"2017-02-01T15:53:24.000Z\",\"agent_id\":null},{\"id\":72,\"thread_id\":33,\"user_id\":\"4442\",\"body\":\"Irequireafeedbackplz\",\"timestamp\":\"2017-02-02T14:22:54.000Z\",\"agent_id\":null},{\"id\":13,\"thread_id\":5,\"user_id\":\"779\",\"body\":\"HibranchIhavejustclearedmyloanwhichwasduetodaybutunfortunatelyyouhavedeniedme.Ihaven'tappliedforaloansinceDecemberbutyoursystemsaysthatIhaveappliedforaloanlastweek.Pleasereviewmyloan\",\"timestamp\":\"2017-02-02T17:29:24.000Z\",\"agent_id\":null},{\"id\":32,\"thread_id\":13,\"user_id\":\"2126\",\"body\":\"Ifthereisawayucancheckthempesasmsinmyphone..Checkandseealltransactionssmsareavailable....andmpesaaccountisveryactive\",\"timestamp\":\"2017-02-01T15:51:32.000Z\",\"agent_id\":null}]"

    val messageWithThreadId40: String = "{\"id\":80,\"thread_id\":40,\"user_id\":\"5696\",\"body\":\"Iamsendingthefullamounttodayjustgotbusy\",\"timestamp\":\"2017-02-03T13:00:34.000Z\",\"agent_id\":null}"

}