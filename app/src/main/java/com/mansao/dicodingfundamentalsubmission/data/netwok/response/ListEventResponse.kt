package com.mansao.dicodingfundamentalsubmission.data.netwok.response

import com.google.gson.annotations.SerializedName

data class ListEventResponse(

	@field:SerializedName("listEvents")
	val listEvents: List<EventDto>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

