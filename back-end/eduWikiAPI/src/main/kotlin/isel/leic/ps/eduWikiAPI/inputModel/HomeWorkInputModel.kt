package isel.leic.ps.eduWikiAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class HomeWorkInputModel (
        @JsonProperty("delivery_date")
        val deliveryDate: String,
        @JsonProperty("off_limit_delivery")
        val offLimitDelivery: Boolean,
        @JsonProperty("multiple_deliveries")
        val multipleDeliveries: Boolean,
        val statement: String
)