package com.spgroup.spapp.presentation.view

import android.content.Context
import android.widget.RelativeLayout
import com.spgroup.spapp.domain.model.ServiceItem

abstract class ServiceItemView : RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context, serviceItem: ServiceItem) : super(context, null)

}