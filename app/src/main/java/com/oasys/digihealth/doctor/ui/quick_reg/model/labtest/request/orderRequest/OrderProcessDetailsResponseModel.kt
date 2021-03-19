package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.orderRequest

data class OrderProcessDetailsResponseModel(
    var responseContents: OrderProcessDetails = OrderProcessDetails(),
    var req: Req = Req(),
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)